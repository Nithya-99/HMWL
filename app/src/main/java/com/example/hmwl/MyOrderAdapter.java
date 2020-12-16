package com.example.hmwl;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.type.Date;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {

    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder viewholder, int position) {
        String resource = myOrderItemModelList.get(position).getProductImage();
//        int rating = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String orderStatus = myOrderItemModelList.get(position).getOrderstatus();
        Date date;
//        switch (orderStatus){
//
//            case "Ordered":
//                date = myOrderItemModelList.get(position).getOrderedDate();
//                break;
//        }
//        String deliveredDate = myOrderItemModelList.get(position).getDeliveryStatus();
        String address = myOrderItemModelList.get(position).getAddress();

        viewholder.setData(resource,title,address);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage, orderIndicator;
        private TextView productTitle, deliveryStatus, orderAddress;
//        private LinearLayout rateNowContainer;

        public Viewholder(@NonNull final View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
            orderAddress = itemView.findViewById(R.id.order_address_value);
//            rateNowContainer = itemView.findViewById(R.id.rate_now_container);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent orderDetailsIntent = new Intent(itemView.getContext(),OrderDetailsActivity.class);
//                    itemView.getContext().startActivity(orderDetailsIntent);
//                }
//            });
        }
        private void setData(String resource,String title, String address){
            Glide.with(itemView.getContext()).load(resource).into(productImage);
            productTitle.setText(title);
            orderAddress.setText(address);

//            if(deliveredDate.equals("Cancelled")) {
//                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
//            }
//            else{
//                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
//            }
//            deliveryStatus.setText(deliveredDate);
//////////////////////////////////////////////////////////rating layout
//            setRating(rating);
//            for(int x =0; x<rateNowContainer.getChildCount(); x++){
//                final int starPosition = x;
//                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setRating(starPosition);
//                    }
//                });
//            }
            ////////////////////////////////////////////////////////////rating layout
        }
        ////////////////////////////////////////////////////////////////rating layout
//        private void setRating(int starPosition) {
//
//            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
//
//                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
//                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
//
//                if (x <= starPosition) {
//                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
//                }
//            }
//        }
        //////////////////////////////////////////////////////////////////////rating layout
    }

}
