package com.customer.shary.live.favourite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.R;
import com.customer.shary.live.favourite.recyclerview.fav_adapter;
import com.customer.shary.live.favourite.recyclerview.favmodel;

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

public class favourite_Activity extends AppCompatActivity {


    //RecyclerView
    private RecyclerView fav_rec;
    private LinearLayoutManager linearLayoutManager_related;



    //TODO:fav_rec PRODUCT
    private ArrayList<String> img;
    private ArrayList<String> list;
    private favmodel favmodel;
    private MediaObject mediaObjects;
    private ArrayList<favmodel> Objects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_);
        ImageView backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fav_rec = findViewById(R.id._orders_recycler);
        linearLayoutManager_related = new LinearLayoutManager(this);
        fav();

    }



    public void fav() {
        Objects = new ArrayList<>();

        SharedPreferences prefs;
        prefs = getSharedPreferences("login", MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(" https://shary.live/api/v1/user-favorites/" + prefs.getString("login_id", "-1"))
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
                            favmodel = new favmodel();
                            list = new ArrayList<>();

                            if (i == 0) {
                                favmodel.setMediatype(1);
                            }

                            favmodel.setMediatype(0);
                            favmodel.setUser_rate(jsonArray.getJSONObject(i).getString("user_rate"));
                            favmodel.setProduct_link(jsonArray.getJSONObject(i).getString("product_link"));
                            favmodel.setProduct_id(jsonArray.getJSONObject(i).getInt("id"));
                            favmodel.setName(jsonArray.getJSONObject(i).getString("name"));
                            favmodel.setViews(jsonArray.getJSONObject(i).getString("views"));
                            favmodel.setDetails(jsonArray.getJSONObject(i).getString("details"));
                            favmodel.setCategory(jsonArray.getJSONObject(i).getString("category"));
                            favmodel.setStore_name(jsonArray.getJSONObject(i).getString("store_name"));
                            favmodel.setVideo_link(jsonArray.getJSONObject(i).getString("video_link") + jsonArray.getJSONObject(i).getJSONArray("video").get(0));
                            favmodel.setLikes(jsonArray.getJSONObject(i).getString("likes"));
                            favmodel.setUser_store_follow(jsonArray.getJSONObject(i).getString("user_store_follow"));
                            favmodel.setUser_share(jsonArray.getJSONObject(i).getString("user_share"));
                            favmodel.setComment_number(jsonArray.getJSONObject(i).getInt("comments"));
                            favmodel.setSeller_id(jsonArray.getJSONObject(i).getString("user_id"));
                            favmodel.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            favmodel.setNew_price(jsonArray.getJSONObject(i).getString("new_price"));
                            favmodel.setUser_like(jsonArray.getJSONObject(i).getString("user_like"));
                            favmodel.setStore_image(jsonArray.getJSONObject(i).getString("store_image"));
                            favmodel.setImage_link(jsonArray.getJSONObject(i).getString("video_thumb"));
                            favmodel.setShare(jsonArray.getJSONObject(i).getString("share"));
                            favmodel.setQuantity(String.valueOf(jsonArray.getJSONObject(i).optInt("quantity")));
                            favmodel.setRate_count(String.valueOf(jsonArray.getJSONObject(i).getInt("rate")));
                            favmodel.setVideo(list);

                            for (int x = 0; x < jsonArray.getJSONObject(i).getJSONArray("video").length(); x++) {

                                list.add(favmodel.getVideo_link() + jsonArray.getJSONObject(i).getJSONArray("video").get(x));
                            }

                            mediaObjects = new MediaObject("",
                                    favmodel.getVideo_link()
                                    , favmodel.getImage_link(), "");
                            favmodel.setMediaObject(mediaObjects);
                            Objects.add(favmodel);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fav_adapter recycler = new fav_adapter(getApplicationContext(), Objects);
                                    fav_rec.setLayoutManager(linearLayoutManager_related);
                                    fav_rec.setAdapter(recycler);

                                }
                            });

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Favourite_ERROR", e.getMessage().toString());
                }

            }
        });
    }
}
