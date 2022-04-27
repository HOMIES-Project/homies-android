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


public class ListGroupFragment extends Fragment {

    Button btnEditGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View info = inflater.inflate(R.layout.fragment_list_group, container, false);

        groupInfo();

        return info;
    }
    public void groupInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupInfo("Bearer " + retrivedToken,userId);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse adslist= response.body();

                    String user = adslist.getGroupName();
                    ((MenuActivity)getActivity()).getSupportActionBar().setTitle(user);
                    ImageButton b1=new ImageButton(((MenuActivity)getActivity()));
                    b1.setImageResource(R.drawable.ic_info);
                    Toolbar.LayoutParams l3=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
                    l3.gravity= Gravity.END;
                    b1.setLayoutParams(l3);
                    ((MenuActivity)getActivity()).toolbar.addView(b1);
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
