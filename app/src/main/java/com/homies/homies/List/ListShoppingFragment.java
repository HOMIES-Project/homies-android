package com.homies.homies.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.homies.homies.R;
import com.homies.homies.groups.MenuActivity;

//*****************in development*****************
public class ListShoppingFragment extends Fragment {

    TextView haveTo, comprado;
    RecyclerView porcomprar, yacomprado;
    Button deleteShop;
    Dialog mDialog;
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View shop = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        haveTo = shop.findViewById(R.id.haveTo);
        comprado = shop.findViewById(R.id.comprado);
        porcomprar = shop.findViewById(R.id.porcomprar);
        yacomprado = shop.findViewById(R.id.yacomprado);
        deleteShop = shop.findViewById(R.id.deleteShop);
        mDialog = new Dialog(getContext());
        activity = getActivity();

        dialog();


        ((MenuActivity)getActivity()).getSupportActionBar().setTitle("Lista de Gastos");



        return shop;
    }

    public void dialog(){
        mDialog.setContentView(R.layout.popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }
}