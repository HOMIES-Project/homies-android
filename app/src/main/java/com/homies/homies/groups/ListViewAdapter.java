/*
    Code written by IJApps
    github.com/IJ-Apps
    Tutorial Series: https://www.youtube.com/watch?v=9nFGR8dIu_w&list=PLLmkb5CTw5rRsR6reO-ZkbE-QJF-GstwG
*/

package com.homies.homies.groups;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.homies.homies.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class ListViewAdapter extends ArrayAdapter<String> {
    ArrayList<String> list;
    Context context;

    // The ListViewAdapter Constructor
    // @param context: the Context from the MainActivity
    // @param items: The list of items in our Grocery List
    public ListViewAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.user_item, items);
        this.context = context;
        list = items;
    }

    // The method we override to provide our own layout for each View (row) in the ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.user_item, null);
            TextView name = convertView.findViewById(R.id.textViewAdmin);
            ImageButton remove = convertView.findViewById(R.id.delete);
            ImageView admin = convertView.findViewById(R.id.admin);
            name.setText(list.get(position));


        }
        return convertView;
    }

}
