package com.homies.homies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupFunctions extends Fragment {


    public GroupFunctions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View task = inflater.inflate(R.layout.fragment_group_functions, container, false);

        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Ajustes de Usuario");
        return task;
    }
}