package com.homies.homies.groups;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.homies.homies.R;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.GroupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListsGroupFragment extends Fragment {

    Button btnEditGroup;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View info = inflater.inflate(R.layout.fragment_lists_group, container, false);
        toolbar = ((MenuActivity)getActivity()).findViewById(R.id.toolbar);

        groupInfo();

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


                    String nameGroup = ((MenuActivity)getActivity()).getSupportActionBar().getTitle().toString();
                    ((MenuActivity)getActivity()).getSupportActionBar().setTitle(user);

                    ((MenuActivity)getActivity()).getSupportActionBar(toolbar);
                    toolbar.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try{

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

                            //transaction.replace(R.id.fragmentDetailGroup, EditGroupFragment.class, null);
                            transaction.replace(R.id.fragmentGroup, InfoGroupFragment.class, null);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
