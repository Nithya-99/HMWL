package com.example.hmwl;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateInfoFragment extends Fragment {

    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PERMISSION_CODE = 1000;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button changePhotoBtn, removeBtn, updateBtn, doneBtn;
    private EditText nameField, emailField, password;
    private Dialog passwordDialog;
    private String name,email,photo;
    private Uri imageUri;
    private boolean updatePhoto = false;
    private Dialog loadingDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        circleImageView = view.findViewById(R.id.profile_image);
        changePhotoBtn = view.findViewById(R.id.change_photo_btn);
        removeBtn = view.findViewById(R.id.remove_photo_btn);
        updateBtn = view.findViewById(R.id.update);
        nameField = view.findViewById(R.id.name);
        emailField = view.findViewById(R.id.email);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        password = passwordDialog.findViewById(R.id.password);
        doneBtn = passwordDialog.findViewById(R.id.done_btn);

        name = getArguments().getString("Name");
        email = getArguments().getString("Email");
        photo = getArguments().getString("Photo");

        Glide.with(getContext()).load(photo).placeholder(R.drawable.profile_placeholder).into(circleImageView);
        nameField.setText(name);
        emailField.setText(email);

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || getContext()
                            .checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else{
                    openCamera();
                }

//                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent,1);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = null;
                updatePhoto = true;
                Glide.with(getContext()).load(R.drawable.profile_placeholder).into(circleImageView);
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
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

        nameField.addTextChangedListener(new TextWatcher() {
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });

        return view;
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(emailField.getText())){
            if(!TextUtils.isEmpty((nameField.getText()))){
                updateBtn.setEnabled(true);
                updateBtn.setTextColor(Color.rgb(255,255,255));
            } else {
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if(emailField.getText().toString().matches(emailPattern)){
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (emailField.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())){
                //same email
                updatePhoto(user);
            } else{
                //update email
                passwordDialog.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userPassword = password.getText().toString();
                        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        passwordDialog.dismiss();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, userPassword);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            user.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        updatePhoto(user);
                                                    }else {

                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
            }
        }else{
            emailField.setError("Invalid Email!");
        }
    }

    private void updatePhoto(final FirebaseUser user){
        if (updatePhoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + user.getUid() + ".jpg");
            if (imageUri != null){

                Glide.with(getContext()).asBitmap().load(imageUri).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()){
                                                imageUri = task.getResult();
                                                DBqueries.profile = task.getResult().toString();
                                                Glide.with(getContext()).load(DBqueries.profile).into(circleImageView);

                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("Name", nameField.getText().toString().trim());
                                                updateData.put("email", emailField.getText().toString().trim());
                                                updateData.put("profile", DBqueries.profile);

                                                updateFields(user,updateData);

                                            }else {
                                                DBqueries.profile = "";
                                                Glide.with(getContext()).load(R.drawable.profile_placeholder).into(circleImageView);
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.profile_placeholder);
                    }
                });

            }else {
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DBqueries.profile = "";

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("Name", nameField.getText().toString().trim());
                            updateData.put("email", emailField.getText().toString().trim());
                            updateData.put("profile", "");

                            updateFields(user,updateData);
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("email", emailField.getText().toString());

            updateFields(user, updateData);
        }
    }

    private void updateFields(FirebaseUser user, Map<String, Object> updateData){
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if(updateData.size() > 1){
                        DBqueries.fullname = nameField.getText().toString().trim();
                        DBqueries.email = emailField.getText().toString().trim();
                    }else {
                        DBqueries.fullname = nameField.getText().toString().trim();
                    }
                    //getActivity().finish();
                    Toast.makeText(getContext(),"Successfully Updated!", Toast.LENGTH_LONG).show();
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK){
            circleImageView.setImageURI(imageUri);
            updatePhoto = true;
        }
//        if (requestCode == 1){
//            if (resultCode == getActivity().RESULT_OK){
//                if(data != null){
//                    imageUri = data.getData();
//                    updatePhoto = true;
//                    circleImageView.setImageURI(imageUri);
//                    Glide.with(getContext()).load(imageUri).into(circleImageView);
//                } else {
//                    Toast.makeText(getContext(),"Image not found!",Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2 || PERMISSION_CODE == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                openCamera();
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent,1);
            }else{
                Toast.makeText(getContext(),"Permission Denied!",Toast.LENGTH_LONG).show();
            }
        }
    }
}