package com.homies.homies;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
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
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupListAdapter;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.ProgramViewHolder;
import com.homies.homies.services.UserRequest;
import com.homies.homies.services.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupFragment extends Fragment {

    ListView recyclerView;
    Button btnAdd, btnCancel;
    EditText inputGroup, inputDescription;
    Activity activity;
    ImageButton add;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View group = inflater.inflate(R.layout.fragment_group, container, false);
        getGroup();
        recyclerView = group.findViewById(R.id.recyclerView);
        add = group.findViewById(R.id.addGroup);
        activity = getActivity();


        add.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_layout_botton_addgroup,
                            group.findViewById(R.id.bottonAddContainer)
                    );
            inputGroup = bottomSheetView.findViewById(R.id.inputGroup);
            inputDescription = bottomSheetView.findViewById(R.id.inputDescription);
            btnCancel = bottomSheetView.findViewById(R.id.btnCancel);
            btnAdd = bottomSheetView.findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(view1 -> {
                if (inputGroup.getText().toString().trim().isEmpty()) {
                    String message = getString(R.string.val_name);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    saveGroup(createRequest());

                    bottomSheetDialog.dismiss();
                }
            });
            btnCancel.setOnClickListener(view1 -> {

                    bottomSheetDialog.dismiss();
                });



            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });


        return group;
    }




    public void getGroup() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<List<GroupResponse>> groupResponseCall = ApiClient.getService().getGroup("Bearer " + retrivedToken);
        groupResponseCall.enqueue(new Callback<List<GroupResponse>>() {
            @Override
            public void onResponse(Call<List<GroupResponse>> groupResponseCall, Response<List<GroupResponse>> response) {

                if (response.isSuccessful()) {
                List<GroupResponse> myGroupList = response.body();
                String[] oneGroup = new String[myGroupList.size()];

                for (int i = 0; i < myGroupList.size(); i++) {
                    oneGroup[i] = myGroupList.get(i).getGroupName();
                }

                recyclerView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item,R.id.textViewGroup , oneGroup));
                recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getContext(),"You cliked " + oneGroup[position],Toast.LENGTH_SHORT).show();
                    }
                });


                }else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupResponse>> groupResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "An error has occured", Toast.LENGTH_LONG).show();

            }

        });
    }
    public GroupRequest createRequest() {
        GroupRequest groupRequest = new GroupRequest();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);
        groupRequest.setUser(userId);
        groupRequest.setGroupName(inputGroup.getText().toString());
        groupRequest.setGroupRelation(inputDescription.getText().toString());


        return groupRequest;
    }

    public void saveGroup(GroupRequest groupRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> groupResponseCall = ApiClient.getService().saveGroup("Bearer " + retrivedToken,groupRequest);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.groupSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    getGroup();
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

    public void onItemClick (AdapterView<?> parent,
                                      View view,
                                      int position,
                                      long id) {

    }
}