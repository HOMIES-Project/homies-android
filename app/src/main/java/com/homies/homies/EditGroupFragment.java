package com.homies.homies;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserAdmin;
import com.homies.homies.services.AddUserGroupRequest;
import com.homies.homies.services.AddUserGroupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGroupFragment extends Fragment {

    ListView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup;
    EditText userInput;
    Activity activity;
    TextView group, description;

    int user;

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

        UserAdmin userAdmin = new UserAdmin();
        user = userAdmin.getId();

        if(user == userAdminInf().getId()){
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

        //groupUserInfo();
        //getUser();

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

        /*btnDeleteGroup.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_delete_group,
                           group.findViewById(R.id.groupDeleteContainer)
                    );

            btnCancelActionGroup = bottomSheetView.findViewById(R.id.btnCancelActionGroup);
            btnConfirmDeleteGroup = bottomSheetView.findViewById(R.id.btnConfirmDeleteGroup);
            btnConfirmDeleteGroup.setOnClickListener(view1 -> {
                  bottomSheetDialog.dismiss();
                }
            });
            btnCancelActionGroup.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });
        
        return group;*/
    }

    public UserAdmin userAdminInf() {
        UserAdmin userAdmin = new UserAdmin();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userAdminId  = preferences.getInt("USERADMIN_ID",0);

        userAdmin.setId(userAdminId);


        return userAdmin;
    }


    /*public void getUser() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        Call<List<UserResponse>> userResponseCall = ApiClient.getService().getUser("Bearer " + retrievedToken);
        userResponseCall.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> userResponseCall, Response<List<UserResponse>> response) {
                if (response.isSuccessful()) {
                    List<UserResponse> myUserList = response.body();
                    String[] oneUsersList = new String[myUserList.size()];

                    for (int i = 0; i < myUserList.size(); i++) {
                        oneUsersList[i] = myUserList.get(i).getFirstName();
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
            public void onFailure(Call<List<UserResponse>> userResponseCall, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public AddUserGroupRequest createUserListRequest() {
        AddUserGroupRequest addUserListRequest = new AddUserGroupRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int idUser = preferences.getInt("USER", 0);
        int idGroup = preferences.getInt("GROUP_ID", 0);
        addUserListRequest.setIdAdminGroup(idUser);
        addUserListRequest.setLogin(userInput.getText().toString().trim());
        addUserListRequest.setIdGroup(idGroup);


        return addUserListRequest;
    }

    public void addUser(AddUserGroupRequest userListRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        userListRequest.setIdAdminGroup(6);
        String retrievedToken  = preferences.getString("TOKEN",null);
        userListRequest.setIdGroup(12);
        Call<String> userResponseCall = ApiClient.getService().addUser2("Bearer " + retrievedToken, userListRequest);
        AddUserGroupRequest userListRequest2 = userListRequest; //Para ver datos

        userResponseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    //getUser();
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserOld(AddUserGroupRequest userListRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        userListRequest.setIdAdminGroup(6);
        userListRequest.setIdGroup(12);
        Call<AddUserGroupResponse> userResponseCall = ApiClient.getService().addUser("Bearer " + retrievedToken, userListRequest);
        AddUserGroupRequest userListRequest2 = userListRequest; //Para ver datos

        userResponseCall.enqueue(new Callback<AddUserGroupResponse>() {
            @Override
            public void onResponse(Call<AddUserGroupResponse> call, Response<AddUserGroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    //getUser();
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

    /*public void deletGroup() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserResponse> deleteRequest = ApiClient.getService().deleteGroup("Bearer " + retrivedToken, userInf().getId());
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
    }*/

    /*public void groupUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<AddUserGroupResponse> userList = ApiClient.getService().groupInfo("Bearer " + retrivedToken, userAdminInf().getId());
        userList.enqueue(new Callback<AddUserGroupResponse>() {
            @Override
            public void onResponse(Call<AddUserGroupResponse> call, Response<AddUserGroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse addlist= response.body();
                    String groupName = addlist.getGroupName();
                    String detailGroup = addlist.getGroupRelation();
                    group.setText(groupName);
                    description.setText(detailGroup);
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
    }*/
}