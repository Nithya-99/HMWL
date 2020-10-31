package com.example.hmwl;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
        categoryModelList.add(new CategoryModel("link","Home"));
        categoryModelList.add(new CategoryModel("link","Earrings"));
        categoryModelList.add(new CategoryModel("link","Bangles"));
        categoryModelList.add(new CategoryModel("link","Necklace"));
        categoryModelList.add(new CategoryModel("link","Jewellery set"));

        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        List<GridProductModel> gridProductModelList = new ArrayList<>();
        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Jhumka", "Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Jhumka", "Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Jhumka","Rs.500/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.darkblue,"Jhumka","Rs.500/-"));


        GridView gridView = view.findViewById(R.id.grid_product_layout_gridview);

        gridView.setAdapter(new GridProductLayoutAdapter(gridProductModelList));

        RecyclerView testing = view.findViewById(R.id.testing);
        //LinearLayoutManager testingLayoutManager = new LinearLayoutManager();

        return view;
    }

}
