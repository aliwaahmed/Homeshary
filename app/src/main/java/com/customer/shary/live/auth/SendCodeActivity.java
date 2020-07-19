package com.customer.shary.live.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.customer.shary.live.MainActivity;
import com.customer.shary.live.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendCodeActivity extends AppCompatActivity {

    Button send_code ;
    EditText editText ;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code);

        send_code = findViewById(R.id.sendCode);
        editText=findViewById(R.id.ver_code);
        back=findViewById(R.id.back);





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendcode(editText.getText().toString());
            }
        });
    }





    public void sendcode(String code)
    {


        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder mMultiplebody=new MultipartBody.Builder().setType(MultipartBody.FORM);

        mMultiplebody.addFormDataPart("email",code);
        mMultiplebody.build();


        RequestBody requestBody1 = mMultiplebody.build();

        Request request =new Request.Builder().url(" https://shary.live/api/v1/user/forgetpassword")
                .post(requestBody1)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {



                String s =response.body().string();


                Log.e("s",s);
                try {
                    JSONObject jsonObject =new JSONObject(s);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toasty.success(getApplicationContext() ,  jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    if(jsonObject.getInt("status")==200)
                    {

                        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }
}
