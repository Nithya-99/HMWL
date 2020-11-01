package com.example.hmwl;

import java.util.List;

public class HomePageModel {
    public static final int GRID_PRODUCT_VIEW = 0;
    private int type;
    private String title;
    private List<GridProductModel> gridProductModelList;

    public HomePageModel(int type, String title, List<GridProductModel> gridProductModelList) {
        this.type = type;
        this.title = title;
        this.gridProductModelList = gridProductModelList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GridProductModel> getGridProductModelList() {
        return gridProductModelList;
    }

    public void setGridProductModelList(List<GridProductModel> gridProductModelList) {
        this.gridProductModelList = gridProductModelList;
    }
}
