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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.GroupResponse;
import com.homies.homies.services.UserAdmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailGroupFragment extends Fragment {

    ListView recyclerView;
    

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View user = inflater.inflate(R.layout.fragment_detail_group, container, false);
        getUser();
        recyclerView = user.findViewById(R.id.userView);


        return user;
    }

    public void getUser() {

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

                recyclerView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, oneGroup));
                    Toast.makeText(getActivity(), "Hola", Toast.LENGTH_LONG).show();
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
}