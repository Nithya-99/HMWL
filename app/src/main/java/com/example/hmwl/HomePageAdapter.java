package com.example.hmwl;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()){
            case 0:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout, viewGroup, false);
                return new GridProductViewHolder(gridProductView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (homePageModelList.get(position).getType()){
            case HomePageModel.GRID_PRODUCT_VIEW:
                String title = homePageModelList.get(position).getTitle();
                List<GridProductModel> gridProductModelList = homePageModelList.get(position).getGridProductModelList();
                ((GridProductViewHolder)viewHolder).setGridProductLayout(gridProductModelList);
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder{

        //private ConstraintLayout container;
        private GridView gridView;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            //container = itemView.findViewById(R.id.container);
            gridView = itemView.findViewById(R.id.grid_product_layout_gridview);
        }

        private void setGridProductLayout(List<GridProductModel> gridProductModelList){
            //container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            //Glide.with(itemView.getContext()).load(gridProductModelList.get(x).getProductImage()).apply(new RequestOptions()).placeholder(R.drawable.home_blue).into(productImage);
            gridView.setAdapter(new GridProductLayoutAdapter(gridProductModelList));


        }
    }
}
