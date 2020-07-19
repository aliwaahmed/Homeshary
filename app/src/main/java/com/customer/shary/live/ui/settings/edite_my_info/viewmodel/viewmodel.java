package com.customer.shary.live.ui.settings.edite_my_info.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customer.shary.live.R;
import com.customer.shary.live.auth.ConfirmData.counteryapi;
import com.customer.shary.live.ui.home.datamodel.apidata;
import com.customer.shary.live.ui.settings.edite_my_info.model.userdatamodel;

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

import static android.content.Context.MODE_PRIVATE;

public class viewmodel extends ViewModel {

    private MutableLiveData<userdatamodel> data;
    private Context context;
    private SharedPreferences prefs;
    private userdatamodel userdatamodel;


    public LiveData<userdatamodel> load(Context context) {
        if (data == null) {
            this.context = context;
            prefs = context.getSharedPreferences("login", MODE_PRIVATE);
            data = new MutableLiveData<>();
            userdatamodel = new userdatamodel();
            loadhttp();
        }

        return data;

    }


    private void loadhttp() {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://shary.live/api/v1/user/data/" + prefs.getString("login_id", "-2"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String res = response.body().string();
                Log.e("res", res);

                JSONObject jsonObject = null;
                try {


                    jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    userdatamodel.setName(jsonArray.getJSONObject(0).getString("name"));
                    userdatamodel.setEmail(jsonArray.getJSONObject(0).getString("email"));
                    userdatamodel.setAddr(jsonArray.getJSONObject(0).getString("address"));
                    userdatamodel.setImg(jsonArray.getJSONObject(0).getString("image"));
                    userdatamodel.setPhone(jsonArray.getJSONObject(0).getString("phone"));
                    data.postValue(userdatamodel);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


}
