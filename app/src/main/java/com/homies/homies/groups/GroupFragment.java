package com.homies.homies.groups;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.GroupRequest;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupFragment extends Fragment {

    ListView listView;
    EditText inputGroup, inputDescription;
    Activity activity;
    ImageButton add;
    TextView numberUser;
    ProgressBar progressBar;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View group = inflater.inflate(R.layout.fragment_group, container, false);
        groupList();
        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Grupos");
        listView = group.findViewById(R.id.listView);
        add = group.findViewById(R.id.addGroup);
        activity = getActivity();
        numberUser = group.findViewById(R.id.numberUser);

        progressBar = group.findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);


        return group;
    }

    //Method to get the list of groups where the user appears
    public void groupList() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Call<UserData> groupResponseCall = NetworkConfig.getService().userInfo("Bearer " + retrivedToken,userId);
        groupResponseCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> groupResponseCall, Response<UserData> response) {

                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                List<GroupResponse> myGroupList = response.body().getGroups();
                String[] oneGroup = new String[myGroupList.size()];
                    String[] numbersUser = new String[myGroupList.size()];

                for (int i = 0; i < myGroupList.size(); i++) {
                    oneGroup[i] = myGroupList.get(i).getGroupName();
                    //numbersUser[i] = String.valueOf(myGroupList.get(i).getUserdata().size());
                }

                    listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item,R.id.textViewGroup , oneGroup));
                    //listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item,R.id.numberUser , numbersUser));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

                            transaction.replace(R.id.fragmentGroup, DetailGroupFragment.class, null);
                            transaction.addToBackStack(null);
                            transaction.commit();

                            Toast.makeText(getContext(),"You cliked " + oneGroup[position],Toast.LENGTH_SHORT).show();//Toast temporal, no añadir string
                        }
                    });


                }else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> groupResponseCall, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

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
    //Method to create a new group
    public void saveGroup(GroupRequest groupRequest) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().saveGroup("Bearer " + retrivedToken,groupRequest);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    String message = getString(R.string.groupSucess);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    groupList();
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