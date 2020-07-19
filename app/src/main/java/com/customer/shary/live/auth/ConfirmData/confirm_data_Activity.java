package com.customer.shary.live.auth.ConfirmData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.customer.shary.live.MainActivity;
import com.customer.shary.live.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class confirm_data_Activity extends AppCompatActivity {

    ArrayList<counteryapi> arr=new ArrayList<>();
    String spinnerarr [];

    ArrayList<counteryapi> arr1=new ArrayList<>();
    String spinnerarr1 [];


    ArrayList<counteryapi> arr2=new ArrayList<>();
    String spinnerarr2 [];
    Spinner spinner2;
    Spinner spinner1;
    Spinner spinner;

    EditText reg_password,reg_confirm_password;
    SharedPreferences prefs;
    SharedPreferences prefs1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_data_);

        reg_password =findViewById(R.id.reg_password);
        reg_confirm_password=findViewById(R.id.reg_confirm_password);


        getCountery();

        Button confirm =findViewById(R.id.reg_button);

         prefs = getSharedPreferences("code", MODE_PRIVATE);
        prefs1 = getSharedPreferences("Signup", MODE_PRIVATE);


        String user_id=prefs.getString("userid",null);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(reg_password.getText().toString().isEmpty())
                {
                    reg_password.setError("Enter Data");
                }
                else if(reg_confirm_password.getText().toString().isEmpty())
                {
                    reg_confirm_password.setError("Enter Data");
                }

                else {
                    confirm(String.valueOf(arr.get(spinner.getSelectedItemPosition()).getId()),
                            String.valueOf(arr1.get(spinner1.getSelectedItemPosition()).getId()),
                            String.valueOf(arr2.get(spinner2.getSelectedItemPosition()).getId()),
                            reg_password.getText().toString(),
                            reg_confirm_password.getText().toString(),user_id);
                }
            }
        });


    }

    public  void getCountery()
    {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/countries")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String res =response.body().string();
                try {
                    JSONObject  jsonObject= new JSONObject(res);
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    spinnerarr  =new String[jsonArray.length()];

                    for(int i =0 ;i<jsonArray.length();i++)
                    {
                        counteryapi counteryapi =new counteryapi();
                        counteryapi.setName(jsonArray.optJSONObject(i).getString("name"));
                        counteryapi.setId(jsonArray.optJSONObject(i).getInt("id"));

                        spinnerarr[i]=jsonArray.optJSONObject(i).getString("name");

                        arr.add(counteryapi);
                    }




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("length", String.valueOf(arr.size()));
                           spinner = findViewById(R.id.spiner1);


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,spinnerarr);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    Counterycode(String.valueOf(arr.get(i).getId()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    public void Counterycode(String Counterycode)
    {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/state/"+Counterycode)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {



                String res =response.body().string();
                try {
                    JSONObject  jsonObject= new JSONObject(res);
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    spinnerarr1  =new String[jsonArray.length()];

                    for(int i =0 ;i<jsonArray.length();i++)
                    {
                        counteryapi counteryapi =new counteryapi();
                        counteryapi.setName(jsonArray.optJSONObject(i).getString("name"));
                        counteryapi.setId(jsonArray.optJSONObject(i).getInt("id"));

                        spinnerarr1[i]=jsonArray.optJSONObject(i).getString("name");

                        arr1.add(counteryapi);
                    }




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("length", String.valueOf(arr1.size()));
                             spinner1 = findViewById(R.id.spiner2);


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,spinnerarr1);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(adapter);

                            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    City(String.valueOf(arr1.get(i).getId()));

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    public void City(String city)
    {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/city/"+city)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {



                String res =response.body().string();
                try {
                    JSONObject  jsonObject= new JSONObject(res);
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    spinnerarr2  =new String[jsonArray.length()];

                    for(int i =0 ;i<jsonArray.length();i++)
                    {
                        counteryapi counteryapi =new counteryapi();
                        counteryapi.setName(jsonArray.optJSONObject(i).getString("name"));
                        counteryapi.setId(jsonArray.optJSONObject(i).getInt("id"));

                        spinnerarr2[i]=jsonArray.optJSONObject(i).getString("name");

                        arr2.add(counteryapi);
                    }




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("length", String.valueOf(arr2.size()));
                             spinner2 = findViewById(R.id.spiner3);


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,spinnerarr2);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(adapter);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    /**
     *
     * @param countery
     * @param gov
     * @param city
     * @param add
     * @param phone
     */
    public void confirm(String countery,String gov,String city,String add,String phone ,String id)
    {


        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder mMultiplebody=new MultipartBody.Builder().setType(MultipartBody.FORM);

        mMultiplebody.addFormDataPart("address",add);
        mMultiplebody.addFormDataPart("state",gov);
        mMultiplebody.addFormDataPart("city",city);
        mMultiplebody.addFormDataPart("country",countery);
        mMultiplebody.addFormDataPart("phone",phone);
        mMultiplebody.addFormDataPart("user_id",id);


        mMultiplebody.build();


        RequestBody requestBody1 = mMultiplebody.build();

        Request request =new Request.Builder().url("https://shary.live/api/v1/user/register_step").post(requestBody1).build();

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
                        prefs.edit().clear().commit();
                        prefs1.edit().clear().commit();



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

















}
