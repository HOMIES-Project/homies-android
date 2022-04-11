package com.homies.homies.services;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homies.homies.R;

public class GroupListAdapter extends ArrayAdapter {
    private String[] groupNames;
    private final Integer[] imageid;
    private final Activity context;

    public GroupListAdapter(Activity context, String[] groupNames,  Integer[] imageid) {
        super(context, R.layout.list_item, groupNames);
        this.context = context;
        this.groupNames = groupNames;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.list_item, null, true);
        TextView textViewGroup = (TextView) row.findViewById(R.id.textViewGroup);
        ImageView imageViewGroup = (ImageView) row.findViewById(R.id.imageViewGroup);

        textViewGroup.setText(groupNames[position]);
        imageViewGroup.setImageResource(imageid[position]);
        return  row;
    }
}