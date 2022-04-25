package com.homies.homies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homies.homies.services.AddUserGroupResponse;

import java.util.ArrayList;

public class RecyclerViewAdapterListUser extends RecyclerView.Adapter<RecyclerViewAdapterListUser.ViewHolder> {

    private LayoutInflater inflater;
    ArrayList<AddUserGroupResponse> data;

    public RecyclerViewAdapterListUser(Context context, ArrayList<AddUserGroupResponse> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewAdmin.setText(data.get(position).getUserAdmin().getId());
        holder.textViewUser.setText((data.get(position).getUserData().getId()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUser, textViewAdmin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewAdmin = itemView.findViewById(R.id.textViewAdmin);
            textViewUser = itemView.findViewById(R.id.textViewUser);
        }
    }

}
