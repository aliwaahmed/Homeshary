package com.customer.shary.live.ui.live;

import android.Manifest;
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
import com.customer.shary.live.ui.live.CustomPeerConnectionObserver;
import com.customer.shary.live.ui.live.CustomSdpObserver;
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

import static com.customer.shary.live.ui.live.SignallingClient.socket;


public class MyVideoCall extends AppCompatActivity implements View.OnClickListener, SignallingClient.SignalingInterface {
    private PeerConnectionFactory peerConnectionFactory;
    private MediaConstraints audioConstraints;
    private MediaConstraints videoConstraints;
    private VideoSource videoSource;
    private AudioSource audioSource;
    private ImageButton hangup;
    private PeerConnection localPeer;
    private EglBase rootEglBase;
    private boolean gotUserMedia;
    private VideoCapturer videoCapturerAndroid;
    private AudioManager audioManager;
    private List<PeerConnection.IceServer> peerIceServers = new ArrayList<>();
    boolean cameraface = true;
    private String channelName;
    private static MediaPlayer mediaPlayer;
    private final String TAG = getClass().getSimpleName();
    private  SurfaceViewRenderer remoteVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video_call);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hangup = findViewById(R.id.end_call);
        hangup.setOnClickListener(this);

        rootEglBase = EglBase.create();



        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        int id = prefs.getInt("user_ID", 0);



        String sender = prefs.getString("room", "");;
        channelName = sender;


        SignallingClient.getInstance().init(this, sender, id, "s", getApplicationContext());



        SignallingClient.getInstance().startChat();
        getIceServers();
        start();


        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);


        audioManager.setSpeakerphoneOn(false);


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
        String[] turn = {"turn:159.69.165.189:3478?transport=udp"};

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

        //create an AudioSource instance
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints);

        if (videoCapturerAndroid != null) {
            videoCapturerAndroid.startCapture(520, 360, 50);
        }

        //create a videoRenderer based on SurfaceViewRenderer instance

        // And finally, with our VideoRenderer ready, we
        // can add our renderer to the VideoTrack.


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

            if (!SignallingClient.getInstance().isStarted && SignallingClient.getInstance().isChannelReady) {
                createPeerConnection();
                SignallingClient.getInstance().isStarted = true;
                if (SignallingClient.getInstance().isInitiator) {
                    Log.v("SignallingClient", "start before doCall");
                    doCall();
                }
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
                    super.onAddStream(mediaStream);
                    gotRemoteStream(mediaStream);
                }
            });

        } catch (Exception e) {
            e.getMessage();
            Log.v("SignallingClient", "from docall ");
        }
    }


    /**
     * This method is called when the app is initiator - We generate the offer and send it over through socket
     * to remote peer
     */
    private void doCall() {

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

    }


    /**
     * Received remote peer's media stream. we will get the first video track and render it
     */
    private void gotRemoteStream(MediaStream stream) {


        Log.v(TAG, "number of video media stream : " + stream.videoTracks.size());
        Log.v(TAG, "number of Audio  media stream : " + stream.audioTracks.size());

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(false);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);


        runOnUiThread(() -> {
            try {

                VideoTrack videoTrack = stream.videoTracks.get(0);
                remoteVideoView = findViewById(R.id.remote_gl_surface_view);
                VideoRenderer remoteRenderer = new VideoRenderer(remoteVideoView);
                remoteVideoView.init(rootEglBase.getEglBaseContext(), null);
                remoteVideoView.setZOrderMediaOverlay(true);
                remoteVideoView.setVisibility(View.VISIBLE);

                videoTrack.addRenderer(remoteRenderer);

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
        } else {
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
            findViewById(R.id.remote_gl_surface_view).setVisibility(View.VISIBLE);
            hangup.setVisibility(View.VISIBLE);
            mediaPlayer.stop();
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
                localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
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


    /**
     * Closing up - normal hangup and app destroye
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_call:
                hangup();
                break;



        }
    }


    private void hangup() {
        try {
            localPeer.dispose();
            localPeer = null;
            peerIceServers = null;
            videoSource.dispose();
            remoteVideoView.release();

            rootEglBase.releaseSurface();
            rootEglBase.release();
            peerConnectionFactory.dispose();
            audioManager.setMode(AudioManager.MODE_NORMAL);
            videoCapturerAndroid = null;
            SignallingClient.getInstance().canncel();
            SignallingClient.getInstance().close();
            socket.emit("cancel", "logapps");
            SignallingClient.getInstance().canncel();
            onBackPressed();


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

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
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


}


