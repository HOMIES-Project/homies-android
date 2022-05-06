package com.homies.homies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserData> {


    private Context context;
    private GroupResponse groups;
    public UserAdapter(@NonNull Context context, int resource, @NonNull List<UserData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.user_item,parent,false);


        ImageView imageView4 = (ImageView) row.findViewById(R.id.imageView3);
        TextView textViewAdmin = (TextView) row.findViewById(R.id.textViewAdmin);

        textViewAdmin.setText(groups.getUserData().get(position).getUser().getLogin());
        String photoString = groups.getUserdata().get(position).getPhoto();
        if(photoString != null){
            byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView4.setImageBitmap(decodedByte);
        }

        return row;
    }
}
