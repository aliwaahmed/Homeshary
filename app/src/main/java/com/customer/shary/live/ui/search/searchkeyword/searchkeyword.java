package com.customer.shary.live.ui.search.searchkeyword;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.ui.home.datamodel.apidata;

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

public class searchkeyword extends ViewModel {



    private MutableLiveData<ArrayList<data>> listvideos;
    private ArrayList<data> list ;



    private MutableLiveData<ArrayList<data>> cat;
    private ArrayList<data> catlist ;
    private String res;

    public LiveData<ArrayList<data>> getcat(Context context) {
        if (cat == null) {
            cat = new MutableLiveData<ArrayList<data>>();
            catlist=new ArrayList<>();
        }
        return cat;
    }



    public LiveData<ArrayList<data>> getkeyword(Context context) {
        if (listvideos == null) {
            listvideos = new MutableLiveData<ArrayList<data>>();
            list=new ArrayList<>();
            loadUsers(context);
        }
        return listvideos;
    }

    private void loadUsers(Context context) {



        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/products/search_keys")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                 res = response.body().string();
                data data ;

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        data =new data();
                        data.setKey(jsonArray.get(i).toString());
                        data.setCategories(jsonArray.get(i).toString());

                        list.add(data);



                    }

                    load_cat();
                    listvideos.postValue(list);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }

    public void load_cat()
    {
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("categories");
            data data ;

            for (int i = 0; i < jsonArray.length(); i++) {
                data =new data();
                data.setKey(jsonArray.get(i).toString());
                data.setCategories(jsonArray.get(i).toString());

                catlist.add(data);
                cat.postValue(catlist);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

