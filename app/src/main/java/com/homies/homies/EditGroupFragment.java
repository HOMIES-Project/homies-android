package com.homies.homies;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.AddUserGroupRequest;
import com.homies.homies.services.AddUserGroupResponse;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGroupFragment extends Fragment {

    RecyclerView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup;
    EditText userInput;
    Activity activity;
    TextView group, description;
    AddUserGroupResponse addUserGroupResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editGroup = inflater.inflate(R.layout.fragment_edit_group, container, false);

        userList = editGroup.findViewById(R.id.userList);
        userList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        btnAddUser = editGroup.findViewById(R.id.btnAddUser);
        activity = getActivity();

        group = editGroup.findViewById(R.id.textInputGroupName);
        description = editGroup.findViewById(R.id.textInputDetailGroupName);
        btnDeleteGroup = editGroup.findViewById(R.id.btnDeleteGroup);



        //Extraigo los datos del user que ha hecho login y del userAdmin del grupo
        int userId = user().getId();
        int userAdmin = addUserGroupResponse.getUserAdmin().getId();

        //Si el usuario es el administrador se habilitan o deshabilitan los botones
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

                    //addUserGroup(createUserListRequest());
                    addUserGroup();
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


    //Metodo para pedir los parámetros del grupo
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
    //Metodo para añadir usuario al grupo
    public void addUserGroup(/*AddUserGroupRequest addUserGroupRequest*/) {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        /*userListRequest.setIdAdminGroup(6);
        userListRequest.setIdGroup(12);*/
        createUserListRequest();

        Call<ArrayList<AddUserGroupResponse>> addUserGroupResponseCall = ApiClient.getService().addUserGroup("Bearer " + retrievedToken);
        //AddUserGroupRequest userListRequest2 = userListRequest; //Para ver datos
        addUserGroupResponseCall.enqueue(new Callback<ArrayList<AddUserGroupResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AddUserGroupResponse>> call, Response<ArrayList<AddUserGroupResponse>> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    ArrayList<AddUserGroupResponse> data = response.body();

                    RecyclerViewAdapterListUser adapterListUser = new RecyclerViewAdapterListUser(getContext(), data);
                    userList.setAdapter(adapterListUser);
                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AddUserGroupResponse>> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Metodo para obtener el usuario que ha hecho login.
    public UserData user() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);

        userData.setId(userId);

        return userData;
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