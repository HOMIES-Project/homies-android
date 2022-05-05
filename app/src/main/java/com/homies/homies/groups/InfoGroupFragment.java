package com.homies.homies.groups;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.AddUser;
import com.homies.homies.retrofit.model.DeleteUser;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGroupFragment extends Fragment {

    ListView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup;
    EditText userInput;
    Activity activity;
    EditText et_GroupName, et_detail;
    TextInputLayout ip_groupName,ip_groupDetail;
    ImageButton delete;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editGroup = inflater.inflate(R.layout.fragment_info_group, container, false);

        userList = editGroup.findViewById(R.id.userList);
        btnAddUser = editGroup.findViewById(R.id.btnAddUser);
        btnCancelActionGroup = editGroup.findViewById(R.id.btnCancelActionGroup);
        btnConfirmDeleteGroup = editGroup.findViewById(R.id.btnConfirmDeleteGroup);
        activity = getActivity();

        et_GroupName = editGroup.findViewById(R.id.et_GroupName);
        et_detail = editGroup.findViewById(R.id.et_detail);

        ip_groupName = editGroup.findViewById(R.id.ip_groupName);
        ip_groupDetail = editGroup.findViewById(R.id.ip_groupDetail);
        btnDeleteGroup = editGroup.findViewById(R.id.btnDeleteGroup);

        delete = editGroup.findViewById(R.id.delete);


        groupInfo();

        btnAddUser.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_add_user,
                            editGroup.findViewById(R.id.addUserContainer)
                    );
            userInput = bottomSheetView.findViewById(R.id.userInput);
            btnCancelAction = bottomSheetView.findViewById(R.id.btnCancelAction);
            btnConfirmUser = bottomSheetView.findViewById(R.id.btnConfirmUser);
            btnConfirmUser.setOnClickListener(view1 -> {
                if (userInput.getText().toString().trim().isEmpty()) {
                    String message = getString(R.string.val_user);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {

                    addUserGroup(createRequest());
                    bottomSheetDialog.dismiss();
                }
            });
            btnCancelAction.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });


        return editGroup;
    }

    public void groupInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupInfo("Bearer " + retrivedToken,idGroup);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse adslist= response.body();

                    String user = adslist.getGroupName();
                    String detail = adslist.getGroupRelationName();
                    String photoString = adslist.getUserAdmin().getPhoto();

                    /*if(photoString != null){
                        byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        userList. imageView4.setImageBitmap(decodedByte);
                    }*/

                    et_GroupName.setText(user);
                    et_detail.setText(detail);

                    List<UserData>  myUserList = response.body().getUserData();
                    String[] oneGroup = new String[myUserList.size()];

                    for (int i = 0; i < myUserList.size(); i++) {
                        oneGroup[i] = myUserList.get(i).getUser().getLogin();

                    }

                    userList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item,R.id.textViewGroup , oneGroup));
                    userList.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);

                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                    getActivity(), R.style.BottonSheetDialogTheme
                            );
                            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                                    .inflate(
                                            R.layout.dialog_delete_user,
                                            getActivity().findViewById(R.id.groupDeleteContainer)
                                    );
                            btnCancelActionGroup = bottomSheetView.findViewById(R.id.btnCancelActionGroup);
                            btnConfirmDeleteGroup = bottomSheetView.findViewById(R.id.btnConfirmDeleteGroup);
                            btnConfirmDeleteGroup.setOnClickListener(view1 -> {

                                deleteUser();
                                bottomSheetDialog.dismiss();

                            });
                            btnCancelActionGroup.setOnClickListener(view1 -> {

                                bottomSheetDialog.dismiss();
                            });



                            bottomSheetDialog.setContentView(bottomSheetView);
                            bottomSheetDialog.show();
                        }
                    });

                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            }
        });

    }

    //Method to obtain the logged in user.
    public AddUser createRequest() {
        AddUser addUser = new AddUser();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        addUser.setLogin(userInput.getText().toString());
        addUser.setIdGroup(idGroup);
        addUser.setIdAdminGroup(userId);

        return addUser;
    }

    public void addUserGroup(AddUser addUser) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> userResponseCall = NetworkConfig.getService().addUserGroup("Bearer " + retrivedToken,addUser);
        userResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.groupSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupInfo();
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public DeleteUser deleteRequest() {
        DeleteUser deleteUser = new DeleteUser();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        deleteUser.setLogin(userInput.getText().toString());
        deleteUser.setIdGroup(idGroup);
        deleteUser.setIdAdminGroup(userId);

        return deleteUser;
    }


    public void deleteUser() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> userResponseCall = NetworkConfig.getService().deleteUserGroup("Bearer " + retrivedToken,deleteRequest().getIdAdminGroup());
        userResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.groupSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupInfo();

                    GroupResponse adslist= response.body();

                    String user = adslist.getGroupName();
                    String detail = adslist.getGroupRelationName();


                    List<UserData>  myUserList = response.body().getUserData();
                    String[] oneGroup = new String[myUserList.size()];

                    for (int i = 0; i < myUserList.size(); i++) {
                        oneGroup[i] = myUserList.get(i).getUser().getLogin();

                    }


                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*public void groupInfoUserAdmin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<AddUserGroupResponse> userInfo = ApiClient.getService().groupInfoUserAdmin("Bearer " + retrivedToken, user().getId());
        userInfo.enqueue(new Callback<AddUserGroupResponse>() {
            @Override
            public void onResponse(Call<AddUserGroupResponse> call, Response<AddUserGroupResponse> response) {
                response.body().getUserAdmin().getId();

            }

            @Override
            public void onFailure(Call<AddUserGroupResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Ha fallado la petición", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}