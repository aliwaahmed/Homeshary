package com.customer.shary.live.Contacts;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.customer.shary.live.Contacts.contacts_model.contact_api_model;
import com.customer.shary.live.Contacts.rec.Adapter;
import com.customer.shary.live.GetingDeviceInfo.GettingDeviceInfo;
import com.customer.shary.live.R;
import com.customer.shary.live.notification.rec.adapte;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;


public class ContactsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ArrayList<model> models,models1;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences ;
    private String link;
    private LinkedList<String> phones;
    private contact_api_model contact_api_model ;
    private ArrayList<contact_api_model> contact_api;
    private ArrayList<String> bool =new ArrayList<>();
    private HashMap<Integer ,contact_api_model> map =new HashMap<>();
    private HashMap<Integer ,String> map1 =new HashMap<>();


    JSONArray jsonElements;
    JSONObject jsonObject2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        models=new ArrayList<>();
        models1=new ArrayList<>();
        phones=new LinkedList<>();
        contact_api=new ArrayList<>();
        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        //setContentView(R.layout.you);
        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        dialog.setMessage(getResources().getString(R.string.load));
        dialog.setCancelable(false);
//        dialog.show();

        recyclerView = findViewById(R.id._contacts_recycler);
        models = new ArrayList<>();

        jsonElements =new JSONArray();

        Get_active_user();
        share_linke();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                ContentResolver contentResolver = getApplicationContext().getContentResolver();
//                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//                while (cursor.moveToNext()) {
//                    String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
//                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
//                    contentResolver.delete(uri, null, null);
//                    Log.e("dsad","ad");
//                }


                getContactList();
            }
        }).start();

    }

    private ArrayList<model> getContactList() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        jsonObject2 =new JSONObject();

                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        models.add(new model(pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                                pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER)), "","false"));

                        models1.add(new model(pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                                pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER)), "","false"));

                        try {
                            jsonObject2.put("name",pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                            jsonObject2.put("phone",phoneNo);
                            jsonObject2.put("email","");
                            jsonElements.put(jsonObject2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    pCur.close();
                }
            }



        }
        if (cur != null) {
            cur.close();
        }

        upload_contact();



        return models;
    }


    public void upload_contact() {




        JSONObject  jsonObject =new JSONObject();
        try {
            jsonObject.put("user_id",sharedPreferences.getString("login_id","-1"));
            jsonObject.put("conatcts",jsonElements);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        Log.e("json",jsonObject.toString());

        AndroidNetworking
                .post("https://shary.live/api/v1/addcontacts")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res",response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }



    public  void Get_active_user()
    {


        OkHttpClient client =new OkHttpClient();

        Request request =new Request.Builder().
                url("https://shary.live/api/v1/getusercontact/"+sharedPreferences.getString("login_id","-1")).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                String res =response.body().string();


                try {

                    JSONObject jsonObject =new JSONObject(res);
                    JSONArray jsonArray =jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    Type category = new TypeToken<ArrayList<contact_api_model>>(){}.getType();
                    contact_api = gson.fromJson(jsonArray.toString(), category);
                    Log.e("res", String.valueOf(contact_api.get(0).getRoom_name()));




                    Log.e("size", String.valueOf(models.size()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for(int i=0 ;i<models.size();i++)
                            {

                                String s1=models.get(i).getPhone().toString().trim();
                                for(int j =0 ;j<contact_api.size();j++) {

                                    String s=contact_api.get(j).getPhone();

                                    if(s!=null) {


                                        if (s1.replaceAll(" ","").trim().equals(s.toString().trim())) {


                                            map.put(i,contact_api.get(j));
                                            map1.put(i,"true");
                                            bool.add("true");

                                        } else {

                                            bool.add("false");

                                        }
                                    }
                                    else
                                    {
                                        bool.add("false");

                                    }
                                }
                            }




                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new Adapter(contact_api,map,map1,link,models, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    //  dialog.dismiss();
                                }
                            });
                        }
                    }).start();


                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        });

    }

    public  void share_linke()
    {

        OkHttpClient client =new OkHttpClient();

        Request request =new Request.Builder().
                url("http://shary.live/api/v1/sharelink").build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                String res =response.body().string();

                try {
                    JSONObject jsonObject =new JSONObject(res);
                    link=jsonObject.getString("url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }





}
