package com.homies.homies.retrofit.api;

import com.homies.homies.retrofit.model.group.AddUser;
import com.homies.homies.retrofit.model.group.ChangeAdmin;
import com.homies.homies.retrofit.model.user.ChangePass;
import com.homies.homies.retrofit.model.user.DeleteUser;
import com.homies.homies.retrofit.model.group.GroupRequest;
import com.homies.homies.retrofit.model.group.GroupResponse;
import com.homies.homies.retrofit.model.group.LeaveGroup;
import com.homies.homies.retrofit.model.user.UserData;
import com.homies.homies.retrofit.model.user.UserRequest;
import com.homies.homies.retrofit.model.user.UserResponse;
import com.homies.homies.retrofit.model.tasks.AddUserTask;
import com.homies.homies.retrofit.model.tasks.CreateNewTask;
import com.homies.homies.retrofit.model.tasks.DeleteUserTask;
import com.homies.homies.retrofit.model.tasks.TaskListResponse;
import com.homies.homies.retrofit.model.tasks.UpdateTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("authenticate")
    Call<UserResponse> loginUser(@Body UserRequest userRequest);

    @POST("register")
    Call<Void> saveUser(@Body UserRequest userRequest);

    @POST("account/change-password")
    Call<Void> changePassword(@Header("Authorization")String authHeader,@Body ChangePass changePass);

    @POST("account/reset-password/init")
    Call<Void> resetPassword(@Body String email);

    @POST("groups")
    Call<GroupResponse> saveGroup(@Header("Authorization")String authHeader, @Body GroupRequest groupRequest);

    @GET("user-data/{id}")
    Call<UserData> userInfo(@Header("Authorization")String authHeader, @Path("id") int userId);

    @PUT("user-data/{id}")
    Call<UserData> updateInfo(@Header("Authorization")String authHeader,@Path("id") int userId, @Body UserRequest userRequest);

    @DELETE("user-data/{id}")
    Call<UserResponse> deleteUser(@Header("Authorization")String authHeader,@Path("id") int userId);

    @GET("groups/{id}")
    Call<GroupResponse> groupInfo(@Header("Authorization")String authHeader,@Path("id") int userId);

    @GET("groups/{id}")
    Call<GroupResponse> groupPhoto(@Header("Authorization")String authHeader, @Path("id") int userId);

    @POST("groups/add-user")
    Call<GroupResponse> addUserGroup(@Header("Authorization") String authHeader, @Body AddUser addUser);

    @POST("groups/delete-user")
    Call<GroupResponse> deleteUserGroup(@Header("Authorization") String authHeader,@Body DeleteUser deleteUser);

    @POST("groups/delete-user")
    Call<GroupResponse> leaveUserGroup(@Header("Authorization") String authHeader, @Body LeaveGroup leaveGroup);

    @DELETE("groups/{id}")
    Call<GroupResponse> deleteGroup(@Header("Authorization")String authHeader,@Path("id") int userId);

    @POST("groups/change-admin")
    Call<GroupResponse> changeAdmin(@Header("Authorization")String authHeader,@Body ChangeAdmin changeAdmin);

    @PUT("groups/{id}")
    Call<GroupResponse> updateInfoGroup(@Header("Authorization")String authHeader,@Path("id") int userId, @Body GroupRequest groupRequest);


    //task funtions(in development)

    @POST("tasks")
    Call<TaskListResponse> createNewTask(@Header("Authorization") String authHeader, @Body CreateNewTask createNewTask);

    @POST("tasks/add-user")
    Call<TaskListResponse> addUserTask(@Header("Authorization") String authHeader, @Body AddUserTask addUserTask);

    @DELETE("task/delete-user")
    Call<TaskListResponse> deleteUserTask(@Header("Authorization") String authHeader, @Body DeleteUserTask deleteUserTask);

    @DELETE("/task/delete-task/{id}")
    Call<Void> deleteTask(@Header("Authorization") String authHeader, @Path("id") int userId);

    @GET("task-lists")
    Call<List<TaskListResponse>> getAllTask(@Header("Authorization") String authHeader);

    @GET("task-lists/{id}")
    Call<TaskListResponse> getTask(@Header("Authorization") String authHeader);

    @GET("task-lists-user/{id}/{login}")
    Call<List<TaskListResponse>> getUserTasks(@Header("Authorization") String authHeader, @Path("id") int userId, @Path("login") String login);

    @PUT("tasks/update-tasks")
    Call<TaskListResponse> updateTasks(@Header("Authorization") String authHeader, @Body UpdateTask updateTask);


}
