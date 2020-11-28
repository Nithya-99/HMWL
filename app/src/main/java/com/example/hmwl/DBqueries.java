package com.example.hmwl;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {
//    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String email,fullname,profile;
    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

    //public static List<HomePageModel> homePageModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesName = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;

    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static void loadCategories(final CategoryAdapter categoryAdapter, final Context context){

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),
                                        documentSnapshot.get("categoryName").toString()));
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
        
    }

    public static void loadFragmentData(final HomePageAdapter adapter, final Context context, final int index, String categoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase()).collection("HOME").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if((long)documentSnapshot.get("view_type") == 0){
                                    List<GridProductModel> gridProductModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for(long x = 1; x < no_of_products + 1; x++){
                                        gridProductModelList.add(new GridProductModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0
                                            ,documentSnapshot.get("layout_title").toString()
                                            ,gridProductModelList));
                                }
                            }
                            adapter.notifyDataSetChanged();
                           // HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public static void loadCartList(final Context context, final boolean loadProductData){
        cartList.clear();
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x = 0 ; x < (Long) task.getResult().get("list_size");x++){
                        cartList.add(task.getResult().get("product_ID_" +x).toString());

                        if(DBqueries.cartList.contains(ProductDetailsActivity.productID)){
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }

                        if (loadProductData){
                            cartItemModelList.clear();
                            final String productID = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        int index = 0;
                                        if (cartList.size() >= 2){
                                            index = cartList.size() -2;
                                        }
                                        cartItemModelList.add(index,new CartItemModel(CartItemModel.CART_ITEM, productID
                                                , task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , task.getResult().get("product_price").toString()
                                                , (Integer) 0));

                                        if (cartList.size() == 1){
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                        }
                                        if (cartList.size() == 0){
                                            cartItemModelList.clear();
                                        }

                                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                                    }
                                }
                            });

                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void removeFromCart(final int index, final Context context){

        final String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String,Object> updateCartList = new HashMap<>();

        for(int x=0; x<cartList.size();x++){
            updateCartList.put("product_ID_"+x,cartList.get(x));
        }
        updateCartList.put("list_size",(long) cartList.size());

        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {// set krne se new document create hota h.......
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(cartItemModelList.size() !=0 ){
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0 ){
//                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
//                        parent.setVisibility(View.GONE);
                        cartItemModelList.clear();
                    }
                    Toast.makeText(context, "removed successfully!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    cartList.add(index,removedProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

                ProductDetailsActivity.running_cart_query = false;
            }
        });

    }


    public static void loadAddresses(final Context context, Dialog loadingDialog, boolean gotoDeliveryActivity){

        addressesModelList.clear();
        firebaseFirestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Intent deliveryIntent= null;;
                    if((long) task.getResult().get("list_size") == 0) {
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    }
                    else{

                        for(long x = 1; x < (long)task.getResult().get("list_size") + 1; x++){
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    (boolean)task.getResult().get("selected_"+x)));
                            if((boolean)task.getResult().get("selected_"+x)){
                                selectedAddress = Integer.parseInt(String.valueOf(x-1));
                            }
                        }
                        if(gotoDeliveryActivity){
                            deliveryIntent = new Intent(context, DeliveryActivity.class);
                        }
                    }
                    if(gotoDeliveryActivity){
                        context.startActivity(deliveryIntent);
                    }
                } else{
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesName.clear();
        cartList.clear();
        cartItemModelList.clear();
    }

}
