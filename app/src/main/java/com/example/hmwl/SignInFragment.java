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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }
    private TextView register_link, forgot_pw;
    private FrameLayout parentFramelayout;

    private EditText email, password;

    private ImageButton cancelBtn;
    private Button loginBtn;

    private FirebaseAuth firebaseAuth;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn =false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in,container, false);
        register_link = view.findViewById(R.id.register_link);
        parentFramelayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.signin_email);
        password = view.findViewById(R.id.signin_pw);

        forgot_pw = view.findViewById(R.id.signin_forgot_pw);

        loginBtn = view.findViewById(R.id.signin_button);
        cancelBtn = view.findViewById(R.id.signin_cancelBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        if(disableCloseBtn){
            cancelBtn.setVisibility(View.GONE);
        }else {
            cancelBtn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment() );
            }
        });

        forgot_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.onResetPasswordFragment= true;
                setFragment(new ResetPasswordFragment());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
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
            if(!TextUtils.isEmpty(password.getText())){
                loginBtn.setEnabled(true);
                loginBtn.setTextColor(Color.rgb(255,255,255));
            }else{
                loginBtn.setEnabled(false);
                loginBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            loginBtn.setEnabled(false);
            loginBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(password.length() >= 8){
                loginBtn.setEnabled(false);
                loginBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"Login successful", Toast.LENGTH_SHORT).show();
                            if(disableCloseBtn){
                                disableCloseBtn = false;
                            }
                            else {
                                Intent mainPage = new Intent(getActivity(),MainActivity.class);
                                startActivity(mainPage);
                            }
                            getActivity().finish();
                        }else{
                            loginBtn.setEnabled(true);
                            loginBtn.setTextColor(Color.rgb(255,255,255));

                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getActivity(),"Incorrect email or password!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"Incorrect email or password!",Toast.LENGTH_SHORT).show();
        }
    }
}
