package com.example.hmwl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static com.example.hmwl.DBqueries.lists;
import static com.example.hmwl.DBqueries.loadFragmentData;
import static com.example.hmwl.DBqueries.loadedCategoriesName;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private HomePageAdapter adapter;

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

//        List<GridProductModel> gridProductModelList = new ArrayList<GridProductModel>();
//        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Red Bangles","Rs.650/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Pink Pearl Jhumka","Rs.450/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Blue Jhumka", "Rs.550/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Pink Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Black Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Blue-Yellow Bangles", "Rs.650/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Green Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Orange Jhumka","Rs.450/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Green Jhumka","Rs.200/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Red Bangles","Rs.650/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Pink Pearl Jhumka","Rs.450/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Blue Jhumka", "Rs.550/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Pink Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Black Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Blue-Yellow Bangles", "Rs.650/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Green Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Orange Jhumka","Rs.450/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Green Jhumka","Rs.200/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.redbangles,"Red Bangles","Rs.650/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.pinkpearlearrings,"Pink Pearl Jhumka","Rs.450/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blueearrings,"Blue Jhumka", "Rs.550/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.pinkjewelleryset2,"Pink Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blackjewelleryset,"Black Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.blueyellowbangles,"Blue-Yellow Bangles", "Rs.650/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.greenjewelleryset,"Green Jewellery Set","Rs.700/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.orangeearrings,"Orange Jhumka","Rs.450/-"));
//        gridProductModelList.add(new GridProductModel(R.drawable.greenearrings,"Green Jhumka","Rs.200/-"));

//        GridView gridView = view.findViewById(R.id.grid_product_layout_gridview);
//        gridView.setAdapter(new GridProductLayoutAdapter(gridProductModelList));

        //testing = view.findViewById(R.id.hom_page_rv);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        int listPosition = 0;
        for (int x = 0; x < loadedCategoriesName.size(); x++){
            if(loadedCategoriesName.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if(listPosition == 0){
            loadedCategoriesName.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            adapter = new HomePageAdapter(lists.get(loadedCategoriesName.size() - 1));
            loadFragmentData(adapter,this,loadedCategoriesName.size() - 1, title);
        }else {
            adapter = new HomePageAdapter(lists.get(listPosition));
        }

        //List<HomePageModel> homePageModelList = new ArrayList<>();
        //homePageModelList.add(new HomePageModel(0,"Title", gridProductModelList));
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
//        if (id == R.id.main_search_icon) {
//            Intent searchIntent = new Intent(this, SearchActivity.class);
//            startActivity(searchIntent);
//            return true;
//        }else
        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}