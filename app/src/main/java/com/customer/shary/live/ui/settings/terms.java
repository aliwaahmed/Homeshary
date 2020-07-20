package com.customer.shary.live.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.shary.live.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class terms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_layout);
        ImageView backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        get("http://shary.live/api/v1/terms");
    }
    public void get(String url)
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();


                try {
                    JSONObject jsonObject =new JSONObject(myResponse);
                    JSONArray jsonElements = jsonObject.getJSONArray("data");
                    for(int i =0;i<jsonElements.length();i++)
                    {

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView10 =findViewById(R.id.textView10);
                            try {
                                textView10.setText(jsonElements.getJSONObject(0).getString("content"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}