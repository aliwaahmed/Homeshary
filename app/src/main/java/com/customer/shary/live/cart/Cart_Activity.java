package com.customer.shary.live.cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.R;
import com.customer.shary.live.auth.main;
import com.customer.shary.live.cart.recyclerview.cartRecycler;
import com.customer.shary.live.cart.recyclerview.cartmodel;
import com.customer.shary.live.payment.payment;

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

public class Cart_Activity extends AppCompatActivity implements cart_sumation {


    //RecyclerView
    private RecyclerView Cart;
    private LinearLayoutManager linearLayoutManager_related;


    //TODO:Cart PRODUCT
    private ArrayList<String> img;
    private ArrayList<String> list;
    private cartmodel cartmodel;
    private MediaObject mediaObjects;
    private ArrayList<cartmodel> Objects;


    private ImageView backbtn;
    public static LinearLayout buy,empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);

        empty =findViewById(R.id.empty);

        buy=findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences =
                        getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {


                    Intent in = new Intent(getApplicationContext(), payment.class);

                    in.putExtra("products", "1");
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }
            }
        });

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Cart = findViewById(R.id.cart_recycler);
        linearLayoutManager_related = new LinearLayoutManager(this);


        Load_Cart();

    }

    public void Load_Cart() {
        Objects = new ArrayList<>();

        SharedPreferences prefs;
        prefs = getSharedPreferences("login", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/cart_products/" + prefs.getString("login_id", "-1"))
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

                    if(jsonArray.length()<=0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                empty.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                empty.setVisibility(View.GONE);
                            }
                        });

                    }

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if ((jsonArray.getJSONObject(i).getJSONArray("video").length() > 0)) {

                            img = new ArrayList<>();
                            cartmodel = new cartmodel();
                            list = new ArrayList<>();

                            if (i == 0) {
                                cartmodel.setMediatype(1);
                            }

                            cartmodel.setMediatype(0);
                            cartmodel.setUser_rate(jsonArray.getJSONObject(i).getString("user_rate"));
                            cartmodel.setProduct_link(jsonArray.getJSONObject(i).getString("product_link"));
                            cartmodel.setProduct_id(jsonArray.getJSONObject(i).getInt("id"));
                            cartmodel.setName(jsonArray.getJSONObject(i).getString("name"));
                            cartmodel.setViews(jsonArray.getJSONObject(i).getString("views"));
                            cartmodel.setDetails(jsonArray.getJSONObject(i).getString("details"));
                            cartmodel.setCategory(jsonArray.getJSONObject(i).getString("category"));
                            cartmodel.setStore_name(jsonArray.getJSONObject(i).getString("store_name"));
                            cartmodel.setVideo_link(jsonArray.getJSONObject(i).getString("video_link") + jsonArray.getJSONObject(i).getJSONArray("video").get(0));
                            cartmodel.setLikes(jsonArray.getJSONObject(i).getString("likes"));
                            cartmodel.setUser_store_follow(jsonArray.getJSONObject(i).getString("user_store_follow"));
                            cartmodel.setUser_share(jsonArray.getJSONObject(i).getString("user_share"));
                            cartmodel.setComment_number(jsonArray.getJSONObject(i).getInt("comments"));
                            cartmodel.setSeller_id(jsonArray.getJSONObject(i).getString("user_id"));
                            cartmodel.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            cartmodel.setNew_price(jsonArray.getJSONObject(i).getString("new_price"));
                            cartmodel.setUser_like(jsonArray.getJSONObject(i).getString("user_like"));
                            cartmodel.setStore_image(jsonArray.getJSONObject(i).getString("store_image"));
                            cartmodel.setImage_link(jsonArray.getJSONObject(i).getString("video_thumb"));
                            cartmodel.setShare(jsonArray.getJSONObject(i).getString("share"));
                            cartmodel.setQuantity(String.valueOf(jsonArray.getJSONObject(i).optInt("cart_product_count")));
                            cartmodel.setRate_count(String.valueOf(jsonArray.getJSONObject(i).getInt("rate")));
                            cartmodel.setVideo(list);
                            for (int x = 0; x < jsonArray.getJSONObject(i).getJSONArray("video").length(); x++) {

                                list.add(cartmodel.getVideo_link() + jsonArray.getJSONObject(i).getJSONArray("video").get(x));
                            }

                            mediaObjects = new MediaObject("",
                                    cartmodel.getVideo_link()
                                    , cartmodel.getImage_link(), "");
                            cartmodel.setMediaObject(mediaObjects);
                            Objects.add(cartmodel);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cartRecycler recycler = new cartRecycler(Cart_Activity.this, getApplicationContext(), Objects);
                                    Cart.setLayoutManager(linearLayoutManager_related);
                                    Cart.setAdapter(recycler);
                                    Double sum = 0.0;
                                    TextView buy_price = findViewById(R.id.buy_price);

                                    for (int i = 0; i < Objects.size(); i++) {
                                        sum += (Double.valueOf(Objects.get(i).getQuantity()) * Double.valueOf(Objects.get(i).getNew_price()));
                                    }
                                    Log.e("Sumation", String.valueOf(sum));
                                    buy_price.setText(String.valueOf(sum));
                                }
                            });

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }

    @Override
    public void sumation(ArrayList<cartmodel> cartmodels) {
        Double sum = 0.0;
        TextView buy_price = findViewById(R.id.buy_price);

        for (int i = 0; i < cartmodels.size(); i++) {
            sum += (Double.valueOf(cartmodels.get(i).getQuantity()) * Double.valueOf(cartmodels.get(i).getNew_price()));
        }
        Log.e("Sumation", String.valueOf(sum));
        buy_price.setText(String.valueOf(sum));
    }
}
