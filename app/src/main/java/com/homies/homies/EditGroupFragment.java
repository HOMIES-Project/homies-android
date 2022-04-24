package com.homies.homies;


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
    AddUserGroupRequest addUserGroupRequest;


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




        /*//get user
        ApiClient.getInstance().getUser(userId).enqueue(new Callback<UserAdmin>());

        //get userAdmin
        ApiClient.getInstance().getUserAdmin(userId);*/

        //userAdmin

        int userNu = user().getId();


        if(userNu == idAdmin){
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


    /*public void getUser() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        Call<List<AddUserGroupResponse>> userResponseCall = ApiClient.getService().addUser("Bearer " + retrievedToken);
        userResponseCall.enqueue(new Callback<List<AddUserGroupResponse>>() {
            @Override
            public void onResponse(Call<List<AddUserGroupResponse>> userResponseCall, Response<List<AddUserGroupResponse>> response) {
                if (response.isSuccessful()) {
                    List<AddUserGroupResponse> myUserList = response.body();
                    String[] oneUsersList = new String[myUserList.size()];

                    for (int i = 0; i < myUserList.size(); i++) {
                        oneUsersList[i] = myUserList.get(i).getUserAdmin();
                    }
                    userList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.user_item,R.id.textViewUser , oneUsersList));
                    userList.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getContext(),"You cliked " + oneUsersList[position],Toast.LENGTH_SHORT).show();//Toast temporal para eliminar usuario
                        }
                    });

                }else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<AddUserGroupResponse>> userResponseCall, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }*/

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

    public void deleteUser(DeleteUserGroupRequest deleteUserGroupRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<DeleteUserGroupResponse> deleteUserGroup = ApiClient.getService().deleteUser("Bearer " + retrivedToken, deleteUserGroupRequest);
        deleteUserGroup.enqueue(new Callback<DeleteUserGroupResponse>() {
            @Override
            public void onResponse(Call<DeleteUserGroupResponse> call, Response<DeleteUserGroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.delete_user_group);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DeleteUserGroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

   public void getGroupUserAdmin(UserAdmin userAdmin) {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);

        Call<GroupResponse> groupResponseCall = ApiClient.getService().groupInfo("Bearer " + retrievedToken, userAdmin);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {

                   response.body().getId();
                   response.body().getGroupName();
                   response.body().getGroupRelation();

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

    public UserData user() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);

        userData.setId(userId);

        return userData;
    }

    /*public AddUserGroupRequest getGroupUserAdmin() {

        AddUserGroupRequest userAdmin = new AddUserGroupRequest();
        userAdmin.getIdAdminGroup();

        return userAdmin;
    }*/





}