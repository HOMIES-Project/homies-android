package com.homies.homies;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homies.homies.services.ApiClient;
import com.homies.homies.services.GroupRequest;
import com.homies.homies.services.GroupResponse;

import java.text.DateFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupFragment extends Fragment {

    ListView recyclerView;
    String groups;
    

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View group = inflater.inflate(R.layout.fragment_group, container, false);
        /*MyListData[] myListData = new MyListData[] {
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),


        };


        RecyclerView recyclerView = (RecyclerView) group.findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);*/

        recyclerView = group.findViewById(R.id.recyclerView);



        getGroup();

        return group;
    }

    public void getGroup() {
        Call<List<GroupResponse>> groupResponseCall = ApiClient.getService().getGroup();
        groupResponseCall.enqueue(new Callback<List<GroupResponse>>() {
            @Override
            public void onResponse(Call<List<GroupResponse>> call, Response<List<GroupResponse>> response) {
                List<GroupResponse> myGroupList = response.body();
                String[] oneGroup = new String[myGroupList.size()];

                for (int i = 0; i < myGroupList.size(); i++) {
                    oneGroup[i] = myGroupList.get(i).getGroupName();
                }

                recyclerView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, oneGroup));
            }

            @Override
            public void onFailure(Call<List<GroupResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "An error has occured", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void onItemClick (AdapterView<?> parent,
                                      View view,
                                      int position,
                                      long id) {

    }
}