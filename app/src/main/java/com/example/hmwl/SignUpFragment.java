package com.example.hmwl;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }
    private TextView login_link;
    private FrameLayout parentFramelayout;
    private EditText email, name, password, confirm_password;
    private ImageButton closeBtn;
    private Button signupBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        login_link = view.findViewById(R.id.login_link);
        parentFramelayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.signup_email);
        name = view.findViewById(R.id.signup_name);
        password = view.findViewById(R.id.signup_pw);
        confirm_password = view.findViewById(R.id.signup_cpw);

        closeBtn = view.findViewById(R.id.signup_cancelBtn);
        signupBtn = view.findViewById(R.id.signup_button);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainPage = new Intent(getActivity(),MainActivity.class);
                startActivity(mainPage);
                getActivity().finish();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFramelayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty((name.getText()))){
                if(!TextUtils.isEmpty(password.getText())){
                    if(!TextUtils.isEmpty(confirm_password.getText()) && password.length() >= 8){
                        signupBtn.setEnabled(true);
                        signupBtn.setTextColor(Color.rgb(255,255,255));
                    }else {
                        signupBtn.setEnabled(false);
                        signupBtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else {
                    signupBtn.setEnabled(false);
                    signupBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else {
                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
            signupBtn.setEnabled(false);
            signupBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(password.getText().toString().equals(confirm_password.getText().toString())){

                signupBtn.setEnabled(false);
                signupBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            CollectionReference userdatareference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                            Map<Object,String> userdata = new HashMap<>();
                            userdata.put("Name", name.getText().toString());
                            userdata.put("email",email.getText().toString());
                            userdata.put("profile","");

                            Map<String,Object> cartMap = new HashMap<>();
                            cartMap.put("list_size", (long) 0);

                            Map<String,Object> myAddressesMap = new HashMap<>();
                            myAddressesMap.put("list_size", (long) 0);

                            final List<String> documentNames = new ArrayList<>();
                            documentNames.add("MY_CART");
                            documentNames.add("MY_ADDRESSES");

                            List<Map<String,Object>> documentFields = new ArrayList<>();
                            documentFields.add(cartMap);
                            documentFields.add(myAddressesMap);

                            for(int x=0; x< documentNames.size(); x++){
                                final int finalX = x;
                                userdatareference.document(documentNames.get(x)).set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            if(finalX == documentNames.size()-1){
                                                mainIntent();
                                            }
                                        }
                                        else {
                                            signupBtn.setEnabled(true);
                                            signupBtn.setTextColor(Color.rgb(255, 255, 255));
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Map<Object,Long> listSize = new HashMap<>();
                                        listSize.put("Name", (long) 0);
                                        CollectionReference userdatareference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");
                                        Toast.makeText(getActivity(), "Signup successful", Toast.LENGTH_SHORT).show();
                                        if(disableCloseBtn){
                                            disableCloseBtn = false;
                                        }
                                        else {
                                            Intent mainPage = new Intent(getActivity(),MainActivity.class);
                                            startActivity(mainPage);
                                        }
                                        getActivity().finish();

                                    }else{
                                        signupBtn.setEnabled(true);
                                        signupBtn.setTextColor(Color.rgb(255,255,255));

                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            signupBtn.setEnabled(true);
                            signupBtn.setTextColor(Color.rgb(255,255,255));

                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                confirm_password.setError("Password doesn't match!");
            }
        }else{
            email.setError("Invalid Email!");
        }
    }

    private void mainIntent(){

        if(disableCloseBtn){
            disableCloseBtn = false;
        }
        else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
            getActivity().finish();
        }
    }
}