package com.customer.shary.live.ui.home.VideoCallAndVoiceCall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.customer.shary.live.R;
import com.customer.shary.live.ui.live.SignallingClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.customer.shary.live.ui.live.SignallingClient.caller_data;


public class MyVideoCall extends AppCompatActivity implements View.OnClickListener, SignallingClient.SignalingInterface
        {

    PeerConnectionFactory peerConnectionFactory;
    MediaConstraints audioConstraints;
    MediaConstraints videoConstraints;
    VideoSource videoSource;
    VideoTrack localVideoTrack;
    AudioSource audioSource;
    AudioTrack localAudioTrack;
    SurfaceViewRenderer localVideoView;
    VideoRenderer localRenderer;
    ImageButton hangup, muute, swap;
    ImageView acceptbtn, Refusebtn;
    ImageView callericon;
    PeerConnection localPeer;
    EglBase rootEglBase;
    boolean gotUserMedia;
    VideoCapturer videoCapturerAndroid;
    AudioManager audioManager;
    List<PeerConnection.IceServer> peerIceServers = new ArrayList<>();
    boolean cameraface = true;
    public String channelName;
    private int calltype;
    public TextView caller_text_name;
    public TextView inquery_text;
    String img;
    private final static int REQUEST_PERMISSION_CODE = 111;

    private final String TAG = getClass().getSimpleName();
    private boolean callstarted = false;
    private final static int REQUEST_MICROPHONE = 200;

    String call_start_time;
    String call_end_time;
    public static int call_id;
    SurfaceViewRenderer remoteVideoView;
    private  SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videocall);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedPreferences =getSharedPreferences("login",MODE_PRIVATE);


        SignallingClient.getInstance().init(this,sharedPreferences.getString("name","-2"),
                Integer.parseInt(sharedPreferences.getString("login_id","2")), sharedPreferences.getString("name","-2"),this );

        SignallingClient.getInstance().Create(sharedPreferences.getString("name","-2"),sharedPreferences.getString("login_id","-2"));



        //initViews();
            //initVideos();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (getPermissionRequest()) {
            initViews();
            int callerStart = 1;
            calltype = 100;
            SharedPreferences prefs=getSharedPreferences("login",MODE_PRIVATE);
            String id= prefs.getString("login_id","-1");


            String sender =  prefs.getString("room","");;
            String receiver = "receiver";
            channelName = sender;


            SignallingClient.getInstance().init(this, sender, Integer.parseInt(id),"s",getApplicationContext());
            switch (calltype) {
                case 100:
                    //video call
                    initVideos();
                    break;
                case 200:
                    //voice call
                    localVideoView.setVisibility(View.GONE);
                    findViewById(R.id.remote_gl_surface_view).setVisibility(View.GONE);
                    findViewById(R.id.local_gl_surface_view).setVisibility(View.GONE);
                    swap.setVisibility(View.GONE);
                    callericon.setVisibility(View.VISIBLE);
                    hangup.setVisibility(View.VISIBLE);
                    muute.setVisibility(View.VISIBLE);
                    break;
            }
            switch (callerStart) {
                case 0:
                    // when making a call
                    acceptbtn.setVisibility(View.GONE);
                    Refusebtn.setVisibility(View.GONE);
                    getIceServers();
                    start();

//                        videoCallViewModel.getContactdata(Integer.parseInt(receiver)).observe(this, userContacts -> {
//                            if (userContacts != null) {
//                                Uri uri = Uri.parse(getResources().getString(R.string.API_files)).buildUpon()
//                                        .appendPath("" + userContacts.getId())
//                                        .appendPath(userContacts.getImage()).build();
//                                GlideApp.with(this).asBitmap().circleCrop().placeholder(R.drawable.avatar).LoadTables(uri)
//                                        .into(callericon);
//                            }
//
//                        });
                    break;
                case 1:
                    //when receiving call
                    acceptbtn.setVisibility(View.VISIBLE);
                    Refusebtn.setVisibility(View.VISIBLE);
//                        videoCallViewModel.getContactdata(Integer.parseInt(sender)).observe(this, userContacts -> {
//                            if (userContacts != null) {
//                                Uri uri = Uri.parse(getResources().getString(R.string.API_files)).buildUpon()
//                                        .appendPath("" + userContacts.getId())
//                                        .appendPath(userContacts.getImage()).build();
//                                GlideApp.with(this).asBitmap().circleCrop().placeholder(R.drawable.avatar).LoadTables(uri)
//                                        .into(callericon);
//                            }
//
//                        });
                    break;
            }


            // }

        } else {
            requestPermission();
        }


        Log.d("SignallingClient ", "before call start");
        call_start_time = formatDate(String.valueOf(Calendar.getInstance().getTime()));




        inquery_text.setVisibility(View.GONE);
        acceptbtn.setVisibility(View.GONE);
        callericon.setVisibility(View.GONE);
        Refusebtn.setVisibility(View.GONE);
        hangup.setVisibility(View.VISIBLE);
        swap.setVisibility(View.VISIBLE);
        muute.setVisibility(View.VISIBLE);
        SignallingClient.getInstance().Create("logapps","1");
        SignallingClient.getInstance().startChat();
        getIceServers();
        start();




        if (!SignallingClient.getInstance().isStarted && localVideoTrack != null && SignallingClient.getInstance().isChannelReady) {
            createPeerConnection();
            SignallingClient.getInstance().isStarted = true;
            if (SignallingClient.getInstance().isInitiator) {
                Log.v("SignallingClient", "start before doCall");
                doCall();
            }
        }






    }


    // for sending UpdateCallEnd data to DB after finishing the call
    public void UpdateCallEnd(int status) {
        Log.d("SignallingClient call_H", "call_end_time " + call_end_time);
        Log.d("SignallingClient call_H", "call_status " + status + "");
        OkHttpClient client = new OkHttpClient();
        Log.d("Okhttp file ", "called actual request");
        String url = "http://getstores.net/api/v1/updatecallend";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("call_end_time", call_end_time + "")
                .addFormDataPart("call_id", call_id + "")
                .addFormDataPart("status", status + "")
                .build();
        Log.d("Okhttp file ", "request body generated");
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d("Okhttp file ", "request successfull");
                Log.d("Okhttp file ", response.body().string());
            } else {
            }

        } catch (IOException e) {
            Log.d("Okhttp file ", "Exception occured");
            e.printStackTrace();
        }

    }

    // enclosing UpdateCallEnd in anew thread
    public void uploadingUpdateCallEnd(int status) {
        new Thread() {
            @Override
            public void run() {
                UpdateCallEnd(status);
            }
        }.start();
    }

    // for sending RefusedCall data to DB after canceling the call
    public void RefusedCall(int status) {
        Log.d("SignallingClient call_H", "call_end_time " + call_end_time);
        Log.d("SignallingClient call_H", "call_status " + status + "");
        OkHttpClient client = new OkHttpClient();
        Log.d("Okhttp file ", "called actual request");
        String url = "http://getstores.net/api/v1/updatecallend";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("call_end_time", call_end_time + "")
                .addFormDataPart("call_id", call_id + "")
                .addFormDataPart("status", status + "")
                .build();
        Log.d("Okhttp file ", "request body generated");
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d("Okhttp file ", "request successfull");
                Log.d("Okhttp file ", response.body().string());
            } else {
            }

        } catch (IOException e) {
            Log.d("Okhttp file ", "Exception occured");
            e.printStackTrace();
        }

    }

    // enclosing UpdateCallEnd in anew thread
    public void uploadingRefusedCall(int status) {
        new Thread() {
            @Override
            public void run() {
                RefusedCall(status);
            }
        }.start();
    }


    // for getting ip address of android device
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    // for getting the caller image when seller receive a call
    @SuppressLint("StaticFieldLeak")
    public void getImg(int id) {
        try {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String bodyData = null;

                    OkHttpClient httpClient = new OkHttpClient();

                    String url = "http://getstores.net/api/v1/user/getimage/" + id;
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = null;
                    try {
                        response = httpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            Log.d("Okhttp file ", "request successfull");
                            bodyData = response.body().string();
                            Log.d("Okhttp file ", "inside doinback  " + bodyData);
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "error in getting response get request okhttp");
                    }
                    return bodyData;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try {
                        parseME(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (Exception e) {
            Log.v("Exception img", e.getMessage() + "");
            e.getMessage();
        }

    }

    public void parseME(String s) throws JSONException {
        try {
            JSONObject v = new JSONObject(s);
            img = v.get("image").toString();
            Log.d("SignallingClient", img);
            Picasso.with(getApplicationContext()).load(Uri.parse(img)).into(callericon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String formatDate(String date) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        Date parsed = null;
        try {
            parsed = sourceFormat.parse(date);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } // => Date is in UTC now

        TimeZone tz = TimeZone.getTimeZone(TimeZone.getDefault().getID());
        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        destFormat.setTimeZone(tz);

        String result = destFormat.format(parsed);
        return result;

    }




    private boolean getPermissionRequest() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return false;
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return false;
            }
        }

        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;

            case REQUEST_MICROPHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    Log.v("SignallingClient permis", "microphone granted mick");

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied mick", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MyVideoCall.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void initViews() {
        caller_text_name = findViewById(R.id.caller_text_name);
        inquery_text = findViewById(R.id.inquery_text);
        hangup = findViewById(R.id.end_call);
        muute = findViewById(R.id.mute);
        swap = findViewById(R.id.swap);
        acceptbtn = findViewById(R.id.Accept);
        Refusebtn = findViewById(R.id.Refuse_btn1);
        localVideoView = findViewById(R.id.local_gl_surface_view);
        callericon = findViewById(R.id.colle_icon);

        Refusebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_end_time = formatDate(String.valueOf(Calendar.getInstance().getTime()));
                uploadingRefusedCall(3);
                SignallingClient.getInstance().canncel();

            }
        });

        ViewGroup.LayoutParams xx = localVideoView.getLayoutParams();
        xx.height = ViewGroup.LayoutParams.MATCH_PARENT;
        xx.width = ViewGroup.LayoutParams.MATCH_PARENT;
        localVideoView.setLayoutParams(xx);
        /*************************************/
        hangup.setOnClickListener(this);
        swap.setOnClickListener(this);
        muute.setOnClickListener(this);
        acceptbtn.setOnClickListener(this);

    }


    private void initVideos() {
        try {
            rootEglBase = EglBase.create();
            localVideoView.init(rootEglBase.getEglBaseContext(), null);
            localVideoView.setZOrderMediaOverlay(true);
        } catch (Exception e) {
            e.getMessage();
            Log.v("Exception", e.getMessage() + "");
        }
    }

    private void getIceServers() {
        // free stun server
/**
 *
 * {'urls': 'stun:88.99.102.247:3478'}
 *             ,{'urls': 'turn:88.99.102.247:3478?transport=udp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:88.99.102.213:3478?transport=udp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:88.99.102.213:3478?transport=tcp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:85.10.200.117:3478?transport=udp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:85.10.200.117:3478?transport=tcp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:41.32.226.208:3478?transport=udp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:41.32.226.208:3478?transport=tcp','credential': 'chanhbc','username': 'chanhbc'}
 *             ,{'urls': 'turn:88.99.102.247:3478?transport=tcp','credential': 'chanhbc','username': 'chanhbc'}
 *
 * stun:stun.l.google.com:19302
 * **/


        String[] stun = {"stun:159.69.165.189:3478"};
        String[] turn = {
                "turn:159.69.165.189:3478?transport=udp"
        };

        for (String c : stun) {
            PeerConnection.IceServer peerIceServer = PeerConnection.IceServer.builder(c)
                    .createIceServer();
            peerIceServers.add(peerIceServer);

        }

        for (String c : turn) {
            PeerConnection.IceServer peerIceServer = PeerConnection.IceServer.builder(c)
                    .setUsername("logappss")
                    .setPassword("logpass")
                    .createIceServer();
            peerIceServers.add(peerIceServer);
        }

    }

    public void start() {

        //Initialize PeerConnectionFactory globals.
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(this)
                        .setEnableVideoHwAcceleration(true)
                        .createInitializationOptions();

        PeerConnectionFactory.initialize(initializationOptions);

        //Create a new PeerConnectionFactory instance - using Hardware encoder and decoder.
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        if (calltype == 100) {
            DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(
                    rootEglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */true,  /* enableH264HighProfile */true);
            DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
            peerConnectionFactory = new PeerConnectionFactory(options, defaultVideoEncoderFactory, defaultVideoDecoderFactory);

            videoCapturerAndroid = createCameraCapturer(new Camera1Enumerator(false));
            videoConstraints = new MediaConstraints();
            audioConstraints = new MediaConstraints();


            //Create a VideoSource instance
            if (videoCapturerAndroid != null) {
                videoSource = peerConnectionFactory.createVideoSource(videoCapturerAndroid);
            }

            localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource);
            //create an AudioSource instance
            audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
            localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource);

            if (videoCapturerAndroid != null) {
                videoCapturerAndroid.startCapture(520, 360, 18);
            }
            localVideoView.setVisibility(View.VISIBLE);

            //create a videoRenderer based on SurfaceViewRenderer instance
            localRenderer = new VideoRenderer(localVideoView);

            // And finally, with our VideoRenderer ready, we
            // can add our renderer to the VideoTrack.
            localVideoTrack.addRenderer(localRenderer);

            localVideoView.setMirror(false);

        } else {
            peerConnectionFactory = new PeerConnectionFactory(options);
            audioConstraints = new MediaConstraints();
            audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
            localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource);


        }


        gotUserMedia = true;
        if (SignallingClient.getInstance().isInitiator) {
            onTryToStart();
        }
    }


    /**
     * This method will be called directly by the app when it is the initiator and has got the local media
     * or when the remote peer sends a message through socket that it is ready to transmit AV data
     */
    @Override
    public void onTryToStart() {

        runOnUiThread(() -> {
            switch (calltype) {
                case 100:
                    if (!SignallingClient.getInstance().isStarted && localVideoTrack != null && SignallingClient.getInstance().isChannelReady) {
                        createPeerConnection();
                        SignallingClient.getInstance().isStarted = true;
                        if (SignallingClient.getInstance().isInitiator) {
                            Log.v("SignallingClient", "start before doCall");
                            doCall();
                        }
                    }
//                    else {
//                        if (SignallingClient.getInstance().isStarted) {
//                            showToast("from communincation is started : true");
//                            Log.v("SignallingClient", "from communincation started");
//                        }
//                        if (localVideoTrack == null) {
//                            showToast("local video track is null");
//                            Log.v("SignallingClient", "from communincation video");
//                        }
//                        if (!SignallingClient.getInstance().isChannelReady) {
//                            showToast("channel not ready");
//                            Log.v("SignallingClient", "from communincation channel");
//                        }
//
//                    }
                    break;
                case 200:
                    if (!SignallingClient.getInstance().isStarted && audioSource != null && SignallingClient.getInstance().isChannelReady) {
                        createPeerConnection();
                        SignallingClient.getInstance().isStarted = true;
                        if (SignallingClient.getInstance().isInitiator) {
                            doCall();
                        }
                    } else {
                        if (SignallingClient.getInstance().isStarted) {
                            showToast(" is started : true");

                        }
                        if (localVideoTrack == null) {
                            showToast("local video track is null");
                        }
                        if (!SignallingClient.getInstance().isChannelReady) {
                            showToast("channel not ready");
                        }

                    }
                    break;

            }
        });
    }


    /**
     * Creating the local peerconnection instance
     */
    private void createPeerConnection() {
        try {
            PeerConnection.RTCConfiguration rtcConfig =
                    new PeerConnection.RTCConfiguration(peerIceServers);
            // TCP candidates are only useful when connecting to a server that supports
            // ICE-TCP.
            rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
            rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
            rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
            rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
            // Use ECDSA encryption.


            rtcConfig.keyType = PeerConnection.KeyType.ECDSA;


            localPeer = peerConnectionFactory.createPeerConnection(rtcConfig, new CustomPeerConnectionObserver("localPeerCreation") {
                @Override
                public void onIceCandidate(IceCandidate iceCandidate) {
                    super.onIceCandidate(iceCandidate);
                    Log.v("ahmed s", iceCandidate.sdp.toString());
                    onIceCandidateReceived(iceCandidate);
                }

                @Override
                public void onAddStream(MediaStream mediaStream) {
//                showToast("Received Remote stream");
                    super.onAddStream(mediaStream);
                    gotRemoteStream(mediaStream);
                }
            });

            addStreamToLocalPeer();
        } catch (Exception e) {
            e.getMessage();
            Log.v("SignallingClient", "from docall ");
        }
    }

    /**
     * Adding the stream to the localpeer
     */
    private void addStreamToLocalPeer() {
        //creating local mediastream
        MediaStream stream = peerConnectionFactory.createLocalMediaStream("102");
        if (calltype == 100)
            stream.addTrack(localVideoTrack);

        stream.addTrack(localAudioTrack);
        localPeer.addStream(stream);
    }

    /**
     * This method is called when the app is initiator - We generate the offer and send it over through socket
     * to remote peer
     */
    private void doCall() {
        switch (calltype) {
            case 100:

                localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {
                        super.onCreateSuccess(sessionDescription);
                        localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
                        Log.d("onCreateSuccess", "SignallingClient emit ");
                        SignallingClient.getInstance().emitMessage(sessionDescription);
                        Log.d("onCreateSuccess", "SignallingClient : " + sessionDescription.toString());
                    }
                }, videoConstraints);
                break;
            case 200:
                localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {
                        super.onCreateSuccess(sessionDescription);
                        localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
                        Log.d("onCreateSuccess", "SignallingClient emit ");
                        SignallingClient.getInstance().emitMessage(sessionDescription);
                    }
                }, audioConstraints);
                break;
        }
    }

    /**
     * Received remote peer's media stream. we will get the first video track and render it
     */
    private void gotRemoteStream(MediaStream stream) {
        //we have remote video stream. add to the renderer.
//        stream.audioTracks.get(0).setVolume(100);
//        AudioTrack audioTrack=stream.audioTracks.get(0);
//        audioTrack.setEnabled(true);
        callstarted = true;

        Log.v(TAG, "number of video media stream : " + stream.videoTracks.size());
        Log.v(TAG, "number of Audio  media stream : " + stream.audioTracks.size());

//        final VideoTrack videoTrack = stream.videoTracks.get(0);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(true);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);


        runOnUiThread(() -> {
            try {
                if (calltype == 100) {
                    VideoTrack videoTrack = stream.videoTracks.get(0);
                    remoteVideoView = findViewById(R.id.remote_gl_surface_view);
                    VideoRenderer remoteRenderer = new VideoRenderer(remoteVideoView);
                    remoteVideoView.init(rootEglBase.getEglBaseContext(), null);
                    remoteVideoView.setZOrderMediaOverlay(true);
                    remoteVideoView.setVisibility(View.VISIBLE);

                    updateVideoViews(true);
                    localVideoView.setVisibility(View.GONE);
                    localVideoView.setVisibility(View.VISIBLE);
                    videoTrack.addRenderer(remoteRenderer);
                } else {
                    AudioTrack audioTrack = stream.audioTracks.get(0);
                    audioTrack.setVolume(50);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("Exception", e.getMessage() + "");
            }

        });

    }


    /**
     * Received local ice candidate. Send it to remote peer through signalling for negotiation
     */
    public void onIceCandidateReceived(IceCandidate iceCandidate) {
        //we have received ice candidate. We can set it to the other peer.
        Log.v("ahmed", iceCandidate.serverUrl.toString());
        SignallingClient.getInstance().emitIceCandidate(iceCandidate);
    }

    /**
     * SignallingCallback - called when the room is created - i.e. you are the initiator
     */
    @Override
    public void onCreatedRoom() {
        showToast("You created the room " + gotUserMedia);
        if (gotUserMedia) {
            SignallingClient.getInstance().emitMessage(channelName);
        }
        else {
            SignallingClient.getInstance().emitMessage(channelName);

        }
    }

    /**
     * SignallingCallback - called when you join the room - you are a participant
     */
    @Override
    public void onJoinedRoom() {
        showToast("You joined the room " + gotUserMedia);
        if (gotUserMedia) {
            SignallingClient.getInstance().emitMessage(channelName);
            showToast("emited channel : " + channelName);

        } else {
            showToast("there is no media stream");

        }
    }

    @Override
    public void onNewPeerJoined() {
        showToast("Remote Peer Joined");
        runOnUiThread(() -> {
            acceptbtn.setVisibility(View.GONE);
            callericon.setVisibility(View.GONE);
            Refusebtn.setVisibility(View.GONE);
            findViewById(R.id.remote_gl_surface_view).setVisibility(View.VISIBLE);
            findViewById(R.id.local_gl_surface_view).setVisibility(View.VISIBLE);
            hangup.setVisibility(View.VISIBLE);
            muute.setVisibility(View.VISIBLE);
            swap.setVisibility(View.VISIBLE);
        });


    }







    @Override
    public void CallCanceled() {
        runOnUiThread(() -> {
            Log.v(TAG, "cancel call");
            hangup();
            onBackPressed();
        });
    }






    /**
     * SignallingCallback - Called when remote peer sends offer
     */
    @Override
    public void onOfferReceived(final JSONObject data) {
        Log.v(TAG, "Received Offer");
        runOnUiThread(() -> {
            if (!SignallingClient.getInstance().isInitiator && !SignallingClient.getInstance().isStarted) {
                onTryToStart();
            }

            try {
                localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"),
                        new SessionDescription(SessionDescription.Type.OFFER,
                                data.getString("sdp")));
                doAnswer();
                //updateVideoViews(true);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("Exception", e.getMessage() + "");
            }
        });
    }

    private void doAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"), sessionDescription);
                SignallingClient.getInstance().emitMessage(sessionDescription);
            }
        }, new MediaConstraints());
    }

    /**
     * SignallingCallback - Called when remote peer sends answer to your offer
     */

    @Override
    public void onAnswerReceived(JSONObject data) {
        showToast("Received Answer");
        try {
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("type").toLowerCase()), data.getString("sdp")));
            //updateVideoViews(true);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("Exception", e.getMessage() + "");
        }
    }

    /**
     * Remote IceCandidate received
     */
    @Override
    public void onIceCandidateReceived(JSONObject data) {
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) {
            Log.v(TAG, e.getMessage());
        }
    }


    private void updateVideoViews(final boolean remoteVisible) {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = localVideoView.getLayoutParams();
            if (remoteVisible) {
                params.height = dpToPx(150);
                params.width = dpToPx(150);

            } else {
                params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            localVideoView.setLayoutParams(params);
        });

    }


    /**
     * Closing up - normal hangup and app destroye
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_call:
                hangup();
                break;
            case R.id.swap:
                /// swap camera
                try {
                    switchCamera();
                } catch (Exception e) {
                    Log.d("cus", e.getMessage());
                }
                break;

            case R.id.mute:
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

                if (audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                    //  Toasty.success(this, "speaker off", Toast.LENGTH_SHORT, true).show();
                } else {
                    audioManager.setSpeakerphoneOn(true);
                    // Toasty.success(this, "speaker on", Toast.LENGTH_SHORT, true).show();

                }

                break;

            case R.id.Refuse_btn1: {
                Log.v("SignallingClient", "refused call");
                finish();
                break;
            }


            case R.id.Accept:
                try {


                    break;
                } catch (Exception e) {
                    e.getMessage();
                    Log.v("Exception", e.getMessage() + "");
                }


        }
    }


    private void hangup() {
        try {
            call_end_time = formatDate(String.valueOf(Calendar.getInstance().getTime()));
            uploadingUpdateCallEnd(2);
            localPeer.dispose();
//            localPeer.close();
            localPeer = null;
            peerIceServers = null;
            videoSource.dispose();
            remoteVideoView.release();
            localVideoView.release();

            rootEglBase.releaseSurface();
            rootEglBase.release();
            peerConnectionFactory.dispose();
            audioManager.setMode(AudioManager.MODE_NORMAL);
            videoCapturerAndroid = null;
            SignallingClient.getInstance().canncel();
            SignallingClient.getInstance().close();


            SignallingClient.getInstance().canncel();




//            initViews();
//            initVideos();
//
// getIceServers();
//            start();


//            Toast.makeText(this,"on hug s: "+channelName+" d : "+dest,Toast.LENGTH_SHORT).show();
//            SignallingCall.getInstance().endcall(channelName, dest);

//            SignallingClient.getInstance().close(channelName);

//            super.onBackPressed();
//            startActivity(new Intent(this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK));

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Exception", e.getMessage() + "");
            super.onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            audioManager.setMode(AudioManager.MODE_NORMAL);
//           SignallingClient.getInstance().close();
//            SignallingCall.getInstance().close();

//            hangup();
//            Toast.makeText(this, "on pause", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }


    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
            e.getMessage();
            Log.v("Exception", e.getMessage() + "");
        }

    }

    /**
     * Util Methods
     */
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();


        // First, try to find front facing camera
        Logging.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    cameraface = true;
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Logging.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                CameraVideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    cameraface = false;
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private VideoCapturer createCameraCapturer2(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();


//        // First, try to find front facing camera
//        Logging.d(TAG, "Looking for front facing cameras.");
//        for (String deviceName : deviceNames) {
//            if (enumerator.isFrontFacing(deviceName)) {
//                Logging.d(TAG, "Creating front facing camera capturer.");
//                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
//
//                if (videoCapturer != null) {
//                    cameraface = true;
//                    return videoCapturer;
//                }
//            }
//        }

        // Front facing camera not found, try something else
        Logging.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                CameraVideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    cameraface = false;
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private void switchCamera() {
        if (videoCapturerAndroid != null) {
            if (videoCapturerAndroid instanceof CameraVideoCapturer) {
                CameraVideoCapturer cameraVideoCapturer = (CameraVideoCapturer) videoCapturerAndroid;
                cameraVideoCapturer.switchCamera(null);
            } else {
                // Will not switch camera, video capturer is not a camera
            }
        }
    }

    @Override
    public void onBackPressed() {


    }
}


