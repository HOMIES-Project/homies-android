package com.homies.homies.groups;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.homies.homies.Adapters.UserAdapter;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.group.AddUser;
import com.homies.homies.retrofit.model.group.ChangeAdmin;
import com.homies.homies.retrofit.model.user.DeleteUser;
import com.homies.homies.retrofit.model.group.GroupRequest;
import com.homies.homies.retrofit.model.group.GroupResponse;
import com.homies.homies.retrofit.model.group.LeaveGroup;
import com.homies.homies.retrofit.model.user.UserData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGroupFragment extends Fragment {

    RecyclerView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup,
            btnConfirmChangeAdmin,btnCancelChangeAdmin,btnLeaveGroup,btnConfirmLeaveGroup,act;
    EditText userInput,et_GroupName, et_detail;
    Activity activity;
    TextInputLayout ip_groupName,ip_groupDetail;
    ImageButton delete;
    ArrayList<UserData> usuarios;
    UserAdapter adaptador;
    Context context;
    UserAdapter.ClickedItem clickedItem;
    Toolbar toolbar;
    ProgressBar progressBar;
    boolean condition = true;




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

        progressBar = editGroup.findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);



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
                    progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.VISIBLE);
                leaveGroup(leaveRequest());
                bottomSheetDialog.dismiss();

            });
            btnCancelActionGroup.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        validateFields();
        //Update Info Group
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition = true;
                validateClickFields();
                if (condition) {
                    progressBar.setVisibility(View.VISIBLE);
                    updateInfoGroup(createRequestGroup());
                }

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
                    progressBar.setVisibility(View.GONE);
                    GroupResponse adslist= response.body();

                    String user = adslist.getGroupName();
                    String detail = adslist.getGroupRelationName();
                    et_GroupName.setText(user);
                    et_detail.setText(detail);

                    if (adslist.getUserAdmin().getId() != userId){

                        btnAddUser.setVisibility(View.GONE);
                        btnDeleteGroup.setVisibility(View.GONE);
                        act.setVisibility(View.GONE);
                        et_GroupName.setFocusable(false);
                        et_GroupName.setFocusableInTouchMode(false);
                        et_GroupName.setClickable(false);
                        et_detail.setFocusable(false);
                        et_detail.setFocusableInTouchMode(false);
                        et_detail.setClickable(false);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    GroupResponse data = response.body();

                    adaptador.setData(data);
                    userList.setAdapter(adaptador);

                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    String message = getString(R.string.userSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupInfo();
                    groupPhoto();
                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    String message = getString(R.string.deleteSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupInfo();
                    groupPhoto();

                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    preferences.edit().putString("USER_NAME",response.body().getUser().getLogin()).apply();

                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    String message = getString(R.string.deleteSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    GroupFragment groupFragment = new GroupFragment();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGroup, groupFragment);
                    fragmentTransaction.commit();

                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
                    String message = getString(R.string.deleteGroupSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    GroupFragment groupFragment = new GroupFragment();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGroup, groupFragment);
                    fragmentTransaction.commit();

                } else {
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
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
    // Validations when interacting with form fields
    private void validateFields() {
        //region validation
        et_GroupName.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_GroupName.getText().toString().trim().isEmpty()) {
                    ip_groupName.setError(getString(R.string.val_name));
                    condition = false;
                }if (et_GroupName.getText().toString().trim().length() < 3) {
                    ip_groupName.setError(getString(R.string.val_group));
                    condition = false;
                } else {
                    ip_groupName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
        //region validationUser

        et_detail.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_detail.getText().toString().trim().isEmpty()) {
                    ip_groupDetail.setError(getString(R.string.val_desc));
                    condition = false;
                }if (et_detail.getText().toString().trim().length() < 3) {
                    ip_groupDetail.setError(getString(R.string.val_group));
                    condition = false;
                } else {
                    ip_groupDetail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
    }
    //Validations when clicking on the registration button
    private void validateClickFields() {

        String nameGroup = et_GroupName.getText().toString().trim();
        if (nameGroup.isEmpty()) {
            ip_groupName.setError(getString(R.string.val_name));
            condition = false;
        }if (nameGroup.length() < 3) {
            ip_groupName.setError(getString(R.string.val_group));
            condition = false;
        }else {
            ip_groupName.setErrorEnabled(false);
        }

        String descriptionInput = et_detail.getText().toString().trim();
        if (descriptionInput.isEmpty()) {
            ip_groupDetail.setError(getString(R.string.val_desc));
            condition = false;
        }
        if (descriptionInput.length() < 3) {
            ip_groupDetail.setError(getString(R.string.val_group));
            condition = false;
        }else {
            ip_groupDetail.setErrorEnabled(false);
        }


    }
}