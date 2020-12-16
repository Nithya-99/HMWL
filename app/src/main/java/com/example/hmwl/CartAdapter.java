package com.example.hmwl;

import android.app.Dialog;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                String productPriceText = cartItemModelList.get(position).getProductPrice();
                int quantity = cartItemModelList.get(position).getProductQuantity();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();


                ((CartItemViewholder)viewHolder).setItemDetails(productID ,resource,title,productPriceText,position, quantity, qtyIds);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for (int x=0; x< cartItemModelList.size(); x++){
                    int qty=Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                    totalItems=totalItems+ qty;
                    if(qty==0){
                        qty=qty+1;
                        if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM){
                            totalItems++;
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice()) * qty;
                        }
                    }
                    else{
                        if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM){
                            totalItems++;
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice()) * qty;
                        }
                    }

                }
                if (totalItemPrice > 500){
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                }else {
                    deliveryPrice = "100";
                    totalAmount = totalItemPrice + 100;
                }

//                String deliveryPrice = cartItemModelList.get(position).getDeliveryPrice();
//                String totalAmount = cartItemModelList.get(position).getTotalAmount();
//                String savedAmount = cartItemModelList.get(position).getSavedAmount();

                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;

        private LinearLayout deleteBtn;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);

            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
        }
        private void setItemDetails(String productID,String resource, String title, String productPriceText, int position, int quantity, List<String> qtyIds){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.border_background)).into(productImage);
            productTitle.setText(title);
            productPrice.setText(productPriceText);

            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog quantityDialog = new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDialog.setCancelable(false);
                    quantityDialog.show();
                    EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                    Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                    Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            quantityDialog.dismiss();
                        }

                    });

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!TextUtils.isEmpty(quantityNo.getText())) {
                                if (Long.valueOf(quantityNo.getText().toString()) <= 20) {
                                    if (itemView.getContext() instanceof MainActivity) {
                                        cartItemModelList.get(position).setProductQuantity(Integer.valueOf(quantityNo.getText().toString()));
                                    } else {
                                        if (DeliveryActivity.fromCart) {
                                            cartItemModelList.get(position).setProductQuantity(Integer.valueOf(quantityNo.getText().toString()));
                                        } else {
                                            DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Integer.valueOf(quantityNo.getText().toString()));
                                        }
                                    }

                                    productQuantity.setText("Qty: " + quantityNo.getText());
                                    notifyItemChanged(cartItemModelList.size()-1);


                                    if (!showDeleteBtn) {
                                        int initialQty = Integer.parseInt(String.valueOf(productQuantity));
                                        int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                        final FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();

                                        for (int y = 0; y < finalQty - initialQty; y++) {
                                            final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                            Map<String, Object> timeStamp = new HashMap<>();
                                            timeStamp.put("time", FieldValue.serverTimestamp());

                                            final int finalY = y;
                                            firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName)
                                                    .set(timeStamp)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                qtyIds.add(quantityDocumentName);
                                                                if (finalY + 1 == finalQty - initialQty) {
                                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        List<String> serverQuantity = new ArrayList<>();

                                                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                            serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                        }

                                                                                        DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                    } else {
                                                                                        String error = task.getException().getMessage();
                                                                                        Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                    }
//                                                                                loadingDialog.dismiss();
                                                                                }
                                                                            });

                                                                }

                                                            } else {
//                                                            loadingDialog.dismiss();
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }


                                    }

                                    quantityDialog.dismiss();
                                }
                            }
                        }
                    });

            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            } else{
                deleteBtn.setVisibility(View.GONE);
            }

                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!ProductDetailsActivity.running_cart_query) {
                                ProductDetailsActivity.running_cart_query = true;

                                DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                            }
                        }
                    });
                }
            });
        }

    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }
        private void setTotalAmount(int totalItemText,int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText){
            totalItems.setText("Price");
            totalItemPrice.setText("Rs."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")){
                deliveryPrice.setText(deliveryPriceText);
            }
            else{
                deliveryPrice.setText("Rs."+deliveryPriceText+"/-");
            }
            totalAmount.setText("Rs."+totalAmountText+"/-");
            cartTotalAmount.setText("Rs."+totalAmountText+"/-");
            savedAmount.setText("you saved Rs."+savedAmountText+"/- on this product");
            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();

            if (totalItemPriceText == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }
            else{
                parent.setVisibility(View.VISIBLE);
            }
//            deliveryPrice.setText(deliveryPriceText);
//            totalAmount.setText(totalAmountText);
//            savedAmount.setText(savedAmountText);
        }
    }
}

