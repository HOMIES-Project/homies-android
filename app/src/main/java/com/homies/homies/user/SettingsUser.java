package com.homies.homies.user;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.groups.MenuActivity;
import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.user.ChangePass;
import com.homies.homies.retrofit.model.user.UserData;
import com.homies.homies.retrofit.model.user.UserRequest;
import com.homies.homies.retrofit.model.user.UserResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsUser extends Fragment {


    ImageView imageView3;
    Button btn_delete, btnConfirm,btn_change,btnAdd,btnCancel;
    Activity activity;
    EditText et_user,et_name, et_lastname, et_email,et_phone,et_birthday,et_password,et_newpassword;
    TextInputLayout ip_user, ip_name, ip_lastname, ip_email,ip_phone,ip_birthday,ip_password,ip_newpassword;
    Button upload, btn_save;
    ProgressBar progressBar;
    boolean condition = true;
    private Bitmap bitmap;
    DatePickerDialog datePickerDialog;

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private final int PICK_IMAGE_FROM_GALLERY_REQUEST =1;
    private final int IMG_REQUEST =21;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settings = inflater.inflate(R.layout.fragment_settings_user, container, false);

        ip_user = settings.findViewById(R.id.ip_user);
        ip_name = settings.findViewById(R.id.ip_name);
        ip_lastname = settings.findViewById(R.id.ip_lastname);
        ip_email = settings.findViewById(R.id.ip_email);
        ip_phone = settings.findViewById(R.id.ip_phone);
        ip_birthday = settings.findViewById(R.id.ip_birthday);

        imageView3 = settings.findViewById(R.id.imageView3);

        btn_delete = settings.findViewById(R.id.btn_delete);
        btn_change = settings.findViewById(R.id.btn_change);
        btn_save = settings.findViewById(R.id.btn_save);
        upload = settings.findViewById(R.id.upload);
        activity = getActivity();
        et_user = settings.findViewById(R.id.et_user);
        et_name = settings.findViewById(R.id.et_name);
        et_lastname = settings.findViewById(R.id.et_lastname);
        et_email = settings.findViewById(R.id.et_email);
        et_phone = settings.findViewById(R.id.et_phone);
        et_birthday = settings.findViewById(R.id.et_birthday);

        progressBar = settings.findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);

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
        //button save changes
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition = true;
                validateClickFields();
                if (condition) {
                    updateInfo(userInf());
                    //uploadPhoto();
                }
            }
        });
        //Button delete user
        btn_delete.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_delete_profile,
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

        btn_change.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_change_pass,
                            settings.findViewById(R.id.changePass)
                    );


            et_password = bottomSheetView.findViewById(R.id.et_password);
            et_newpassword = bottomSheetView.findViewById(R.id.et_newpassword);
            btnAdd = bottomSheetView.findViewById(R.id.btnAdd);
            btnCancel = bottomSheetView.findViewById(R.id.btnCancel);
            btnAdd.setOnClickListener(view1 -> {

                changePass(changeRequest());


                bottomSheetDialog.dismiss();

            });
            btnCancel.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                Date date = new Date(year-1900, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String cdate = formatter.format(date);
                                et_birthday.setText(cdate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        return settings;
    }


    public UserData userInf() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        userData.setId(userId);

        return userData;
    }
    //Method to delete user
    public void deleteUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserResponse> deleteRequest = NetworkConfig.getService().deleteUser("Bearer " + retrivedToken, userInf().getId());
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
    //Method to obtain user data
    public void userInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserData> userInfo = NetworkConfig.getService().userInfo("Bearer " + retrivedToken, userInf().getId());
        userInfo.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    UserData adslist= response.body();

                    String user = adslist.getUser().getLogin();
                    String name = adslist.getUser().getFirstName();
                    String lastName = adslist.getUser().getLastName();
                    String email = adslist.getUser().getEmail();
                    String photoString = adslist.getPhoto();
                    String phone = adslist.getPhone();
                    String birthday = adslist.getBirthDate();

                    if(photoString != null){
                        byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView3.setImageBitmap(decodedByte);
                    }

                    et_user.setText(user);
                    et_name.setText(name);
                    et_lastname.setText(lastName);
                    et_email.setText(email);
                    et_phone.setText(phone);
                    et_birthday.setText(birthday);

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
    //Method for updating user data
    public void updateInfo(UserData userData) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        userData.setUser(new UserRequest());

        userData.getUser().setFirstName(et_name.getText().toString());
        userData.getUser().setLastName(et_lastname.getText().toString());
        userData.getUser().setLogin(et_user.getText().toString());
        userData.getUser().setEmail(et_email.getText().toString());
        userData.getUser().setLangKey("en");
        userData.getUser().setPhone(et_phone.getText().toString());
        userData.getUser().setBirthDate(et_birthday.getText().toString());

        try {
            BitmapDrawable drawable = (BitmapDrawable) imageView3.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);


            userData.getUser().setPhoto(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<UserData> updateInfo = NetworkConfig.getService().updateInfo("Bearer " + retrivedToken, userInf().getId(), userData.getUser());
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

        //region validationPhone

        et_phone.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneInput = et_phone.getText().toString().trim();
                if (phoneInput.isEmpty()) {
                    ip_phone.setError(getString(R.string.fieldEmpty));
                    condition = false;
                }
                if (phoneInput.length() < 8) {
                    ip_phone.setError(getString(R.string.val_email));
                    condition = false;
                } else if (!Patterns.PHONE.matcher(phoneInput).matches()) {
                    ip_phone.setError(getString(R.string.fieldEmail));
                    condition = false;
                } else {
                    ip_phone.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion

        //region validationUser

        et_user.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_user.getText().toString().trim().length() < 4) {
                    ip_user.setError(getString(R.string.val_four_character));
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
            ip_user.setError(getString(R.string.val_four_character));
            condition = false;
        } else {
            ip_user.setErrorEnabled(false);
        }

        String phoneInput = et_phone.getText().toString().trim();
        if (phoneInput.length() < 8) {
            ip_phone.setError(getString(R.string.val_email));
            condition = false;
        }  else {
            ip_phone.setErrorEnabled(false);
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


    public ChangePass changeRequest() {
        ChangePass changePass = new ChangePass();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        changePass.setCurrentPassword(et_password.getText().toString());
        changePass.setNewPassword(et_newpassword.getText().toString());

        return changePass;
    }

    //Method to change pass
    public void changePass(ChangePass changePass) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Call<Void> deleteRequest = NetworkConfig.getService().changePassword("Bearer " + retrivedToken,changeRequest());
        deleteRequest.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    if (et_newpassword.length() < 8) {
                        ip_password.setError(getString(R.string.val_passMin));
                        condition = false;
                        String message = getString(R.string.val_passMin);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    }else {
                        String message = getString(R.string.changeSucess);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setReorderingAllowed(true);

                        transaction.replace(R.id.fragmentGroup, SettingsUser.class, null);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }


                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
                imageView3.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}