package com.example.hmwl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager ProductImagesViewPager;

    private ViewPager ProductDetailsViewPager;
    private TabLayout ProductDetailsTabLayout;

    private Button buyNowBtn;

    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton addToWishlistBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProductImagesViewPager = findViewById(R.id.product_image_viewpager);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        ProductDetailsViewPager = findViewById(R.id.product_details_vp);
        ProductDetailsTabLayout = findViewById(R.id.product_details_tl);
        buyNowBtn = findViewById(R.id.buy_now_btn);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.drawable.blueyellowbangles);

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
        ProductImagesViewPager.setAdapter(productImagesAdapter);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ALREADY_ADDED_TO_WISHLIST){
                    ALREADY_ADDED_TO_WISHLIST = false;
                    addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                }else{
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                }
            }
        });

        ProductDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),ProductDetailsTabLayout.getTabCount()));
        ProductDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(ProductDetailsTabLayout));
        ProductDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ProductDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            finish();
            return true;
        }else if(id == R.id.main_search_icon){
            //todo: search
            return true;
        }else if(id == R.id.mai_cart_icon){
            //todo: cart
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}