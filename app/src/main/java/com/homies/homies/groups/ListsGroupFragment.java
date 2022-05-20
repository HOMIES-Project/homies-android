package com.homies.homies.groups;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.homies.homies.R;
import com.homies.homies.ToDoAdapter;
import com.homies.homies.retrofit.config.NetworkConfig;
import com.homies.homies.retrofit.model.AddUser;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.ToDoModel;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.tasks.AddUserTask;
import com.homies.homies.retrofit.model.tasks.CreateNewTask;
import com.homies.homies.retrofit.model.tasks.TaskListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;


public class ListsGroupFragment extends Fragment {

    Button btnEditGroup, btnAddTask, btnCreateTask, btnCancelTask;
    Toolbar toolbar;
    EditText userTask, descriptionTask;
    Spinner spinner;
    RecyclerView toDoList;
    private ToDoAdapter tasksAdapter;
    Activity activity;
    Context context;

    ToDoAdapter.ClickedTask clickedTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View info = inflater.inflate(R.layout.fragment_lists_group, container, false);
        toolbar = ((MenuActivity)getActivity()).findViewById(R.id.toolbar);
        toDoList = info.findViewById(R.id.toDoList);
        activity = getActivity();
        context = getActivity().getApplicationContext();
        groupInfo();


        
        //toDoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //tasksAdapter = new ToDoAdapter(clickedTask);
        //toDoList.setAdapter(tasksAdapter);

        //events within the listview
        //tasksAdapter.setOnItemClickListener(new ToDoAdapter.ClickedTask() {});

        btnAddTask = info.findViewById(R.id.btn_addTask);

        btnAddTask.setOnClickListener((View.OnClickListener) view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    getActivity(), R.style.BottonSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(getContext())
                    .inflate(
                            R.layout.dialog_add_task,
                            info.findViewById(R.id.addTask)
                    );
            userTask = bottomSheetView.findViewById(R.id.userTask);
            btnCreateTask = bottomSheetView.findViewById(R.id.btnCreateTask);
            btnCancelTask = bottomSheetView.findViewById(R.id.btnCancelTask);
            spinner = bottomSheetView.findViewById(R.id.spinnerTask);

            spinnerList();

            groupUsers(userListTask());
            addTask(createNewTask());


            descriptionTask = bottomSheetView.findViewById(R.id.descriptionTask);

            btnCreateTask.setOnClickListener(view1 -> {

                if (spinner.toString().trim().isEmpty()) {
                    String message = getString(R.string.val_user);
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                } else {


                    bottomSheetDialog.dismiss();
                }
            });
            btnCancelTask.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return info;

    }





    public void groupInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        int userId  = preferences.getInt("USER_ID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupInfo("Bearer " + retrivedToken, idGroup);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse adslist= response.body();
                    String user = adslist.getGroupName();


                    try{

                        ((MenuActivity)getActivity()).getSupportActionBar().setTitle(user);
                        ((MenuActivity)getActivity()).getSupportActionBar(toolbar);
                    } catch (Exception e) {
                    e.printStackTrace();
                }

                    toolbar.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try{

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setReorderingAllowed(true);

                            //transaction.replace(R.id.fragmentDetailGroup, EditGroupFragment.class, null);
                            transaction.replace(R.id.fragmentGroup, InfoGroupFragment.class, null);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void spinnerList() {

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);
        int userId  = preferences.getInt("USER_ID",0);
        Integer idGroup  = preferences.getInt("GROUPID",0);
        Call<GroupResponse> groupResponseCall = NetworkConfig.getService().groupInfo("Bearer " + retrivedToken,idGroup);
        groupResponseCall.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> groupResponseCall, Response<GroupResponse> response) {

                if (response.isSuccessful()) {

                    List<UserData> myGroupList = response.body().getUserdata();
                    String[] oneGroup = new String[myGroupList.size()];
                    Integer[] numberGroup = new Integer[myGroupList.size()];

                    for (int i = 0; i < myGroupList.size(); i++) {
                        oneGroup[i] = myGroupList.get(i).getUser().getLogin();
                        numberGroup[i] = myGroupList.get(i).getId();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, oneGroup);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                }else {
                    String message = getString(R.string.error_login);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> groupResponseCall, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

            }

        });
    }

    //*****************in development*****************
    public void groupUsers(AddUserTask addUserTask) {
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken = preferences.getString("TOKEN", null);


        Call<TaskListResponse> call = NetworkConfig.getService().addUserTask("Bearer " + retrivedToken, addUserTask);
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                userListTask();


                /*String userAssigned = userListTask().getLogin();
                response.body().setUserAssigneds(userAssigned);

                ArrayAdapter<String> adapter = new ArrayAdapter (getContext(),android.R.layout.simple_spinner_item, user);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listUserTask.setAdapter(adapter);*/
            }

            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //*****************in development*****************
    public void addTask(CreateNewTask createNewTask){
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken = preferences.getString("TOKEN", null);

        Call<TaskListResponse> taskListResponseCall = NetworkConfig.getService().createNewTask("Bearer " + retrivedToken, createNewTask);

        taskListResponseCall.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(Call<TaskListResponse> call, Response<TaskListResponse> response) {
                if (response.isSuccessful()) {
                    String message = "Tarea añadida";
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                   // <TaskListResponse> data = response.body();

                   // RecyclerViewAdapterListUser adapterListUser = new RecyclerViewAdapterListUser(getContext(), data);
                    //userList.setAdapter(adapterListUser);
                } else {
                    String message = getString(R.string.error_login);
                    makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<TaskListResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public AddUserTask userListTask(){

        AddUserTask addUserTask = new AddUserTask();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);

        int idTask  = preferences.getInt("ID_TASK",0);
        String login = preferences.getString("NAME_USER", null);
        int idList = preferences.getInt("ID_LIST", 0);

        addUserTask.setIdTask(idTask);
        addUserTask.setLogin(login);
        addUserTask.setIdList(idList);

        return addUserTask;
    }

    public CreateNewTask createNewTask (){
        CreateNewTask createNewTask = new CreateNewTask();
        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);

        int user = preferences.getInt("USER_TASK", 0);
        int idGroup = preferences.getInt("ID_GROUP_TASK", 0);
        String taskName = preferences.getString("TASK_NAME", null);
        String description = preferences.getString("TASK_DESCRIPTION", null);

        createNewTask.setUser(user);
        createNewTask.setIdGroup(idGroup);
        createNewTask.setTaskName(taskName);
        createNewTask.setDescription(description);

        return createNewTask;
    }




}
