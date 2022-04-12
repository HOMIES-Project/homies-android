package com.homies.homies.services;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homies.homies.R;

public class GroupListAdapter extends ArrayAdapter {
    String[] groupNames;
    int[] imageViewGroup;
    Context context;

    public GroupListAdapter(Context context, String[] groupNames,  int[] imageid) {
        super(context, R.layout.list_item,R.id.textViewGroup ,groupNames);
        this.context = context;
        this.groupNames = groupNames;
        this.imageViewGroup = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolder holder = null;


        //LayoutInflater inflater = context.getLayoutInflater();
        if(singleItem == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);

        }else {
            holder = (ProgramViewHolder) singleItem.getTag();
        }

        holder.itemImage.setImageResource(imageViewGroup[position]);
        holder.groupTitle.setText(groupNames[position]);
        singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"You cliked" + groupNames[position],Toast.LENGTH_SHORT).show();
            }
        });

        return  singleItem;


        /*    row = inflater.inflate(R.layout.list_item, null, true);
        TextView textViewGroup = (TextView) row.findViewById(R.id.textViewGroup);
        ImageView imageViewGroup = (ImageView) row.findViewById(R.id.imageViewGroup);

        textViewGroup.setText(groupNames[position]);
        imageViewGroup.setImageResource(imageViewGroup[position]);*/

    }
}