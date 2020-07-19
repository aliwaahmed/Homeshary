package com.customer.shary.live.notification.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.telecom.Call;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.notification.datamodel;
import com.customer.shary.live.notification.rec.adapte;
import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;
import com.google.gson.JsonArray;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class notificationviewmodel extends ViewModel {

    private MutableLiveData<ArrayList<datamodel>> notifcationlist;
    private com.customer.shary.live.notification.datamodel datamodel;
    private ArrayList<datamodel> list = new ArrayList<>();
    private Context context;

    MediaObject mediaObjects;

    public LiveData<ArrayList<datamodel>> getText(Context context) {
        if (notifcationlist == null) {
            list = new ArrayList<>();
            notifcationlist = new MutableLiveData<>();
            this.context = context;
            load_users();
            return notifcationlist;
        } else {
            return notifcationlist;

        }
    }

    private void load_users() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("login",
                Context.MODE_PRIVATE);


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/customer-notifications/" +
                        sharedPreferences.getString("login_id", "-1"))
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {


                String res = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    JSONArray product;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        datamodel = new datamodel();
                        datamodel.setId(String.valueOf(jsonArray.getJSONObject(i).getInt("id")));
                        datamodel.setTitle(jsonArray.optJSONObject(i).getString("title"));
                        datamodel.setMessage(jsonArray.optJSONObject(i).getString("message"));
                        datamodel.setSeller_id(jsonArray.optJSONObject(i).getString("seller_id"));
                        datamodel.setSeller_image(jsonArray.optJSONObject(i).getString("seller_image"));


                        for (int j = 0; j < jsonArray.optJSONObject(i).getJSONArray("product").length(); j++) {


                            datamodel.setProduct_imag(jsonArray.optJSONObject(i).getJSONArray("product")
                                    .getJSONObject(j).getString("video_thumb"));

                            product = jsonArray.getJSONObject(i).getJSONArray("product");

                            if (product.getJSONObject(j).getJSONArray("video").length() > 0) {

                                datamodel.setUser_rate(product.getJSONObject(j).getString("user_rate"));
                                Log.e("product_link", product.getJSONObject(j).getString("product_link"));
                                datamodel.setProduct_link(product.getJSONObject(j).getString("product_link"));
                                datamodel.setProduct_id(product.getJSONObject(j).getInt("id"));
                                datamodel.setName(product.getJSONObject(j).getString("name"));
                                datamodel.setViews(product.getJSONObject(j).getString("views"));
                                datamodel.setDetails(product.getJSONObject(j).getString("details"));
                                datamodel.setCategory(product.getJSONObject(j).getString("category"));
                                datamodel.setStore_name(product.getJSONObject(j).getString("store_name"));
                                datamodel.setVideo_link(product.getJSONObject(j).getString("video_link") + product.getJSONObject(j).getJSONArray("video").get(0));
                                datamodel.setLikes(product.getJSONObject(j).getString("likes"));
                                datamodel.setUser_store_follow(product.getJSONObject(j).getString("user_store_follow"));
                                datamodel.setUser_share(product.getJSONObject(j).getString("user_share"));
                                datamodel.setComment_number(product.getJSONObject(j).getInt("comments"));
                                datamodel.setSeller_id(product.getJSONObject(j).getString("user_id"));
                                datamodel.setPrice(product.getJSONObject(j).getString("price"));
                                datamodel.setNew_price(product.getJSONObject(j).getString("new_price"));
                                datamodel.setUser_like(product.getJSONObject(j).getString("user_like"));
                                datamodel.setStore_image(product.getJSONObject(j).getString("store_image"));
                                datamodel.setImage_link(product.getJSONObject(j).getString("video_thumb"));
                                datamodel.setShare(product.getJSONObject(j).getString("share"));
                                datamodel.setQuantity(String.valueOf(product.getJSONObject(j).optInt("quantity")));
                                datamodel.setRate_count(String.valueOf(product.getJSONObject(j).getInt("rate")));
                                // datamodel.setSeller_room(product.getJSONObject(i).getString("seller_room"));
                                mediaObjects = new MediaObject("",
                                        datamodel.getVideo_link()
                                        , datamodel.getImage_link(), "");
                                datamodel.setMediaObject(mediaObjects);

                            }
                            list.add(datamodel);
                        }


                    }

                    notifcationlist.postValue(list);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }
}
