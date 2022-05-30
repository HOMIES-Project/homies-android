package com.homies.homies.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homies.homies.R;
import com.homies.homies.groups.InfoGroupFragment;
import com.homies.homies.retrofit.model.group.GroupResponse;
import com.homies.homies.retrofit.model.user.UserData;

//adapter to view group members
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolderDatos> {

    GroupResponse groups ;
    Context context;
    ClickedItem clickedItem;

    InfoGroupFragment infoGroupFragment = new InfoGroupFragment();

    public UserAdapter(ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
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

        SharedPreferences preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);

        UserData groupResponse = groups.getUserData().get(position);

        String textViewAdmin = groupResponse.getUser().getLogin();

        holder.textViewAdmin.setText(textViewAdmin);
        String photoString = groups.getUserdata().get(position).getPhoto();

        if (groups.getUserData().get(position).getId() != groups.getUserAdmin().getId()){
            holder.admin.setVisibility(View.GONE);
        }

        if ( groups.getUserAdmin().getId() != userId){

            holder.delete.setVisibility(View.GONE);


        }else {
            holder.delete.setVisibility(View.VISIBLE);
        }
        if (photoString != null) {
            byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView4.setImageBitmap(decodedByte);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                preferences.edit().putString("MEMBERID", groupResponse.getUser().getLogin()).apply();
                clickedItem.ClickedUser(groupResponse);
            }
        });
        holder.textViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( groups.getUserAdmin().getId() == userId) {

                    clickedItem.Clicked(groupResponse);
                }
            }
        });
    }

    public interface ClickedItem{
        public void ClickedUser(UserData groupResponse);
        public void Clicked(UserData groupResponse);

    }
    public void setOnItemClickListener(ClickedItem listener) {
        clickedItem = listener;
    }

    @Override
    public int getItemCount() {
        return groups.getUserData().size();
    }
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageView imageView4 ;
        TextView textViewAdmin ;
        ImageView delete;
        ImageView admin;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            imageView4 = itemView.findViewById(R.id.imageView4);
            textViewAdmin = itemView.findViewById(R.id.textViewAdmin);
            delete = itemView.findViewById(R.id.delete);
            admin = itemView.findViewById(R.id.admin);
        }

    }

}
