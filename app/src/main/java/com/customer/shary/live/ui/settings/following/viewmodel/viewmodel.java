package com.customer.shary.live.ui.settings.following.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;
import com.customer.shary.live.ui.settings.following.datamodel.follow_data_model;

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

public class viewmodel extends ViewModel {


    private MutableLiveData<ArrayList<follow_data_model>> saller_followed;
    private ArrayList <follow_data_model> arrayList ;
    private follow_data_model model;
    private Context context;

    public LiveData<ArrayList<follow_data_model>> getfollowed(Context context) {
        if(saller_followed==null)
        {
            saller_followed =new MutableLiveData<>();
            arrayList=new ArrayList<>();
            this.context =context;

            Loaddata();

            return saller_followed;

        }
       else
        {
            return saller_followed;
        }
    }

    private void Loaddata() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.jsonbin.io/b/5e36986e50a7fe418c582d41/1")
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

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        model=new follow_data_model();
                        model.setImg(jsonArray.getJSONObject(i).getString("img"));
                        model.setDescription(jsonArray.getJSONObject(i).getString("last_msg"));
                        model.setName(jsonArray.getJSONObject(i).getString("name"));


                        arrayList.add(model);

                    }
                    saller_followed.postValue(arrayList);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });



    }
}
