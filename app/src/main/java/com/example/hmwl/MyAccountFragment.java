package com.example.hmwl;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }
    private FloatingActionButton settingsBtn;
    private Button viewAllAddressBtn,signoutBtn;
    public static final int MANAGE_ADDRESS =1;
    private CircleImageView profileView;
    private TextView name,email;
    private LinearLayout layoutContainer;
    private Dialog loadingDialog;
    private TextView addressname,address,pincode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        profileView = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.user_email);
        addressname = view.findViewById(R.id.address_fullname);
        address = view.findViewById(R.id.address);
        pincode = view.findViewById(R.id.address_pincode);
        signoutBtn = view.findViewById(R.id.sign_out_btn);
        settingsBtn = view.findViewById(R.id.settings_btn);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        loadingDialog.show();

        layoutContainer = view.findViewById(R.id.layout_container);

        if(DBqueries.addressesModelList.size()==0){
            addressname.setText("No Address");
            address.setText("-");
            pincode.setText("-");
        }else {
//            String nameText,mobileNo;
//            nameText = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
//            //mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
//            //addressname.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullname());
//            //address.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
//            pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
        }
                //loadingDialog.show();
         loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                loadingDialog.setOnDismissListener(null);
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBqueries.addressesModelList.size() == 0) {
                            addressname.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        } else {
                            setAddress();
                        }
                    }
                });
                DBqueries.loadAddresses(getContext(),loadingDialog,false);
            }
        });

        viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myAddressesIntent = new Intent(getContext() ,MyAdressesActivity.class);
                myAddressesIntent.putExtra("MODE",MANAGE_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateUserInfo = new Intent(getContext(),UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name", name.getText());
                updateUserInfo.putExtra("Email", email.getText());
                updateUserInfo.putExtra("Photo", DBqueries.profile);
                startActivity(updateUserInfo);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);
        if(!DBqueries.profile.equals("")){
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile_placeholder)).into(profileView);
        } else {
            profileView.setImageResource(R.drawable.profile_placeholder);
        }

        //if(loadingDialog.isShowing()){
            if (DBqueries.addressesModelList.size() == 0) {
                addressname.setText("No Address");
                address.setText("-");
                pincode.setText("-");
            } else {
                setAddress();
            }
    }

    private void setAddress() {
        String nametext,mobileNo;
        nametext = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        addressname.setText(nametext + " - " + mobileNo);

//        if(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")){
//            addressname.setText(name + " - " + mobileNo);
//        }
//        else{
//            addressname.setText(name + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
//        }
        
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();

        address.setText(flatNo +" "+ locality +" " + city +" " + state);
//        if(landmark.equals("")){
//            address.setText(flatNo +" "+ locality +" " + city +" " + state);
//        } else{
//            address.setText(flatNo +" "+ locality +" " + landmark +" " + city +" " + state);
//        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
    }
}