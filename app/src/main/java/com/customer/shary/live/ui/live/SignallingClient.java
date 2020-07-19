package com.customer.shary.live.ui.live;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import io.socket.client.IO;
import io.socket.client.Socket;

import static org.webrtc.ContextUtils.getApplicationContext;


public class SignallingClient {




    String name;
    private Context mContext;
    private static SignallingClient instance;
    private String roomName = null;
    int ID = 0;
    public static Socket socket;
    public boolean isChannelReady = true;
    public boolean isInitiator =true;
    public boolean isStarted = false;
    private SignalingInterface callback;
    StringBuilder query;

    //This piece of code should not go into production!!
    //This will help in cases where the node server is running in non-https server and you want to ignore the warnings
    @SuppressLint("TrustAllX509TrustManager")
    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) {
        }
    }};
    public static boolean mboolean = false;
    public static int stoping_call = 0;
    public static String[] caller_data;

    public SignallingClient() {
    }
    public SignallingClient(Context applicationContext) {
        this.mContext=applicationContext;
    }

    public static SignallingClient getInstance() {
        if (instance == null) {
            instance = new SignallingClient();
        }
        return instance;
    }

    /**
     * @param signalingInterface
     * @param roomName
     * @param ID
     * @param name
     * @param mContext
     */

    /**
     *
     * @param signalingInterface
     * @param roomName
     * @param ID
     * @param name
     * @param mContext
     */
    public void init(SignalingInterface signalingInterface, String roomName, int ID, String name, Context mContext) {
        this.callback = signalingInterface;
        this.roomName = roomName;
        this.ID = ID;
        this.mContext = mContext;
        this.name = name;

        try {

            IO.Options options = new IO.Options();
            String result = "";

            //ToDO : emit for data

            Log.e("rrom", roomName);
            StringBuilder query =
                    new StringBuilder("user_id=" + ID + "&username=" + name + "&room=" + roomName + "&url=" + "https://c-box.live:5000" + "&title=" + "my title" + "&type=" + 1);

            result = query.toString();
            options.query = result;

            socket = IO.socket("https://shary.live:7000", options);
            if (!socket.connected()) {

                socket.connect();
            }


            Log.e("Connected", socket.connected() + " out");

            if (socket.connected()) {
                Log.i("SignallingClient", socket.connected() + "connec");

            } else {
                Log.i("SignallingClient", socket.connected() + "");

            }
            Log.i("SignallingClient", "init() called");

            if (!roomName.isEmpty()) {
                emitInitStatement(roomName);
            }


            socket.on("callrequest", args -> {
                mboolean = true;
                if (roomName != null && roomName.equalsIgnoreCase((String) args[0])) {
                    Log.e("SignallingClient", "callrequest comming 0  " + args[0]);
                    Log.e("SignallingClient", "callrequest comming 1  " + args[1]);

                    caller_data = new String[4];
                    caller_data[0] = args[0].toString();//room
                    caller_data[1] = args[1].toString();//name
                    caller_data[3]=args[2].toString();
                    //  callback.requestCall((String) args[1], (String) args[2]);
                }

                peer();

            });





            socket.on("notAnswerCall",args -> {

//                MyVideoCall.mediaPlayer.stop();
//                Intent intent =new Intent(mContext, Visitors.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);


            });

            //room created event.
            socket.on("created", args -> {

                if (((String) args[0]).equalsIgnoreCase(roomName)) {
                    Log.v("SignallingClient", "created call() called with: args = [" + Arrays.toString(args) + "]");
                    isInitiator = true;
                    callback.onCreatedRoom();
                }


            });

            //log event
            socket.on("log", args -> Log.d("SignallingClient", "log call() called with: args = [" + Arrays.toString(args) + "]"));


            // check over
            socket.on("got user media", args -> {
                Log.v("SignallingClient", "got user media");
                callback.onTryToStart();

            });


            // offer
            socket.on("offer", args -> {
                Log.v("SignallingClient", "offer");

               JSONObject data = (JSONObject) args[0];
               callback.onOfferReceived(data);
            });



            socket.on("answer", args -> {
                Log.v("SignallingClient", "answer");
                JSONObject data = (JSONObject) args[0];
                callback.onAnswerReceived(data);
            });

            socket.on("candidate", args -> {
                try {
                    Log.v("SignallingClient", "candante");

                    if (args[0] instanceof JSONObject) {
                        JSONObject data = (JSONObject) args[0];
                        Log.v("data", data.toString());

                        callback.onIceCandidateReceived(data);

                    } else {
                        Log.v("ahmed error 1", "not instance of jsonobject");
                    }

                    if (args[0] == null) {
                        Log.v("ahmed error", "ic candante= null");
                    }
                } catch (Exception e) {
                    Log.v(getClass().getSimpleName(), e.getMessage());
                }

            });


            //TODO : check user online
            socket.on("user_is_online", args -> {
                Log.v("SignallingClient ahmed", "" + args[0]);
            });


            //messages - SDP and ICE candidates are transferred through this
            socket.on("message", args -> {
                //Log.e("SignallingClient", "message call() called with: args = [" + Arrays.toString(args) + "]");

                if (args[0] instanceof String) {
                    Log.v("darwish", "String received :: " + args[0]);
                    String data = (String) args[0];
                    if (data.equalsIgnoreCase(roomName)) {
                        Log.v("SignallingClient", "roomname");

                        callback.onTryToStart();
                    }

                } else if (args[0] instanceof JSONObject) {
                    try {

                        JSONObject data = (JSONObject) args[0];
                        Log.d("SignallingClient", "Json Received :: " + data.toString());
                        String type = data.getString("type");
//                        String comeRoom = data.getString("room");

                        if (type.equalsIgnoreCase("offer") /***&& comeRoom.equalsIgnoreCase(roomName)**/) {
                            Log.e("SignallingClient", "message offer");

                            callback.onOfferReceived(data);
                        } else if (type.equalsIgnoreCase("answer") && isStarted /**&& comeRoom.equalsIgnoreCase(roomName)**/) {
                            Log.e("SignallingClient", "message answer");

                            callback.onAnswerReceived(data);
                        } else if (type.equalsIgnoreCase("candidate") && isStarted /**&& comeRoom.equalsIgnoreCase(roomName)**/) {
                            Log.e("SignallingClient", "message condanite");

                            callback.onIceCandidateReceived(data);
//                            callback.showMes("roam candanint "+ roomName);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v("Exception", e.getMessage() + "");
                    }
                }
            });

            socket.on("docancel", args -> {
                Log.v("VideoCallActivity", "cancel call socket");
                if (roomName.equalsIgnoreCase((String) args[0])) {
                    callback.CallCanceled();
                }
            });






            //ToDo:Listen to status
            socket.on("messageRead", args -> {
                //TODO: BroadCast new message
                Intent intent1 = new Intent("newmsg");
                intent1.putExtra("status", "status");// visitor id


                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
                localBroadcastManager.sendBroadcast(intent1);



            });













            // notiMessage


        } catch (URISyntaxException e ) {
        }
    }


    /**
     * @param txt
     * @param type
     * @param adminname
     * @param Login_ID
     * @param Visitor_id
     */
    public void EmitMsg(String txt, int type, String adminname, int Login_ID, String Visitor_id) {

        socket.emit("sendMessageAdmin", txt, adminname, String.valueOf(type), String.valueOf(Login_ID), Visitor_id);

    }


    public static  void  EmitQR (String id, String name ,String room )
    {
        socket.emit("qr_login", id,name,room);

    }


    public void emitMessage(String message) {
        socket.emit("sendMessageAdmin", message);

    }


    public void Create(String roomname,String my_id)
    {

        socket.emit("create or join old",roomname,my_id);
    }

    public String msg() {
        final String[] s = {""};
        socket.on("sendMessageAdmin", args -> {

            s[0] = args.toString();
        });
        return s[0];
    }


    public void sendRomeName(String roomName) {
        socket.emit("check", roomName);
    }

    public void emitInitStatement(String message) {
        roomName = message;
        socket.emit("create or join", message, 1);
    }


    public void requestResponse(int response) {
        socket.emit("requestres", response);
    }


    public void emitMessage(SessionDescription message) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", message.type.canonicalForm());
            obj.put("sdp", message.description);
            obj.put("room", roomName);
            Log.d("emitMessage", obj.toString());
            socket.emit("message", obj);
            Log.d("vivek1794", obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startChat() {
        socket.emit("startchat", roomName);
        Log.e("Romali", roomName);
    }

    public void canncel() {
        try {
            socket.emit("cancel", roomName);
        } catch (Exception e) {
            e.getMessage();
            Log.v("Exception", e.getMessage() + "");
        }

    }

    public void emitIceCandidate(IceCandidate iceCandidate) {
        try {
            JSONObject object = new JSONObject();
            object.put("type", "candidate");
            object.put("label", iceCandidate.sdpMLineIndex);
            object.put("id", iceCandidate.sdpMid);
            object.put("candidate", iceCandidate.sdp);
            object.put("room", roomName);
            socket.emit("message", object);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void close() {
        //    isChannelReady = false;
        isStarted = false;
        isInitiator = false;
        // instance.roomName = null;
        socket.emit("bye", roomName);
        socket.disconnect();
        socket.close();
        stoping_call = 0;
        Log.d("SignallingClient", "close method ended");

    }

    public void close(String roomName) {
        socket.emit("bye", roomName);
        Log.v("darwish_get_out", roomName);

    }


    public void peer() {
        //region peer
        socket.on("join", args -> {
                    if (stoping_call == 0) {
                        if (((String) args[0]).equals(roomName)) {
                            Log.v("SignallingClient", "join call() " + stoping_call + " called with: args = [" + Arrays.toString(args) + "]");
                            isChannelReady = true;
                            callback.onNewPeerJoined();
//                            callback.showMes("roam join " + roomName);

                        }
                    } else {
                        Log.v("SignallingClient", "join call() for second time" + "]");
                    }
                    stoping_call++;

                }
        );


        //when you joined a chat room successfully
        socket.on("joined", args -> {

            if (((String) args[0]).equals(roomName)) {

                Log.v("SignallingClient", "joined call() called with: args = [" + Arrays.toString(args) + "]");
                isChannelReady = true;
                callback.onJoinedRoom();
//                    callback.showMes("roam joiend " + roomName);


            }

        });


        socket.on("ready", args -> {
            Log.v("SignallingClient", "ready");

            if (((String) args[0]).equals(roomName)) {
                isChannelReady = true;
            }
        });

        //endregion
    }

    public interface SignalingInterface {

        void onOfferReceived(JSONObject data);

        void onAnswerReceived(JSONObject data);

        void onIceCandidateReceived(JSONObject data);

        void onTryToStart();

        void onCreatedRoom();

        void onJoinedRoom();

        void onNewPeerJoined();








        void CallCanceled();


    }








}
