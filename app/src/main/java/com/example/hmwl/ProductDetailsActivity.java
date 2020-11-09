package com.example.hmwl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager ProductImagesViewPager;
    private TextView productTitle, productPrice, tvCodIndicator;
    private ImageView codIndicator;

    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager ProductDetailsViewPager;
    private TabLayout ProductDetailsTabLayout;

    private String productDescription;
    //private Integer tabPosition = -1;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    private Button buyNowBtn;

    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton addToWishlistBtn;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProductImagesViewPager = findViewById(R.id.product_image_viewpager);
        productTitle = findViewById(R.id.product_title);
        productPrice = findViewById(R.id.product_price);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        tvCodIndicator = findViewById(R.id.cod_indicator_textview);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);

        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        ProductDetailsViewPager = findViewById(R.id.product_details_vp);
        ProductDetailsTabLayout = findViewById(R.id.product_details_tl);
        buyNowBtn = findViewById(R.id.buy_now_btn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();

        firebaseFirestore.collection("PRODUCTS").document("llKs9hrpXdUyZWhS0Ggv").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            for(long x = 1; x < (long)documentSnapshot.get("no_of_product_images") + 1; x++){
                                productImages.add(documentSnapshot.get("product_image_"+x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                            ProductImagesViewPager.setAdapter(productImagesAdapter);
                            productTitle.setText(documentSnapshot.get("product_title").toString());
                            productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                            if((boolean)documentSnapshot.get("COD")){
                                codIndicator.setVisibility(View.VISIBLE);
                                tvCodIndicator.setVisibility(View.VISIBLE);
                            }
                            else {
                                codIndicator.setVisibility(View.INVISIBLE);
                                tvCodIndicator.setVisibility(View.INVISIBLE);
                            }
                            if((boolean)documentSnapshot.get("use_tab_layout")){
                                productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                productDescription = documentSnapshot.get("product_description").toString();
                                //ProductSpecificationFragment.productSpecificationModelList
                                for(long x = 1; x < (long)documentSnapshot.get("spec_total_fields")+1; x++){
                                productSpecificationModelList.add(new ProductSpecificationModel(documentSnapshot
                                        .get("spec_field_"+x+"_name").toString(), documentSnapshot.get("spec_field_"+x+"_value").toString()));
                                }
                            }else{

                            }
                            ProductDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),ProductDetailsTabLayout.getTabCount(),productDescription,productSpecificationModelList));
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_LONG).show();
                        }

                    }
                });


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


        ProductDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(ProductDetailsTabLayout));
        ProductDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tabPosition = tab.getPosition();
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