package com.homies.homies.groups;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.GroupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;


public class ListsGroupFragment extends Fragment {

    Button btnEditGroup, btnAddTask, btnCreateTask, btnCancelTask;
    EditText userTask, descriptionTask;
    Spinner listUserTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View info = inflater.inflate(R.layout.fragment_lists_group, container, false);

        groupInfo();

        btnAddTask = info.findViewById(R.id.btn_addTask);

        btnAddTask.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(getContext())
                    .inflate(
                            R.layout.dialog_add_task,
                            info.findViewById(R.id.addTask)
                    );
            userTask = bottomSheetView.findViewById(R.id.userTask);
            btnCreateTask = bottomSheetView.findViewById(R.id.btnCreateTask);
            btnCancelTask = bottomSheetView.findViewById(R.id.btnCancelTask);
            listUserTask = bottomSheetView.findViewById(R.id.listUserTask);
            descriptionTask = bottomSheetView.findViewById(R.id.descriptionTask);

            btnCreateTask.setOnClickListener(view1 -> {
                if (listUserTask.toString().trim().isEmpty()) {
                    String message = getString(R.string.val_user);
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {


                    bottomSheetDialog.dismiss();
                }
            });
            btnCancelTask.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });




        return info;


    }
    public void groupInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        int userId  = preferences.getInt("USER_ID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupInfo("Bearer " + retrivedToken,idGroup);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse adslist= response.body();

                    String user = adslist.getGroupName();


                    ImageButton b1=new ImageButton(((MenuActivity)getActivity()));
                    b1.setImageResource(R.drawable.ic_info);
                    Toolbar.LayoutParams l3=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
                    l3.gravity= Gravity.END;
                    b1.setLayoutParams(l3);

                    String nameGroup = ((MenuActivity)getActivity()).getSupportActionBar().getTitle().toString();
                    if(nameGroup  == "Grupos"){
                        ((MenuActivity)getActivity()).toolbar.addView(b1);
                    }
                    ((MenuActivity)getActivity()).getSupportActionBar().setTitle(user);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

                            //transaction.replace(R.id.fragmentDetailGroup, EditGroupFragment.class, null);
                            transaction.replace(R.id.fragmentGroup, InfoGroupFragment.class, null);
                            transaction.addToBackStack(null);
                            transaction.commit();

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
}
