package com.homies.homies.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homies.homies.List.ListsTaskFragment;
import com.homies.homies.R;
import com.homies.homies.retrofit.model.tasks.TaskListResponse;


//adapter to view List Task (in development)
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    TaskListResponse taskList ;
    Context context;
    ClickedTask clickedTask;

    ListsTaskFragment listsTaskFragment = new ListsTaskFragment();


    public ToDoAdapter( ClickedTask clickedTask) {
        this.clickedTask = clickedTask;
    }

    public void setData(TaskListResponse taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_item, null, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        SharedPreferences preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        int userId  = preferences.getInt("USER_ID",0);

    }

    public interface ClickedTask{
        void ClickedTaskUser(TaskListResponse taskListResponse);
        void Clicked(TaskListResponse taskListResponse);
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