package com.customer.shary.live.payment.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.customer.shary.live.R;
import com.customer.shary.live.payment.order.ordermodel.Datum;

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

public class myorder extends AppCompatActivity {


    private RecyclerView _orders_recycler;
    private LinearLayoutManager linearLayoutManager;
    private Datum datum;
    private ArrayList<Datum> list = new ArrayList<>();
    private orderadapter orderadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);

        _orders_recycler = findViewById(R.id._orders_recycler_myorder);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        _orders_recycler.setLayoutManager(linearLayoutManager);

        get_my_product();
    }


    public void get_my_product() {

        SharedPreferences prefs;
        prefs = getSharedPreferences("login", MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/product_order/" + prefs.getString("login_id", "-1"))
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

                        datum = new Datum();
                        datum.setId(String.valueOf(jsonArray.getJSONObject(i).get("id")));
                        Log.e("id", String.valueOf(jsonArray.getJSONObject(i).get("id")));

                        datum.setCode(String.valueOf(jsonArray.getJSONObject(i).getString("code")));
                        Log.e("code", String.valueOf(jsonArray.getJSONObject(i).getString("code")));


                        datum.setName(String.valueOf(jsonArray.getJSONObject(i).getString("name")));
                        Log.e("name", String.valueOf(jsonArray.getJSONObject(i).getString("name")));


                        datum.setPhone(String.valueOf(jsonArray.getJSONObject(i).getString("phone")));
                        Log.e("phone", String.valueOf(jsonArray.getJSONObject(i).getString("phone")));


                        datum.setQuantity(String.valueOf(jsonArray.getJSONObject(i).getString("quantity")));
                        Log.e("quantity", String.valueOf(jsonArray.getJSONObject(i).getString("quantity")));


                        datum.setAddress(String.valueOf(jsonArray.getJSONObject(i).getString("address")));
                        Log.e("address", String.valueOf(jsonArray.getJSONObject(i).getString("address")));


                        datum.setPrice(String.valueOf(jsonArray.getJSONObject(i).getString("price")));
                        Log.e("price", String.valueOf(jsonArray.getJSONObject(i).getString("price")));


                        datum.setUser_id(String.valueOf(jsonArray.getJSONObject(i).getString("user_id")));
                        Log.e("user_id", String.valueOf(jsonArray.getJSONObject(i).getString("user_id")));


                        datum.setDelivery_method(String.valueOf(jsonArray.getJSONObject(i).getString("delivery_method")));
                        Log.e("delivery_method", String.valueOf(jsonArray.getJSONObject(i).getString("delivery_method")));


                        datum.setShip_fees(String.valueOf(jsonArray.getJSONObject(i).getString("ship_fees")));
                        Log.e("ship_fees", String.valueOf(jsonArray.getJSONObject(i).getString("ship_fees")));


                        datum.setStatus(String.valueOf(jsonArray.getJSONObject(i).getString("status")));
                        Log.e("status", String.valueOf(jsonArray.getJSONObject(i).getString("status")));


                        datum.setDuration(String.valueOf(jsonArray.getJSONObject(i).getString("duration")));
                        Log.e("duration", String.valueOf(jsonArray.getJSONObject(i).getString("duration")));


                        datum.setReason(String.valueOf(jsonArray.getJSONObject(i).getString("reason")));
                        Log.e("reason", String.valueOf(jsonArray.getJSONObject(i).getString("reason")));


                        datum.setNoti(String.valueOf(jsonArray.getJSONObject(i).getString("noti")));
                        Log.e("noti", String.valueOf(jsonArray.getJSONObject(i).getString("noti")));


                        datum.setCancel_at(String.valueOf(jsonArray.getJSONObject(i).getString("cancel_at")));
                        Log.e("cancel_at", String.valueOf(jsonArray.getJSONObject(i).getString("cancel_at")));


                        datum.setDelivered_at(String.valueOf(jsonArray.getJSONObject(i).getString("delivered_at")));
                        Log.e("delivered_at", String.valueOf(jsonArray.getJSONObject(i).getString("delivered_at")));


                        datum.setConfirm_at(String.valueOf(jsonArray.getJSONObject(i).getString("confirm_at")));
                        Log.e("confirm_at", String.valueOf(jsonArray.getJSONObject(i).getString("confirm_at")));


                        datum.setStore_id(String.valueOf(jsonArray.getJSONObject(i).getString("store_id")));
                        Log.e("store_id", String.valueOf(jsonArray.getJSONObject(i).getString("store_id")));


                        list.add(datum);


                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           orderadapter orderadapter = new orderadapter(getApplicationContext(), list);
                            _orders_recycler.setAdapter(orderadapter);
                            _orders_recycler.smoothScrollToPosition(orderadapter.getItemCount());


                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error", e.getMessage());

                }


            }
        });
    }
}
