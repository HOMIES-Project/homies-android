package com.homies.homies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homies.homies.groups.InfoGroupFragment;
import com.homies.homies.groups.ListsGroupFragment;
import com.homies.homies.groups.MenuActivity;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.ToDoModel;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.tasks.TaskListResponse;
import com.homies.homies.retrofit.model.tasks.UserTasksListModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    TaskListResponse taskList ;
    Context context;
    ClickedTask clickedTask;

    ListsGroupFragment listsGroupFragment = new ListsGroupFragment();


    public ToDoAdapter( ClickedTask clickedTask) {
        this.clickedTask = clickedTask;
    }
    public void setData(GroupResponse groups) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        SharedPreferences preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);

    }

    public interface ClickedTask{

    }

    public void setOnItemClickListener(ClickedTask listener) {
        clickedTask = listener;
    }

    @Override
    public int getItemCount() {
        //Aqui hay que poner el modal correcto
        return taskList.getUserAssigneds().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView taskMember;
        ImageView trashButtonTask;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            taskMember = view.findViewById(R.id.taskMember);
            trashButtonTask = view.findViewById(R.id.trashButtonTask);
        }
    }
}