package com.example.hmwl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MyAdressesActivity extends AppCompatActivity {

    private RecyclerView myAddressesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Rachael Green", "XYZ colony, Mumbai", "400012"));
        addressesModelList.add(new AddressesModel("Monica Geller", "ABC colony, Thane", "403013"));
        addressesModelList.add(new AddressesModel("Ross Geller", "PQR colony, Vashi", "402011"));
        addressesModelList.add(new AddressesModel("Joey Tribbiani", "HHH colony, Goregaon", "400012"));
        addressesModelList.add(new AddressesModel("Chandler Bing", "XYZ colony, Kharghar", "410012"));
        addressesModelList.add(new AddressesModel("Phoebe Buffay", "OOO colony, Parel", "402012"));
        addressesModelList.add(new AddressesModel("Emma Green", "MNB colony, Panve;", "400012"));
        addressesModelList.add(new AddressesModel("Ben", "TRY colony, Mumbai", "423012"));


        AddressesAdapter addressesAdapter = new AddressesAdapter(addressesModelList);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        addressesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}