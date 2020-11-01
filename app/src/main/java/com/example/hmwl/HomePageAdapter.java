package com.example.hmwl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        private GridView gridView;
        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.grid_product_layout_gridview);
        }
        private void setGridProductLayout(List<GridProductModel> gridProductModelList){
            gridView.setAdapter(new GridProductLayoutAdapter(gridProductModelList));
        }
    }
}
