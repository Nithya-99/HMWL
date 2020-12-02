package com.example.hmwl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.gson.JsonObject;
//import com.paytm.pgsdk.PaytmOrder;
//import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    //private Toolbar toolbar;
    private RecyclerView deliveryRecyclerView;
    private Button changeOrAddNewAddressBtn;
    private TextView totalAmount;
    public String Totalamt;
    public int Total;
    public static List<CartItemModel> cartItemModelList;
    private TextView fullname, fullAddress, pincode;
    private Button continueBtn;
    private String name, mobileNo;
    private Dialog paymentMethodDialog;
    private Dialog loadingDialog;
    ImageButton paytm, cod;
    public CartAdapter cartAdapter;
    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;
    private TextView orderId;


    public static final int SELECT_ADDRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ye apan icon show krne ke liye krte h.......
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");


        deliveryRecyclerView = findViewById(R.id.delivery_recyclerview);
        totalAmount = findViewById(R.id.total_cart_amount);

        changeOrAddNewAddressBtn = findViewById(R.id.change_or_add_Address_btn);

        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.cart_continue_btn);


//        loadingDialog = new Dialog(DeliveryActivity.this);
//        loadingDialog.setContentView(R.layout.loading_progress_dialog);
//        loadingDialog.setCancelable(false);
//        loadingDialog.getWindow().setBackgroundDrawable(DeliveryActivity.this.getDrawable(R.drawable.slider_background));
//        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(DeliveryActivity.this.getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm = paymentMethodDialog.findViewById(R.id.paytm);
        cod = paymentMethodDialog.findViewById(R.id.cod_btn);
        orderConfirmationLayout = findViewById(R.id.order_conformation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(DBqueries.cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOrAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressesIntent = new Intent(DeliveryActivity.this,MyAdressesActivity.class);
                myAddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.show();
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
//                Intent otpIntent = new Intent(DeliveryActivity.this,.class);
//                otpIntent.putExtra("mobileNo",mobileNo.substring(0,10));
//                startActivity(otpIntent);
                orderConfirmationLayout.setVisibility(View.VISIBLE);
                continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });
        Checkout.preload(getApplicationContext());

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
//                loadingDialog.show();
                if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }
                Toast.makeText(DeliveryActivity.this, "Starting Payment", Toast.LENGTH_SHORT).show();
                startPayment();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        fullname.setText(name + " - " + mobileNo);
//        if(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")){
//            fullname.setText(name + " - " + mobileNo);
//        }
//        else{
//            fullname.setText(name + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
//        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();

        if(landmark.equals("")){
            fullAddress.setText(flatNo +" "+ locality +" " + city +" " + state);
        } else{
            fullAddress.setText(flatNo +" "+ locality +" " + landmark +" " + city +" " + state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        if (MainActivity.mainActivity != null){
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        }
        if (ProductDetailsActivity.productDettailsActivity != null){
            ProductDetailsActivity.productDettailsActivity.finish();
            ProductDetailsActivity.productDettailsActivity = null;
        }

        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
//        String O_ID = paymentData.getOrderId();
        orderId.setText("Order ID: "+"15246"+mobileNo);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    ////Start Payment

    public void startPayment() {
        Toast.makeText(this, "Payment Started", Toast.LENGTH_SHORT).show();

        Totalamt = totalAmount.getText().toString().substring(3,totalAmount.getText().length()-2);
        Total = Integer.parseInt(Totalamt)*100;

        Checkout checkout = new Checkout();
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("currency", "INR");
            options.put("amount", Total);//pass amount in currency subunits
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    ////Start Payment

}