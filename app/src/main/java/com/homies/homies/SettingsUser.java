package com.homies.homies;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;


public class SettingsUser extends Fragment {


    Button btn_delete, btnConfirm;
    Activity activity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settings = inflater.inflate(R.layout.fragment_settings_user, container, false);

        btn_delete = settings.findViewById(R.id.btn_delete);
        activity = getActivity();

        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Ajustes de Usuario");


        btn_delete.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(
                            R.layout.activity_layout_botton_deleteuser,
                            settings.findViewById(R.id.bottonDeleteUser)
                    );

            btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
            btnConfirm.setOnClickListener(view1 -> {


                    bottomSheetDialog.dismiss();

            });


            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return settings;
    }
}