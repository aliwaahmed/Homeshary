package com.customer.shary.live.googlemaps;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class mViewmodelmap  extends ViewModel {

    private MutableLiveData <Store_map_model> data;
    private Store_map_model model ;


    public LiveData<Store_map_model> getdata(String Saller_id, Context context)
    {
        if(data==null)
        {
            data=new MutableLiveData<>();
            model =new Store_map_model();
            get_store_data(Saller_id);
            return data;
        }

        return data;
    }





    /**
     * @param Store_id
     */
    private  void get_store_data(String Store_id)
    {

        OkHttpClient client =new OkHttpClient();

        Request request =new Request.Builder().url("https://shary.live/api/v1/seller/dataMarket/"+Store_id).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {


            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                String res =response.body().string() ;

                Log.e("res",res);
                JSONObject  jsonObject = null;
                try {
                    jsonObject = new JSONObject(res);
                    JSONArray  jsonArray  =jsonObject.getJSONArray("data");
                    model.setLatitude(jsonArray.getJSONObject(0).getString("lat"));
                    model.setLongitude(jsonArray.getJSONObject(0).getString("log"));
                    model.setStore_name(jsonArray.getJSONObject(0).getString("store_name"));
                    model.setStore_description(jsonArray.getJSONObject(0).getString("store_description"));
                    model.setStore_image(jsonArray.getJSONObject(0).getString("image"));
                    data.postValue(model);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
