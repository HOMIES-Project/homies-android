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
import androidx.recyclerview.widget.RecyclerView;

import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;

import org.w3c.dom.Text;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolderDatos> {

    GroupResponse groups ;
    Context context;

    public UserAdapter(){

    }

    public void setData(GroupResponse groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        UserData groupResponse = groups.getUserData().get(position);

        String textViewAdmin = groupResponse.getUser().getLogin();

        holder.textViewAdmin.setText(textViewAdmin);
        String photoString = groups.getUserdata().get(position).getPhoto();
        if (photoString != null) {
            byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView4.setImageBitmap(decodedByte);
        }
    }

    @Override
    public int getItemCount() {
        return groups.getUserData().size();
    }
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageView imageView4 ;
        TextView textViewAdmin ;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            imageView4 = itemView.findViewById(R.id.imageView4);
            textViewAdmin = itemView.findViewById(R.id.textViewAdmin);
        }

    }
/*
    Context context;




    public UserAdapter(Context context, ArrayList<GroupResponse> objects) {
        super(context, R.layout.user_item, objects);
        this.context = context;
        groups = groups;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.user_item, null);


            ImageView imageView4 = (ImageView) row.findViewById(R.id.imageView4);
            TextView textViewAdmin = (TextView) row.findViewById(R.id.textViewAdmin);

            textViewAdmin.setText(groups.get(position).getUserData().get(position).getUser().getLogin());
            String photoString = groups.get(position).getUserdata().get(position).getPhoto();
            if (photoString != null) {
                byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView4.setImageBitmap(decodedByte);
            }
        }
        return convertView;
    }*/


}
