package com.homies.homies;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.databinding.ActivityGroupBinding;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserData;
import com.homies.homies.services.UserRequest;
import com.homies.homies.services.UserResponse;
import com.homies.homies.user.LoginFragment;
import com.homies.homies.user.MainActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SettingsUser extends Fragment {


    ImageView imageView3;
    Button btn_delete, btnConfirm;
    Activity activity;
    EditText et_user,et_name, et_lastname, et_email;
    TextInputLayout ip_user, ip_name, ip_lastname, ip_email;
    Button upload, btn_save;
    boolean condition = true;
    private Bitmap bitmap;

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST =1;
    private int IMG_REQUEST =21;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settings = inflater.inflate(R.layout.fragment_settings_user, container, false);




        ip_user = settings.findViewById(R.id.ip_user);
        ip_name = settings.findViewById(R.id.ip_name);
        ip_lastname = settings.findViewById(R.id.ip_lastname);
        ip_email = settings.findViewById(R.id.ip_email);

        imageView3 = settings.findViewById(R.id.imageView3);

        btn_delete = settings.findViewById(R.id.btn_delete);
        btn_save = settings.findViewById(R.id.btn_save);
        upload = settings.findViewById(R.id.upload);
        activity = getActivity();
        et_user = settings.findViewById(R.id.et_user);
        et_name = settings.findViewById(R.id.et_name);
        et_lastname = settings.findViewById(R.id.et_lastname);
        et_email = settings.findViewById(R.id.et_email);

        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Ajustes de Usuario");
        userInfo();
        validateFields();


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST);
        }
        upload = settings.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_REQUEST);


            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition = true;
                validateClickFields();
                if (condition) {
                    updateInfo(userInf());
                    uploadPhoto();
                }
            }
        });



        btn_delete.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_layout_botton_deleteuser,
                            settings.findViewById(R.id.bottonDeleteUser)
                    );

            btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
            btnConfirm.setOnClickListener(view1 -> {

                deleteUser();


                    bottomSheetDialog.dismiss();

            });


            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return settings;
    }


   /* protected void OnActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            uploadPhoto(uri);
        }
    }*/



    public UserData userInf() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        userData.setId(userId);


        return userData;
    }

    public void deleteUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserResponse> deleteRequest = ApiClient.getService().deleteUser("Bearer " + retrivedToken, userInf().getId());
        deleteRequest.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.deleteSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();

                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void userInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserData> userInfo = ApiClient.getService().userInfo("Bearer " + retrivedToken, userInf().getId());
        userInfo.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    UserData adslist= response.body();


                    String user = adslist.getUser().getLogin();
                    String name = adslist.getUser().getFirstName();
                    String lastName = adslist.getUser().getLastName();
                    String email = adslist.getUser().getEmail();

                    et_user.setText(user);
                    et_name.setText(name);
                    et_lastname.setText(lastName);
                    et_email.setText(email);



                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void updateInfo(UserData userData) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        userData.setUser(new UserRequest());

        userData.getUser().setFirstName(et_name.getText().toString());
        userData.getUser().setLastName(et_lastname.getText().toString());
        userData.getUser().setLogin(et_user.getText().toString());
        userData.getUser().setEmail(et_email.getText().toString());
        userData.getUser().setLangKey("en");
        userData.getUser().setPhone(null);
        userData.getUser().setPhoto(null);

        Call<UserData> updateInfo = ApiClient.getService().updateInfo("Bearer " + retrivedToken, userInf().getId(), userData.getUser());
        updateInfo.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {


                    startActivity(new Intent(activity, MenuActivity.class));
                    activity.finish();

                    String message = getString(R.string.updateInfo);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();



                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            }
        });

    }


    // Validations when interacting with form fields
    private void validateFields() {
        //region validationUser

        et_user.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_user.getText().toString().trim().length() < 4) {
                    ip_user.setError(getString(R.string.val_username));
                    condition = false;
                } else {
                    ip_user.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        //region validationEmail
        et_email.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = et_email.getText().toString().trim();
                if (emailInput.isEmpty()) {
                    ip_email.setError(getString(R.string.fieldEmpty));
                    condition = false;
                }
                if (emailInput.length() < 8) {
                    ip_email.setError(getString(R.string.val_email));
                    condition = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    ip_email.setError(getString(R.string.fieldEmail));
                    condition = false;
                } else {
                    ip_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Validations when clicking on the registration button
    private void validateClickFields() {

        if (et_user.getText().toString().trim().length() < 4) {
            ip_user.setError(getString(R.string.val_username));
            condition = false;
        } else {
            ip_user.setErrorEnabled(false);
        }

        String emailInput = et_email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            ip_email.setError(getString(R.string.fieldEmpty));
            condition = false;
        }
        if (emailInput.length() < 8) {
            ip_email.setError(getString(R.string.val_email));
            condition = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            ip_email.setError(getString(R.string.fieldEmail));
            condition = false;
        } else {
            ip_email.setErrorEnabled(false);
        }
    }
    //endregion

   public void uploadPhoto() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream);
        byte [] imageInByte = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

       Call<UserData> uploadPhoto = ApiClient.getService().uploadPhoto("Bearer " + retrivedToken, userInf().getId(),encodedImage);
       uploadPhoto.enqueue(new Callback<UserData>() {
           @Override
           public void onResponse(Call<UserData> call, Response<UserData> response) {
               if (response.isSuccessful()) {


                   startActivity(new Intent(activity, MenuActivity.class));
                   activity.finish();

                   String message = getString(R.string.updateInfo);
                   Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();



               } else {
                   String message = getString(R.string.error_login);
                   Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void onFailure(Call<UserData> call, Throwable t) {
               String message = t.getLocalizedMessage();
               Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

           }
       });



   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView3.setImageBitmap(bitmap);

        }
    }




}