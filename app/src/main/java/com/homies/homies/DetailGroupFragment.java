package com.homies.homies;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserAdmin;
import com.homies.homies.services.UserListRequest;
import com.homies.homies.services.UserListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGroupFragment extends Fragment {

    ListView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup;
    EditText userInput;
    Activity activity;
    TextView group, description;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View user = inflater.inflate(R.layout.fragment_detail_group, container, false);
        getUser();
        userList = user.findViewById(R.id.userList);
        btnAddUser = user.findViewById(R.id.btnAddUser);
        activity = getActivity();

        group = user.findViewById(R.id.textInputGroupName);
        description = user.findViewById(R.id.textInputDetailGroupName);
        btnDeleteGroup = user.findViewById(R.id.btnDeleteGroup);



        btnAddUser.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_add_user,
                            user.findViewById(R.id.addUserContainer)
                    );
            userInput = bottomSheetView.findViewById(R.id.userInput);
            btnCancelAction = bottomSheetView.findViewById(R.id.btnCancelAction);
            btnConfirmUser = bottomSheetView.findViewById(R.id.btnConfirmUser);
            btnConfirmUser.setOnClickListener(view1 -> {
                if (userInput.getText().toString().trim().isEmpty()) {
                    String message = getString(R.string.val_user);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    saveUser(createUserListRequest());

                    bottomSheetDialog.dismiss();
                }
            });
            btnCancelAction.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });


        return user;

        btnDeleteGroup.setOnClickListener((View.OnClickListener) view -> {
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
        
        return group;
    }




    public void getUser() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        Call<List<UserListResponse>> userResponseCall = ApiClient.getService().addUser("Bearer " + retrievedToken);
        userResponseCall.enqueue(new Callback<List<UserListResponse>>() {
            @Override
            public void onResponse(Call<List<UserListResponse>> userResponseCall, Response<List<UserListResponse>> response) {

                if (response.isSuccessful()) {
                    List<UserListResponse> myUserList = response.body();
                    String[] oneUsersList = new String[myUserList.size()];

                    for (int i = 0; i < myUserList.size(); i++) {
                        oneUsersList[i] = myUserList.get(i).getGroupName();
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
            public void onFailure(Call<List<UserListResponse>> userResponseCall, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

            }

        });
    }
    public UserListRequest createUserListRequest() {
        UserListRequest userListRequest = new UserListRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int idUser = preferences.getInt("USER", 0);
        int idGroup = preferences.getInt("GROUP_ID", 0);
        userListRequest.setIdAdminGroup(idUser);
        userListRequest.setLogin(userInput.getText().toString().trim());
        userListRequest.setIdGroup(idGroup);


        return userListRequest;
    }

    public void saveUser(UserListRequest userListRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        Call<UserListResponse> userResponseCall = ApiClient.getService().saveUser("Bearer " + retrievedToken, userListRequest);
        userResponseCall.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    getUser();
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}