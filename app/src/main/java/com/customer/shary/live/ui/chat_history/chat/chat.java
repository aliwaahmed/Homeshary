package com.customer.shary.live.ui.chat_history.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.ui.chat_conversation.conv_adapter;
import com.customer.shary.live.ui.chat_conversation.datamodel.Msg_Object;
import com.customer.shary.live.ui.chat_history.chat.bottomsheet.bottomSheet;
import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.customer.shary.live.ui.live.SignallingClient.socket;


public class chat extends AppCompatActivity implements
        View.OnClickListener, SignallingClient.chat,
        bottomSheet.getfile {


    //region objects
    private ImageView attach;
    private RecyclerView RecyclerViewChat;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout mic, send_msg;
    private ArrayList<Msg_Object> msg_objects = new ArrayList<>();
    private EditText msg;
    private Msg_Object object;
    private bottomSheet bottomSheet = new bottomSheet();
    private conv_adapter conv_adapter;
    private LinearLayout _block;
    private SharedPreferences preferences;
    private ImageView img, video, _send_file;
    private static final int Gallary = 0;
    private static final int Video = 1001;
    private TextView _block_txt;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation


        //region initviews
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        RecyclerViewChat = findViewById(R.id.RecyclerViewChat);
        msg = findViewById(R.id.msg);
        mic = findViewById(R.id.mic);
        _block = findViewById(R.id._block);
        _send_file = findViewById(R.id._send_file);
        _block_txt = findViewById(R.id._block_txt);
        send_msg = findViewById(R.id.send_msg);
        attach = findViewById(R.id.image);
        video = findViewById(R.id._send_voice);
        img = findViewById(R.id._send_media);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerViewChat.setLayoutManager(linearLayoutManager);
        attach.setOnClickListener(this);
        SignallingClient.getInstance(getApplicationContext()).chat(this::recieve_msg);
        conv_adapter = new conv_adapter(msg_objects, getApplicationContext());
        //endregion


        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object = new Msg_Object();
                object.setMessage(msg.getText().toString());
                object.setMessage_type("0");
                object.setReceiver("0");
                //myid
                object.setId(preferences.getString("login_id", "-1"));


                msg_objects.add(object);
                SignallingClient.getInstance(getApplicationContext()).send_messsage(
                        object.getMessage(),
                        preferences.getString("name", "-1"),
                        preferences.getString("login_id", "-1"),
                        getIntent().getExtras().getString("seller_room"),
                        getIntent().getExtras().getString("seller_id"));
                conv_adapter.notifyDataSetChanged();
                RecyclerViewChat.smoothScrollToPosition(conv_adapter.getItemCount());
                msg.setText("");


            }
        });

        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                mic.setVisibility(View.INVISIBLE);
//                send_msg.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
//                    mic.setVisibility(View.INVISIBLE);
//                    send_msg.setVisibility(View.VISIBLE);
                } else {
//                    mic.setVisibility(View.VISIBLE);
//                    send_msg.setVisibility(View.INVISIBLE);
                }
            }
        });

        RecyclerViewChat.setAdapter(conv_adapter);

        _block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_block_txt.getText().toString().equals("block")) {
                    mic.setVisibility(View.INVISIBLE);
                    ConstraintLayout constraintLayout5 = findViewById(R.id.constraintLayout5);
                    constraintLayout5.setVisibility(View.INVISIBLE);
                    _block_txt.setText("unblock");
                    Log.e("dsd", "ds");
                    SignallingClient.getInstance(getApplicationContext()).init();
                    SignallingClient.getInstance(getApplicationContext()).
                            block_chat(getIntent().getExtras().getString("seller_id"),
                                    preferences.getString("login_id", "-1"));
                    Log.e("sellar_id", getIntent().getExtras().getString("seller_id"));

                } else {
                    mic.setVisibility(View.VISIBLE);
                    ConstraintLayout constraintLayout5 = findViewById(R.id.constraintLayout5);
                    constraintLayout5.setVisibility(View.VISIBLE);
                    SignallingClient.getInstance(getApplicationContext()).unblock
                            (getIntent().getExtras().getString("seller_id"), preferences.getString("login_id", "-1"));

                    Log.e("sellar_id", getIntent().getExtras().getString("seller_id"));

                    _block_txt.setText("block");
                }


            }
        });

        Log.e("aliali", String.valueOf(getIntent().getExtras().getInt("block")));

        if (getIntent().getExtras().getInt("block") == 1) {
            mic.setVisibility(View.INVISIBLE);
            ConstraintLayout constraintLayout5 = findViewById(R.id.constraintLayout5);
            constraintLayout5.setVisibility(View.INVISIBLE);
            _block_txt.setText("unblock");
        } else {
            mic.setVisibility(View.VISIBLE);
            ConstraintLayout constraintLayout5 = findViewById(R.id.constraintLayout5);
            constraintLayout5.setVisibility(View.VISIBLE);

        }

        RelativeLayout constraintLayout = findViewById(R.id.Relatvie);

        constraintLayout.setVisibility(View.INVISIBLE);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallary);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, Video);
            }
        });

        _send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 1111);
            }
        });

        get_Conversation();


    }

    boolean Flag = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image:
                if (Flag) {
                    Flag = false;
                    revealFAB();
                } else {
                    Flag = true;
                    hideFAB();
                }
