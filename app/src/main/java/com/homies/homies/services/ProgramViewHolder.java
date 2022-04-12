package com.homies.homies.services;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.homies.homies.R;

public class ProgramViewHolder {

    ImageView itemImage;
    TextView  groupTitle;

    ProgramViewHolder( View v){
        itemImage = v.findViewById(R.id.imageViewGroup);
        groupTitle = v.findViewById(R.id.textViewGroup);
    }
}
