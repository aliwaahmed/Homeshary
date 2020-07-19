package com.customer.shary.live.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.ui.home.datamodel.apidata;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<apidata>> listvideos;
    //TODO:fav_rec PRODUCT
    private ArrayList<String> img;
    private ArrayList<String> list;
    private apidata m_apimodel;
    private MediaObject mediaObjects;
    private ArrayList<apidata> Objects;


    public LiveData<ArrayList<apidata>> getvideos(Context context) {
        if (listvideos == null) {
            listvideos = new MutableLiveData<ArrayList<apidata>>();
            Objects = new ArrayList<>();
            loadUsers(context);
        }
        return listvideos;
    }

    private void loadUsers(Context context) {


        SharedPreferences prefs;
        prefs = context.getSharedPreferences("login", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/products?user_id=" + prefs.getString("login_id", "2"))
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

                        if ((jsonArray.getJSONObject(i).getJSONArray("video").length() > 0)) {

                            img = new ArrayList<>();
                            m_apimodel = new apidata();
                            list = new ArrayList<>();

                            if (i == 0) {
                                m_apimodel.setMediatype(1);
                            }
                            m_apimodel.setMediatype(0);
                            m_apimodel.setUser_rate(jsonArray.getJSONObject(i).getString("user_rate"));
                            Log.e("product_link", jsonArray.getJSONObject(i).getString("product_link"));
                            m_apimodel.setProduct_link(jsonArray.getJSONObject(i).getString("product_link"));
                            m_apimodel.setProduct_id(jsonArray.getJSONObject(i).getInt("id"));
                            m_apimodel.setName(jsonArray.getJSONObject(i).getString("name"));
                            m_apimodel.setViews(jsonArray.getJSONObject(i).getString("views"));
                            m_apimodel.setDetails(jsonArray.getJSONObject(i).getString("details"));
                            m_apimodel.setCategory(jsonArray.getJSONObject(i).getString("category"));
                            m_apimodel.setStore_name(jsonArray.getJSONObject(i).getString("store_name"));
                            m_apimodel.setVideo_link(jsonArray.getJSONObject(i).getString("video_link") + jsonArray.getJSONObject(i).getJSONArray("video").get(0));
                            m_apimodel.setLikes(jsonArray.getJSONObject(i).getString("likes"));
                            m_apimodel.setUser_store_follow(jsonArray.getJSONObject(i).getString("user_store_follow"));
                            m_apimodel.setUser_share(jsonArray.getJSONObject(i).getString("user_share"));
                            m_apimodel.setComment_number(jsonArray.getJSONObject(i).getInt("comments"));
                            m_apimodel.setSeller_id(jsonArray.getJSONObject(i).getString("user_id"));
                            m_apimodel.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            m_apimodel.setNew_price(jsonArray.getJSONObject(i).getString("new_price"));
                            m_apimodel.setUser_like(jsonArray.getJSONObject(i).getString("user_like"));
                            m_apimodel.setStore_image(jsonArray.getJSONObject(i).getString("store_image"));
                            m_apimodel.setImage_link(jsonArray.getJSONObject(i).getString("video_thumb"));
                            m_apimodel.setShare(jsonArray.getJSONObject(i).getString("share"));
                            m_apimodel.setUser_store_follow(String.valueOf(jsonArray.getJSONObject(i).getBoolean("user_store_follow")));
                            m_apimodel.setQuantity(String.valueOf(jsonArray.getJSONObject(i).optInt("quantity")));
                            m_apimodel.setRate_count(String.valueOf(jsonArray.getJSONObject(i).getInt("rate")));
                            m_apimodel.setSeller_room(jsonArray.getJSONObject(i).getString("seller_room"));
                            Log.e("seller", jsonArray.getJSONObject(i).getString("seller_room"));
                            m_apimodel.setVideo(list);
                            for (int x = 0; x < jsonArray.getJSONObject(i).getJSONArray("video").length(); x++) {

                                list.add(m_apimodel.getVideo_link() + jsonArray.getJSONObject(i).getJSONArray("video").get(x));
                            }
                            mediaObjects = new MediaObject("",
                                    m_apimodel.getVideo_link()
                                    , m_apimodel.getImage_link(), "");
                            m_apimodel.setMediaObject(mediaObjects);
                            Objects.add(m_apimodel);


                            listvideos.postValue(Objects);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }
}