//                bottomSheet.newInstance(getApplicationContext(),chat.this);
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                break;
        }
    }


    @Override
    public void recieve_msg(String message, String username, String type, String to) {


        Log.e("msg", message);
        if (type.equals("0")) {
            object = new Msg_Object();
            object.setMessage(message);
            object.setMessage_type("0");
            object.setReceiver("1");
            //myid
            object.setId(preferences.getString("login_id", "-1"));
            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });
            RecyclerViewChat.smoothScrollToPosition(conv_adapter.getItemCount());

        } else if (type.equals("1")) {

            object = new Msg_Object();
            object.setMessage_type("1");
            object.setReceiver("1");
            object.setId("2");
            object.setImage(message);
            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });
            RecyclerViewChat.smoothScrollToPosition(conv_adapter.getItemCount());


        } else if (type.equals("2")) {
            object = new Msg_Object();
            object.setMessage_type("2");
            object.setReceiver("1");
            object.setId("2");
            object.setFile(message);

            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });

        } else if (type.equals("3")) {
            object = new Msg_Object();
            object.setMessage_type("3");
            object.setReceiver("1");
            object.setId("2");
            object.setFile(message);

            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();
                }
            });

        }


    }

    @Override
    public void file(String path, String type) {

        if (type.equals("1")) {


        } else if (type.equals("2")) {
            object = new Msg_Object();
            object.setMessage_type("2");
            object.setReceiver("0");
            object.setId("2");
            object.setFile(path);

            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });

        } else if (type.equals("3")) {
            object = new Msg_Object();
            object.setMessage_type("3");
            object.setReceiver("0");
            object.setId("2");
            object.setFile(path);

            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });

        }
        if (path != null) {
            Upload(new File(path), preferences.getString("login_id", "-2"), "ali", "logapps", object.getMessage_type());
        }

    }

    public void Upload(File file, String userid, String name, String room, String type) {


        Random random = new Random();
        int x = random.nextInt();
        NotificationManager notificationManager;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS)
                .writeTimeout(1200, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        AndroidNetworking.upload("https://shary.live/api/v1/android_upload")
                .setPriority(Priority.HIGH)
                .addMultipartFile("file", file)
                .addMultipartParameter("userid", userid)//me
                .addMultipartParameter("name", name)//me
                .addMultipartParameter("room", room)//me
                .setPercentageThresholdForCancelling(1)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        Log.e("progress", String.valueOf(bytesUploaded));
                        String channelId = "Default";
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)

                                .setSmallIcon(R.drawable.ic_file_upload)
                                .setColor(getResources().getColor(R.color.white))
                                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                                .setContentTitle("uploading")
                                .setGroupSummary(true)
                                .setOnlyAlertOnce(true)
                                .setProgress((int) totalBytes, (int) bytesUploaded, false)
                                .setAutoCancel(true);
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        NotificationChannel channel = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                            manager.createNotificationChannel(channel);

                        }


                        manager.notify(x, builder.build());


                    }
                })

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.e("resposde", response.toString());


                        try {
                            String path = response.getString("file_path");
                            Log.e("path", path);


                            NotificationManager notificationManager =
                                    (NotificationManager) getApplicationContext()
                                            .getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(x);


                            //TODO EMIT SEND SOCKET
                            SignallingClient.getInstance(getApplication()).uploaded(path,
                                    preferences.getString("name", "-1"), type, userid,
                                    getIntent().getExtras().getString("seller_id"));

                            Log.e("name",preferences.getString("name", "-1"));
                            Log.e("type",type);
                            Log.e("user_id",userid);
                            Log.e("to",getIntent().getExtras().getString("seller_id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error


                    }
                });


    }

    //
    private void revealFAB() {

        Flag = false;
        RelativeLayout constraintLayout = findViewById(R.id.Relatvie);

        int cx = constraintLayout.getLeft() + constraintLayout.getRight();
        int cy = constraintLayout.getTop();

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(constraintLayout, cx, cy, 0, finalRadius);
        constraintLayout.setVisibility(View.VISIBLE);

        anim.start();
    }

    private void hideFAB() {
        Flag = true;
        RelativeLayout constraintLayout = findViewById(R.id.Relatvie);

        int cx = constraintLayout.getLeft() + constraintLayout.getRight();
        int cy = constraintLayout.getTop();

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(constraintLayout, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                constraintLayout.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }


    private void browseDocuments() {

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1111);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Gallary && resultCode == RESULT_OK && null != data) {

            object = new Msg_Object();
            object.setMessage_type("1");
            object.setReceiver("0");
            object.setId("2");
            object.setImage(getPath(data.getData()));

            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });
            Upload(new File(getPath(data.getData())), preferences.getString("login_id", "-2"), preferences.getString("name", "-2"), preferences.getString("room", "-2"), object.getMessage_type());

            Log.e("d", "alialiali");

        } else if (requestCode == Video && resultCode == RESULT_OK && null != data) {
            object = new Msg_Object();
            object.setMessage_type("2");
            object.setReceiver("0");
            object.setId("2");
            object.setFile(getPath(data.getData()));

            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });
            Upload(new File(getPath(data.getData())), preferences.getString("login_id", "-2"), preferences.getString("name", "-2"),  preferences.getString("room", "-2"), object.getMessage_type());


        } else if (requestCode == 1111 && resultCode == RESULT_OK && null != data) {
            Log.e("d", getPath(data.getData()));
            object = new Msg_Object();
            object.setMessage_type("3");
            object.setReceiver("0");
            object.setId("2");
            object.setFile(getPath(data.getData()));
            msg_objects.add(object);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    conv_adapter.notifyDataSetChanged();

                }
            });

            Upload(new File(getPath(data.getData())), preferences.getString("login_id", "-2"), preferences.getString("name", "-2"), preferences.getString("room", "-2"), object.getMessage_type());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }



    public void get_Conversation ()
    {


        SharedPreferences sharedPreferences =getSharedPreferences("login",Context.MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/getUserChat/"+sharedPreferences.getString("login_id","-1")+"/"+getIntent().getExtras().getString("seller_id")+"/1")
                .build();

        Log.e("id",getIntent().getExtras().getString("seller_id"));
        Log.e("user_id",sharedPreferences.getString("login_id","-1"));

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

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        object = new Msg_Object();


                        object.setMessage_type(jsonArray.getJSONObject(i).getString("type"));
                        if(Integer.valueOf(jsonArray.getJSONObject(i).getString("sender"))
                                ==Integer.valueOf((sharedPreferences.getString("login_id","-1"))))
                        {

                            object.setReceiver("0");

                        }
                        else
                        {
                            object.setReceiver("1");

                        }

                        if(jsonArray.getJSONObject(i).getString("type").equals("3")) {
                            object.setFile(jsonArray.getJSONObject(i).getString("link")
                                    + jsonArray.getJSONObject(i).getString("sound"));
                        }
                        if(jsonArray.getJSONObject(i).getString("type").equals("4")) {
                            object.setFile(jsonArray.getJSONObject(i).getString("link")
                                    + jsonArray.getJSONObject(i).getString("video"));

                        }

                        object.setImage("https://shary.live/files/1/chat/images/"
                                +jsonArray.getJSONObject(i).getString("images"));

                        object.setMessage(jsonArray.getJSONObject(i).getString("message"));

                        msg_objects.add(object);


                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conv_adapter.notifyDataSetChanged();

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }

            }
        });

    }


}



































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































