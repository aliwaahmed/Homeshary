package com.customer.shary.live.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Signup extends AppCompatActivity {

    //vars
    EditText name , email , password , confirm_password ;
    ImageView backBtn ;
    Button register ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        confirm_password = findViewById(R.id.reg_confirm_password);
        register = findViewById(R.id.reg_button);
        backBtn = findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        registeration();


        SharedPreferences prefs = getSharedPreferences("Signup", MODE_PRIVATE);
        String name = prefs.getString("name", "");//"No name defined" is the default value.

        if(!name.equals(""))
        {
            Intent intent =new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();

        }


    }

    //register method
    private void registeration() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //strings to compare two values . password & confirm password
                String pass = password.getText().toString().trim();
                String conf_pass = confirm_password.getText().toString().trim();


                if (name.length() == 0){
                    Toasty.error(Signup.this , R.string.missing_name , Toast.LENGTH_SHORT).show();
                } else if (email.length() == 0){
                    Toasty.error(Signup.this , R.string.missing_email , Toast.LENGTH_SHORT).show();
                }else if (password.length() == 0){
                    Toasty.error(Signup.this , R.string.missing_pass , Toast.LENGTH_SHORT).show();
                }else if (confirm_password.length() == 0){
                    Toasty.error(Signup.this , R.string.missing_confirm , Toast.LENGTH_SHORT).show();
                }


                else  if (pass==conf_pass){

                    Toasty.error(Signup.this , R.string.notEqual , Toast.LENGTH_SHORT).show();
                }
                 else{

                     signUp(name.getText().toString(),email.getText().toString(),password.getText().toString());




                }

            }
        });
    }


    public void signUp(String name,String Email,String password )
    {


        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder mMultiplebody=new MultipartBody.Builder().setType(MultipartBody.FORM);

      mMultiplebody.addFormDataPart("user_name",name);
      mMultiplebody.addFormDataPart("email",Email);
      mMultiplebody.addFormDataPart("user_password",password);
      mMultiplebody.build();


        RequestBody requestBody1 = mMultiplebody.build();

        Request request =new Request.Builder().url("https://shary.live/api/v1/user/signup").post(requestBody1).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {





                try {
                    JSONObject jsonObject =new JSONObject(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toasty.success(Signup.this ,  jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    if(jsonObject.getInt("status")==200)
                    {
                        SharedPreferences.Editor editor = getSharedPreferences("Signup", MODE_PRIVATE).edit();
                        editor.putString("name",name);
                        editor.apply();

                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
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
