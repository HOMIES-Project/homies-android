package com.homies.homies.groups;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.UserResponse;
import com.homies.homies.user.MainActivity;
import com.homies.homies.user.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoGroupFragment extends Fragment {

    RecyclerView userList;
    Button btnAddUser, btnCancelAction, btnConfirmUser, btnDeleteGroup, btnCancelActionGroup, btnConfirmDeleteGroup,btnConfirmChangeAdmin,btnCancelChangeAdmin;
    EditText userInput;
    Activity activity;
    EditText et_GroupName, et_detail;
    TextInputLayout ip_groupName,ip_groupDetail;
    ImageButton delete;
    ArrayList<UserData> usuarios;
    UserAdapter adaptador;
    Context context;
    UserAdapter.ClickedItem clickedItem;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editGroup = inflater.inflate(R.layout.fragment_info_group, container, false);

        userList = editGroup.findViewById(R.id.userList);
        btnAddUser = editGroup.findViewById(R.id.btnAddUser);
        btnCancelActionGroup = editGroup.findViewById(R.id.btnCancelActionGroup);
        btnConfirmDeleteGroup = editGroup.findViewById(R.id.btnConfirmDeleteGroup);
        activity = getActivity();

        context = getActivity().getApplicationContext();


        et_GroupName = editGroup.findViewById(R.id.et_GroupName);
        et_detail = editGroup.findViewById(R.id.et_detail);

        ip_groupName = editGroup.findViewById(R.id.ip_groupName);
        ip_groupDetail = editGroup.findViewById(R.id.ip_groupDetail);
        btnDeleteGroup = editGroup.findViewById(R.id.btnDeleteGroup);

        delete = editGroup.findViewById(R.id.delete);


        usuarios = new ArrayList<>();
        ArrayList<GroupResponse> arrayOfUsers = new ArrayList<GroupResponse>();


        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adaptador = new UserAdapter(clickedItem);

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

        groupInfo();
        groupPhoto();


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
            btnConfirmDeleteGroup = bottomSheetView.findViewById(R.id.btnConfirmUser);
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


        return editGroup;
    }

    public UserData userInf() {
        UserData userData = new UserData();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        userData.setId(userId);

        return userData;
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
                    et_GroupName.setText(user);
                    et_detail.setText(detail);

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

    public DeleteUser deleteRequest() {
        DeleteUser deleteUser = new DeleteUser();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        String memberGroup = preferences.getString("MEMBERID",null);
        //preferences.edit().remove("MEMBERID").commit();
        deleteUser.setLogin(memberGroup);
        deleteUser.setIdGroup(idGroup);
        deleteUser.setIdAdminGroup(userId);

        return deleteUser;
    }


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

    public void deleteGroup() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> deleteRequest = NetworkConfig.getService().deleteGroup("Bearer " + retrivedToken, userInf().getId());
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
}