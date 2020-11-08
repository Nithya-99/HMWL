package com.example.hmwl;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    private List<GridProductModel> gridProductModelList;

    public GridProductLayoutAdapter(List<GridProductModel> gridProductModelList) {
        this.gridProductModelList = gridProductModelList;
    }

    @Override
    public int getCount() {

        return gridProductModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, null);
            ImageView productImage = view.findViewById(R.id.productImage);
            TextView productTitle = view.findViewById(R.id.productName);
            TextView productPrice = view.findViewById(R.id.productPrice);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(view.getContext(), ProductDetailsActivity.class);
                    view.getContext().startActivity(productDetailsIntent);
                }
            });

            Glide.with(parent.getContext()).load(gridProductModelList.get(position).getProductImage())
                    .apply(new RequestOptions().placeholder(R.drawable.home_blue)).into(productImage);
            //productImage.setImageResource(gridProductModelList.get(position).getProductImage());
            productTitle.setText(gridProductModelList.get(position).getProductTitle());
            productPrice.setText("Rs."+gridProductModelList.get(position).getProductPrice()+"/-");
        } else {
            view = convertView;
        }
        return view;
    }
}
