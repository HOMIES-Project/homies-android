package com.homies.homies.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.homies.homies.R;


public class DetailGroupFragment extends Fragment {

    Button btnEditGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View main = inflater.inflate(R.layout.fragment_detail_group, container, false);

        btnEditGroup = main.findViewById(R.id.btn_editGroup);

        btnEditGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                //transaction.replace(R.id.fragmentDetailGroup, EditGroupFragment.class, null);
                transaction.replace(R.id.fragmentGroup, EditGroupFragment.class, null);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });


        return main;
    }
}
