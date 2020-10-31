package com.example.hmwl;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    private EditText registeredEmail;
    private Button resetPasswordBtn;
    private TextView goBack;
    private FrameLayout parentFramelayout;
    private FirebaseAuth firebaseAuth;
    private ImageView redemailicon;
    private ProgressBar progressBar;
    private ViewGroup iconContainer;
    private TextView recoveryText;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        registeredEmail = view.findViewById(R.id.forgotPasswordEmail);
        resetPasswordBtn = view.findViewById(R.id.resetPasswordButton);
        goBack = view.findViewById(R.id.forgotPasswordGoback);
        parentFramelayout = getActivity().findViewById(R.id.register_framelayout);
        firebaseAuth = FirebaseAuth.getInstance();
        iconContainer = view.findViewById(R.id.forgotPassword_linearIconContainer);
        redemailicon = view.findViewById(R.id.redemail);
        recoveryText = view.findViewById(R.id.recoveryText);
        progressBar = view.findViewById(R.id.forgotpasswordprogressBar);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(iconContainer);
                recoveryText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(iconContainer);
                redemailicon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetPasswordBtn.setEnabled(false);
                resetPasswordBtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

                            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, redemailicon.getWidth() / 2, redemailicon.getHeight() / 2);
                            scaleAnimation.setDuration(100);
                            scaleAnimation.setInterpolator(new AccelerateInterpolator());
                            scaleAnimation.setRepeatMode(Animation.REVERSE);
                            scaleAnimation.setRepeatCount(1);

                            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    recoveryText.setText("Recovery email sent successfully ! check your inbox");
                                    recoveryText.setTextColor(getResources().getColor(R.color.successGreen));

                                    TransitionManager.beginDelayedTransition(iconContainer);
                                    recoveryText.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    redemailicon.setImageResource(R.drawable.greenemail);
                                }
                            });

                            redemailicon.startAnimation(scaleAnimation);
                        }
                        else{
                            String error = task.getException().getMessage();

                            TransitionManager.beginDelayedTransition(iconContainer);
                            recoveryText.setText(error);
                            recoveryText.setTextColor(Color.rgb(255,0,0));
                            TransitionManager.beginDelayedTransition(iconContainer);
                            redemailicon.setVisibility(View.VISIBLE);
                            recoveryText.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);

                        resetPasswordBtn.setEnabled(true);
                        resetPasswordBtn.setTextColor(Color.rgb(255,255,255));
                    }
                });
            }
        });

        registeredEmail.addTextChangedListener(new TextWatcher() {
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
    }
    private void checkInputs(){
        if(TextUtils.isEmpty(registeredEmail.getText())){
            resetPasswordBtn.setEnabled(false);
            resetPasswordBtn.setTextColor(Color.argb(50,255,255,255));
        }else{
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setTextColor(Color.rgb(255,255,255));

        }
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFramelayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
