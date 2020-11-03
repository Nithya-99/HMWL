package com.example.hmwl;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyOrderFragment extends Fragment {

    private RecyclerView myOrdersRecyclerView;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.bluegoldenbangles,"Blue bangles","Delivered on Saturday-31st Oct-2020", "D-4, XYZ colony, Navi Mumbai"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.pinkearrings,"Pink earrings","Delivered on Monday-26th Oct-2020", "D-4, XYZ colony, Navi Mumbai"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.redearrings,"Red earrings","Delivered on Saturday-21st Oct-2020", "D-4, XYZ colony, Navi Mumbai"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.necklaceset,"Necklace set","Delivered on Saturday-21st Oct-2020", "D-4, XYZ colony, Navi Mumbai"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.greenearrings,"Green earrings","Cancelled", "D-4, XYZ colony, Navi Mumbai"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }

}