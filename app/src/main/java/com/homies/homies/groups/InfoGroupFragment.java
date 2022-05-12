package com.homies.homies.groups;


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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.R;
import com.homies.homies.retrofit.api.UserService;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.group.AddUserGroupRequest;
import com.homies.homies.retrofit.model.group.AddUserGroupResponse;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.tasks.UserTasksListModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.makeText;

//
//
// IN DEVELOPMENT
//
//

public class InfoGroupFragment extends Fragment {

    TextView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup;
    EditText userInput;
    Activity activity;
    EditText et_GroupName, et_detail;
    TextInputLayout ip_groupName,ip_groupDetail;

    //AddUserGroupResponse addUserGroupResponse;

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
        groupInfo();
        //getUserTasks();



        //extract the data of the logged in user and the userAdmin of the group.
        int userId = user().getId();
        //int userAdmin = addUserGroupResponse.getUserAdmin().getId();

        //If the user is the administrator, the buttons are enabled or disabled.
        /*if(userId == userAdmin){
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
        }*/

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
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

 /*   //TRAEMOS Y PINTAMOS LISTADO DE TAREAS DEL USER
    private void getUserTasks(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://homies-back-app.herokuapp.com/api")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserService userService = retrofit.create(UserService.class);

        Call<List<UserTasksListModel>> call = userService.getUserTasks();

        call.enqueue(new Callback<List<UserTasksListModel>>() {
            @Override
            public void onResponse(Call<List<UserTasksListModel>> call, Response<List<UserTasksListModel>> response) {
                if(!response.isSuccessful()){
                    userList.setText("codigo" + response.code());
                    Log.d("respuesta", "falta datos");
                    return;
                }
                List<UserTasksListModel> tasksList = response.body();

                for(UserTasksListModel userTasksListModel: tasksList){
                    String content = "";
                    content += "Tarea:"+ userTasksListModel.getLogin() + "\n";
                    userList.append(content);

                }
            }

            @Override
            public void onFailure(Call<List<UserTasksListModel>> call, Throwable t) {
                Log.d("respuesta", "error");
            }
        });
    }
*/
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

                    et_GroupName.setText(user);
                    et_detail.setText(detail);

                } else {
                    String message = getString(R.string.error_login);
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            }
        });

    }



    //Method for requesting group parameters
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
    //Method to add user to group
    public void addUserGroup(/*AddUserGroupRequest addUserGroupRequest*/) {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        /*userListRequest.setIdAdminGroup(6);
        userListRequest.setIdGroup(12);*/
        createUserListRequest();

        Call<ArrayList<AddUserGroupResponse>> addUserGroupResponseCall = NetworkConfig.getService().addUserGroup("Bearer " + retrievedToken);
        //AddUserGroupRequest userListRequest2 = userListRequest; //Para ver datos
        addUserGroupResponseCall.enqueue(new Callback<ArrayList<AddUserGroupResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AddUserGroupResponse>> call, Response<ArrayList<AddUserGroupResponse>> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    ArrayList<AddUserGroupResponse> data = response.body();

                    RecyclerViewAdapterListUser adapterListUser = new RecyclerViewAdapterListUser(getContext(), data);
                    //userList.setAdapter(adapterListUser);
                } else {
                    String message = getString(R.string.error_login);
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<AddUserGroupResponse>> call, Throwable t) {
                String message = t.getLocalizedMessage();
                makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method to obtain the logged in user.
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