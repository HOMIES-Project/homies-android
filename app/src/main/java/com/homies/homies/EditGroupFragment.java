package com.homies.homies;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.DeleteUserGroupRequest;
import com.homies.homies.services.DeleteUserGroupResponse;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.AddUserGroupRequest;
import com.homies.homies.services.AddUserGroupResponse;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserAdmin;
import com.homies.homies.services.UserData;
import com.homies.homies.services.UserRequest;
import com.homies.homies.services.UserResponse;
import com.homies.homies.user.MainActivity;

import java.io.ByteArrayOutputStream;
import java.security.acl.Group;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGroupFragment extends Fragment {

    ListView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup;
    EditText userInput;
    Activity activity;
    TextView group, description;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editGroup = inflater.inflate(R.layout.fragment_edit_group, container, false);

        userList = editGroup.findViewById(R.id.userList);
        btnAddUser = editGroup.findViewById(R.id.btnAddUser);
        activity = getActivity();

        group = editGroup.findViewById(R.id.textInputGroupName);
        description = editGroup.findViewById(R.id.textInputDetailGroupName);
        btnDeleteGroup = editGroup.findViewById(R.id.btnDeleteGroup);


        int userAdmin = groupIdAdmin().getId();
        int userId = user().getId();

        if(userId == userAdmin){
            group.setFocusable(true);
            group.setFocusableInTouchMode(true);
            group.setClickable(true);
            description.setFocusable(true);
            description.setFocusableInTouchMode(true);
            description.setClickable(true);
            btnAddUser.setVisibility(View.VISIBLE);
            btnDeleteGroup.setVisibility(View.VISIBLE);

        }else{
            group.setFocusable(false);
            group.setFocusableInTouchMode(false);
            group.setClickable(false);
            description.setFocusable(false);
            description.setFocusableInTouchMode(false);
            description.setClickable(false);
            btnAddUser.setVisibility(View.GONE);
            btnDeleteGroup.setVisibility(View.GONE);
        }

        btnAddUser.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_add_user,
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

                    addUser(createUserListRequest());

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


    public AddUserGroupRequest createUserListRequest() {

        AddUserGroupRequest addUserListRequest = new AddUserGroupRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);

        int idUserAdminGroup = preferences.getInt("USERADMINGROUP_ID", 0);
        int idGroup = preferences.getInt("GROUP_ID", 0);

        addUserListRequest.setIdAdminGroup(idUserAdminGroup);
        addUserListRequest.setLogin(userInput.getText().toString().trim());
        addUserListRequest.setIdGroup(idGroup);

        return addUserListRequest;

    }
    public void addUser(AddUserGroupRequest userListRequest) {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        /*userListRequest.setIdAdminGroup(6);
        userListRequest.setIdGroup(12);*/
        createUserListRequest();

        Call<AddUserGroupResponse> userResponseCall = ApiClient.getService().addUser("Bearer " + retrievedToken, userListRequest);
        //AddUserGroupRequest userListRequest2 = userListRequest; //Para ver datos
        userResponseCall.enqueue(new Callback<AddUserGroupResponse>() {
            @Override
            public void onResponse(Call<AddUserGroupResponse> call, Response<AddUserGroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AddUserGroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public UserData user() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);

        userData.setId(userId);

        return userData;
    }

    public GroupRequest groupIdAdmin() {
        GroupRequest groupRequest = new GroupRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USERADMIN_ID",0);

        groupRequest.setId(userId);

        return groupRequest;

    }

    public void userAdminGroupId(GroupRequest groupRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> userInfo = ApiClient.getService().groupInfo("Bearer " + retrivedToken, groupRequest ,user().getId());
        userInfo.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse userIdGroup= response.body();

                    userIdGroup.getId();
                    userIdGroup.getGroupName();
                    userIdGroup.getGroupRelation();

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

}