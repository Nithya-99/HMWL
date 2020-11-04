package com.example.hmwl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {

    public MyCartFragment(){
        //Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.blueearrings,"Earings","Rs.1999/-","Rs.2999/-",2));
        cartItemModelList.add(new CartItemModel(0,R.drawable.blackjewelleryset,"Jewelleryset","Rs.999/-","Rs.1599/-",2));
        cartItemModelList.add(new CartItemModel(0,R.drawable.bluegoldenbangles,"Bangles","Rs.599/-","Rs.1000/-",2));
        cartItemModelList.add(new CartItemModel(1,"Price (3 Items)", "Rs.2500/-","Free","You saved Rs.501/- in this order","2500/-"));
        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        return view;
    }
}