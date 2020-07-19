package com.customer.shary.live.ui.live.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customer.shary.live.notification.datamodel;
import com.customer.shary.live.ui.live.datamodel.livemodel;

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

public class viewmodel  extends ViewModel {

    private MutableLiveData<ArrayList<livemodel>> listMutableLiveData;
    private livemodel datamodel ;
    private ArrayList<livemodel> list=new ArrayList<>();
    private Context context;


    public LiveData<ArrayList<livemodel>> getText(Context context) {
        if (listMutableLiveData == null) {
            list = new ArrayList<>();
            listMutableLiveData = new MutableLiveData<>();
            this.context = context;

            load_users();
            return listMutableLiveData;
        } else {
            return listMutableLiveData;

        }
    }

    private void load_users() {

        SharedPreferences sharedPreferences =context.getSharedPreferences("login", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/online-broadcasts")
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

                    if(jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            datamodel = new livemodel();
                            datamodel.setId(String.valueOf(jsonArray.getJSONObject(i).getInt("id")));
                            datamodel.setRoom(jsonArray.getJSONObject(i).getString("room"));
                            datamodel.setSeller_name(jsonArray.getJSONObject(i).getString("seller_name"));
                            datamodel.setSeller_image(jsonArray.getJSONObject(i).getString("seller_image"));
                            list.add(datamodel);

                        }

                        listMutableLiveData.postValue(list);
                    }
                    else
                    {
                        listMutableLiveData.postValue(list);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }
}
