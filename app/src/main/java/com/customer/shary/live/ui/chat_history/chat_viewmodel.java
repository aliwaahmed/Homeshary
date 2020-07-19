package com.customer.shary.live.ui.chat_history;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class chat_viewmodel extends ViewModel {

    private MutableLiveData<ArrayList<chat_history_user>> users;
    private ArrayList<chat_history_user> users_list;
    private chat_history_user history_user;
    private Context context;


    public LiveData<ArrayList<chat_history_user>> getText(Context context) {
        if (users == null) {
            users_list = new ArrayList<>();
            users = new MutableLiveData<>();
            this.context = context;

            load_users();
            return users;
        } else {
            return users;

        }
    }

    public void load_users() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/chats/" + sharedPreferences.getString("login_id", "-1"))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                String res = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        history_user = new chat_history_user();
                        history_user.setAdmin_id(String.valueOf(jsonArray.getJSONObject(i).getInt("admin_id")));
                        history_user.setAdmin_name(jsonArray.getJSONObject(i).getString("admin_name"));
                        history_user.setAdmin_image(jsonArray.getJSONObject(i).getString("admin_image"));
                        history_user.setLatest_message(jsonArray.getJSONObject(i).getString("latest_message"));
                        history_user.setRoom(jsonArray.getJSONObject(i).getString("room"));
                        history_user.setBlocked(jsonArray.getJSONObject(i).getInt("block"));
                        history_user.setSaller_id(jsonArray.getJSONObject(i).getString("seller_id"));
                        users_list.add(history_user);


                    }


                    users.postValue(users_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });


    }


}