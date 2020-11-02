package com.example.hmwl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryRecyclerView = findViewById(R.id.category_rv);

        List<GridProductModel> gridProductModelList = new ArrayList<GridProductModel>();
        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Red Bangles","Rs.650/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Pink Pearl Jhumka","Rs.450/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Blue Jhumka", "Rs.550/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Pink Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Black Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Blue-Yellow Bangles", "Rs.650/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Green Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Orange Jhumka","Rs.450/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Green Jhumka","Rs.200/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Red Bangles","Rs.650/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Pink Pearl Jhumka","Rs.450/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Blue Jhumka", "Rs.550/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Pink Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Black Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Blue-Yellow Bangles", "Rs.650/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Green Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Orange Jhumka","Rs.450/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Green Jhumka","Rs.200/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Red Bangles","Rs.650/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Pink Pearl Jhumka","Rs.450/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Blue Jhumka", "Rs.550/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Pink Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Black Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Blue-Yellow Bangles", "Rs.650/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Green Jewellery Set","Rs.700/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Orange Jhumka","Rs.450/-"));
        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Green Jhumka","Rs.200/-"));

//        GridView gridView = view.findViewById(R.id.grid_product_layout_gridview);
//        gridView.setAdapter(new GridProductLayoutAdapter(gridProductModelList));

        //testing = view.findViewById(R.id.hom_page_rv);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(0,"Title", gridProductModelList));

        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_search_icon) {
            //todo: search
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}