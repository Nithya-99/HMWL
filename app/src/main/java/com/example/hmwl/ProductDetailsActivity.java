package com.example.hmwl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hmwl.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_cart_query = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static String productID, product_price;
    private ViewPager ProductImagesViewPager;
    private TextView productTitle, productPrice, tvCodIndicator;
    private ImageView codIndicator;
    public static Activity productDettailsActivity;

    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager ProductDetailsViewPager;
    private TabLayout ProductDetailsTabLayout;

    private String productDescription;
    //private Integer tabPosition = -1;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;

    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private FloatingActionButton addToWishlistBtn;

    private FirebaseFirestore firebaseFirestore;

    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private Dialog loadingDialog;
    private DocumentSnapshot documentSnapshot;

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
        addToCartBtn = findViewById(R.id.add_to_cart_btn);

//        loadingDialog = new Dialog(ProductDetailsActivity.this);
//        loadingDialog.setContentView(R.layout.loading_progress_dialog);
//        loadingDialog.setCancelable(false);
//        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
//        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        loadingDialog.show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();

                            FirebaseFirestore.getInstance().collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {


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
                                                ProductDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),
                                                        ProductDetailsTabLayout.getTabCount(),productDescription,productSpecificationModelList));

                                                if (currentUser != null){
                                                    if (DBqueries.cartList.size() == 0){
                                                        DBqueries.loadCartList(ProductDetailsActivity.this, false, new TextView(ProductDetailsActivity.this));
                                                    }
                                                }

                                                if (DBqueries.cartList.contains(productID)){
                                                    ALREADY_ADDED_TO_CART = true;
                                                } else {
                                                    ALREADY_ADDED_TO_CART = false;
                                                }



                                                addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if(currentUser == null){
                                                            signInDialog.show();
                                                        }else {
                                                            if (!running_cart_query) {
                                                                running_cart_query = true;
                                                                if (ALREADY_ADDED_TO_CART) {
                                                                    running_cart_query = false;
                                                                    Toast.makeText(ProductDetailsActivity.this, "Already added to Cart", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Map<String, Object> addProduct = new HashMap<>();
                                                                    addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                                    addProduct.put("list_size", (long) DBqueries.cartList.size() + 1);

                                                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                            .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {

                                                                                if (DBqueries.cartItemModelList.size() != 0) {
                                                                                    DBqueries.cartItemModelList.add(0,new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                                            , documentSnapshot.get("product_title").toString()
                                                                                            , documentSnapshot.get("product_price").toString()
                                                                                            , (Integer) 1
                                                                                            ,(long) documentSnapshot.get("max-quantity")));
                                                                                }

                                                                                ALREADY_ADDED_TO_CART = true;
                                                                                DBqueries.cartList.add(productID);
                                                                                Toast.makeText(ProductDetailsActivity.this, "Product added to cart successfully", Toast.LENGTH_SHORT).show();
                                                                                running_cart_query = false;
                                                                            }else {
                                                                                running_cart_query = false;
                                                                                String error = task.getException().getMessage();
                                                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                            else{
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

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

//                    firebaseFirestore.collection("USERS")
//                            .document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
//                            .set().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//
//                            }else{
//                                String error = task.getException().getMessage();
//                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

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
                if(currentUser == null){
                    signInDialog.show();
                }else{
                    DeliveryActivity.fromCart = false;
                    DeliveryActivity.cartItemModelList.clear();
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DBqueries.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , documentSnapshot.get("product_price").toString()
                            , (Integer) 1
                            ,(long) documentSnapshot.get("max-quantity")));

                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetailsActivity.this,loadingDialog, true);
                    }
                    else{
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }

                    productDettailsActivity = ProductDetailsActivity.this;
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

//        addToCartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(currentUser == null){
//                    signInDialog.show();
//                }else {
//                    if (!running_cart_query) {
//                        running_cart_query = true;
//                        if (ALREADY_ADDED_TO_CART) {
//                            running_cart_query = false;
//                            Toast.makeText(ProductDetailsActivity.this, "Already added to Cart", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Map<String, Object> addProduct = new HashMap<>();
//                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
//                            addProduct.put("product_price_" + String.valueOf(DBqueries.cartList.size()), product_price);
//                            addProduct.put("list_size", (long) DBqueries.cartList.size() + 1);
//
//                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
//                                    .document("MY_CART")
//                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//
//                                        if (DBqueries.cartItemModelList.size() != 0) {
//                                            DBqueries.cartItemModelList.add(0,new CartItemModel(CartItemModel.CART_ITEM, productID,
//                                                    documentSnapshot.get("product_image_1").toString()
//                                                    , documentSnapshot.get("product_title").toString()
//                                                    , documentSnapshot.get("product_price").toString()
//                                                    , (Integer) 1
//                                                    ,(long) documentSnapshot.get("max-quantity")));
//                                        }
//
//                                        ALREADY_ADDED_TO_CART = true;
//                                        DBqueries.cartList.add(productID);
//                                        Toast.makeText(ProductDetailsActivity.this, "Product added to cart successfully", Toast.LENGTH_SHORT).show();
//                                        running_cart_query = false;
//                                    }else {
//                                        running_cart_query = false;
//                                        String error = task.getException().getMessage();
//                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                        }
//                    }
//                }
//            }
//        });

        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(ProductDetailsActivity.this,RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null){
            if (DBqueries.cartList.size() == 0){
                DBqueries.loadCartList(ProductDetailsActivity.this, false, new TextView(ProductDetailsActivity.this));
            }
        }

        if (DBqueries.cartList.contains(productID)){
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

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
            productDettailsActivity  = null;
            finish();
            return true;
        }
//        else if(id == R.id.main_search_icon){
//            Intent searchIntent = new Intent(this, SearchActivity.class);
//            startActivity(searchIntent);
//            return true;
//        }
        else if(id == R.id.mai_cart_icon){

            if(currentUser == null){
                signInDialog.show();
            }
            else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this,MainActivity.class);
                MainActivity.showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDettailsActivity = null;
        super.onBackPressed();
    }
}