package com.homies.homies.groups;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.homies.homies.R;
import com.homies.homies.UserAdapter;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.AddUser;
import com.homies.homies.retrofit.model.ChangeAdmin;
import com.homies.homies.retrofit.model.DeleteUser;
import com.homies.homies.retrofit.model.GroupRequest;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.LeaveGroup;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.UserRequest;
import com.homies.homies.retrofit.model.UserResponse;
import com.homies.homies.user.LoginFragment;
import com.homies.homies.user.MainActivity;
import com.homies.homies.user.RegisterFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGroupFragment extends Fragment {

    RecyclerView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup,
            btnConfirmChangeAdmin,btnCancelChangeAdmin,btnLeaveGroup,btnConfirmLeaveGroup,act;
    EditText userInput;
    Activity activity;
    EditText et_GroupName, et_detail;
    TextInputLayout ip_groupName,ip_groupDetail;
    ImageButton delete;
    ArrayList<UserData> usuarios;
    UserAdapter adaptador;
    Context context;
    UserAdapter.ClickedItem clickedItem;
    Toolbar toolbar;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editGroup = inflater.inflate(R.layout.fragment_info_group, container, false);

        toolbar = ((MenuActivity)getActivity()).findViewById(R.id.toolbar);
        userList = editGroup.findViewById(R.id.userList);
        btnAddUser = editGroup.findViewById(R.id.btnAddUser);
        btnCancelActionGroup = editGroup.findViewById(R.id.btnCancelActionGroup);
        btnConfirmDeleteGroup = editGroup.findViewById(R.id.btnConfirmDeleteGroup);
        btnLeaveGroup = editGroup.findViewById(R.id.btnLeaveGroup);
        act = editGroup.findViewById(R.id.act);

        activity = getActivity();

        context = getActivity().getApplicationContext();


        et_GroupName = editGroup.findViewById(R.id.et_GroupName);
        et_detail = editGroup.findViewById(R.id.et_detail);

        ip_groupName = editGroup.findViewById(R.id.ip_groupName);
        ip_groupDetail = editGroup.findViewById(R.id.ip_groupDetail);
        btnDeleteGroup = editGroup.findViewById(R.id.btnDeleteGroup);

        delete = userList.findViewById(R.id.delete);


        usuarios = new ArrayList<>();
        ArrayList<GroupResponse> arrayOfUsers = new ArrayList<GroupResponse>();


        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adaptador = new UserAdapter(clickedItem);

        //events within the listview
        adaptador.setOnItemClickListener(new UserAdapter.ClickedItem() {
            @Override
            public void ClickedUser(UserData groupResponse) {
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

                    deleteUser(deleteRequest());
                    bottomSheetDialog.dismiss();

                });
                btnCancelActionGroup.setOnClickListener(view1 -> {

                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }

            @Override
            public void Clicked(UserData groupResponse) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getActivity(), R.style.BottonSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                        .inflate(
                                R.layout.dialog_change_admin,
                                getActivity().findViewById(R.id.groupChangeAdmin)
                        );
                btnCancelActionGroup = bottomSheetView.findViewById(R.id.btnCancelActionGroup);
                btnCancelChangeAdmin = bottomSheetView.findViewById(R.id.btnCancelChangeAdmin);
                btnConfirmChangeAdmin = bottomSheetView.findViewById(R.id.btnConfirmChangeAdmin);
                btnConfirmChangeAdmin.setOnClickListener(view1 -> {

                    changeAdmin(changeRequest());
                    bottomSheetDialog.dismiss();

                });
                btnCancelActionGroup.setOnClickListener(view1 -> {

                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        //Add User
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

        //Delete group
        btnDeleteGroup.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_delete_user,
                            editGroup.findViewById(R.id.groupDeleteContainer)
                    );

            btnCancelActionGroup = bottomSheetView.findViewById(R.id.btnCancelActionGroup);
            btnConfirmDeleteGroup = bottomSheetView.findViewById(R.id.btnConfirmDeleteGroup);
            btnConfirmDeleteGroup.setOnClickListener(view1 -> {

                deleteGroup();


                bottomSheetDialog.dismiss();

            });
            btnCancelActionGroup.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        //Leave group
        btnLeaveGroup.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.dialog_leave_group,
                            editGroup.findViewById(R.id.groupLeaveContainer)
                    );

            btnCancelActionGroup = bottomSheetView.findViewById(R.id.btnCancelActionGroup);
            btnConfirmLeaveGroup = bottomSheetView.findViewById(R.id.btnConfirmLeaveGroup);
            btnConfirmLeaveGroup.setOnClickListener(view1 -> {

                leaveGroup(leaveRequest());
                bottomSheetDialog.dismiss();

            });
            btnCancelActionGroup.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfoGroup(createRequestGroup());

            }
        });

        userInfo();
        groupInfo();
        groupPhoto();


        return editGroup;
    }

    //data to obtain information from the group
    public UserData userInf() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        userData.setId(userId);

        return userData;
    }

    //method of obtaining group information
    public void groupInfo() {
        DeleteUser deleteUser = new DeleteUser();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        String userGroup = preferences.getString("USER_NAME",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);

        deleteUser.setIdGroup(idGroup);
        deleteUser.setIdAdminGroup(userId);

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

                    if (adslist.getUserAdmin().getId() != userId){

                        btnAddUser.setVisibility(View.GONE);
                        btnDeleteGroup.setVisibility(View.GONE);
                        et_GroupName.setFocusable(false);
                        et_GroupName.setFocusableInTouchMode(false);
                        et_GroupName.setClickable(false);
                        et_detail.setFocusable(false);
                        et_detail.setFocusableInTouchMode(false);
                        et_detail.setClickable(false);
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

    //method for obtaining listing information
    public void groupPhoto() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupPhoto("Bearer " + retrivedToken,idGroup);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {

                    GroupResponse data = response.body();

                    adaptador.setData(data);
                    userList.setAdapter(adaptador);

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

    //data to add user
    public void addUserGroup(AddUser addUser) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> userResponseCall = NetworkConfig.getService().addUserGroup("Bearer " + retrivedToken,addUser);
        userResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupInfo();
                    groupPhoto();
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
    //data to delete user
    public DeleteUser deleteRequest() {
        DeleteUser deleteUser = new DeleteUser();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        String memberGroup = preferences.getString("MEMBERID",null);
        deleteUser.setLogin(memberGroup);
        deleteUser.setIdGroup(idGroup);
        deleteUser.setIdAdminGroup(userId);

        return deleteUser;
    }

    //method to delete user
    public void deleteUser(DeleteUser deleteUser) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> userResponseCall = NetworkConfig.getService().deleteUserGroup("Bearer " + retrivedToken,deleteUser);
        userResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.deleteSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupInfo();
                    groupPhoto();

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

    //data to leave the group
    public LeaveGroup leaveRequest() {
        LeaveGroup leaveGroup = new LeaveGroup();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        String username = preferences.getString("USER_NAME",null);
        leaveGroup.setLogin(username);
        leaveGroup.setIdGroup(idGroup);

        return leaveGroup;
    }

    public void userInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<UserData> userInfo = NetworkConfig.getService().userInfo("Bearer " + retrivedToken, userInf().getId());
        userInfo.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    preferences.edit().putString("USER_NAME",response.body().getUser().getLogin()).apply();

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

    //method for leaving the group
    public void leaveGroup(LeaveGroup leaveGroup) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> userResponseCall = NetworkConfig.getService().leaveUserGroup("Bearer " + retrivedToken,leaveRequest());
        userResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.deleteSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    GroupFragment groupFragment = new GroupFragment();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGroup, groupFragment);
                    fragmentTransaction.commit();

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
    //method to delete the group
    public void deleteGroup() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        String userGroup = preferences.getString("USER_NAME",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> deleteRequest = NetworkConfig.getService().deleteGroup("Bearer " + retrivedToken, idGroup);
        deleteRequest.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.deleteGroupSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    GroupFragment groupFragment = new GroupFragment();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGroup, groupFragment);
                    fragmentTransaction.commit();

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

    //to change administrator
    public ChangeAdmin changeRequest() {
        ChangeAdmin changeAdmin = new ChangeAdmin();
        SharedPreferences preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        String memberGroup = preferences.getString("MEMBERID",null);
        changeAdmin.setLogin(memberGroup);
        changeAdmin.setIdGroup(idGroup);
        changeAdmin.setIdAdminGroup(userId);

        return changeAdmin;
    }

    //method to change administrator
    public void changeAdmin(ChangeAdmin changeAdmin) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> userResponseCall = NetworkConfig.getService().changeAdmin("Bearer " + retrivedToken,changeAdmin);
        userResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = context.getString(R.string.userSucess);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                } else {
                    String message = context.getString(R.string.error_login);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //data to update the group
    public GroupRequest createRequestGroup() {
        GroupRequest groupRequest = new GroupRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        groupRequest.setUser(userId);
        groupRequest.setGroupName(et_GroupName.getText().toString());
        groupRequest.setGroupRelation(et_detail.getText().toString());


        return groupRequest;
    }
    //method to update the group
    public void updateInfoGroup(GroupRequest groupRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        //int idGroup  = preferences.getInt("GROUPID",0);
        int userId  = preferences.getInt("USER_ID",0);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int idGroup = preferences.getInt("GROUPID",0);
        Call<GroupResponse> updateInfo = NetworkConfig.getService().updateInfoGroup("Bearer " + retrivedToken, idGroup,createRequestGroup());
        updateInfo.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse adslist= response.body();

                    String title = adslist.getGroupName();

                    try{

                        ((MenuActivity)getActivity()).getSupportActionBar().setTitle(title);
                        ((MenuActivity)getActivity()).getSupportActionBar(toolbar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    groupInfo();

                    String message = getString(R.string.updateInfo);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

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