package com.customer.shary.live.Sockets;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.customer.shary.live.mNotification;
import com.customer.shary.live.ui.home.datamodel.apidata;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SignallingClient {

    com.customer.shary.live.ui.home.datamodel.apidata apidata;
    private static SignallingClient instance;
    private String roomName = null;
    static Socket socket;
    boolean isChannelReady = false;
    boolean isInitiator = false;
    boolean isStarted = false;
    private chat chat;
    private static SharedPreferences sharedPreferences;
    //This piece of code should not go into production!!
    //This will help in cases where the node server is running in non-https server and you want to ignore the warnings

    public static boolean mboolean = false;
    public static int stoping_call = 0;
    public static String[] caller_data;

    public static Context context;

    public static SignallingClient getInstance(Context context1) {
        if (instance == null) {
            instance = new SignallingClient();
            context = context1;
            sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);


        }
        return instance;
    }


    public void chat(chat chat) {
        this.chat = chat;
    }

    public void init() {
        this.roomName = roomName;


        SSLContext sslcontext = null;
        try {
          //  sslcontext = SSLContext.getInstance("TLS");

        //sslcontext.init(null, trustAllCerts, null);
        //    IO.setDefaultHostnameVerifier((hostname, session) -> true);
          //  IO.setDefaultSSLContext(sslcontext);
            socket = IO.socket("https://shary.live:7005");

            IO.Options options = new IO.Options();
            String result = "";
            StringBuilder query = new StringBuilder("user_id=" + sharedPreferences.getString("login_id", "-5") +
                    "&username=" + sharedPreferences.getString("name", "-1") + "&room=" +
                    sharedPreferences.getString("room", "-1") + "&url=" + "https://shary.live:7005" + "&title=" + "my title");
//            try {
//
//                for (String c : contactsId) {
//                    //Log.v("chatsocket", c);
//                    query.append(c);
//                    query.append(",");
//
//                }
//                query.deleteCharAt(query.lastIndexOf(","));
//                result = query.toString();
//
//                //Log.v("chatsocket", result);
//
//            } catch (Exception e) {
//                //Log.v("chatsocket", e.getMessage());
//                result = query.toString();
//
//            }

            result = query.toString();
            options.query = result;

            socket = IO.socket("https://shary.live:7005", options);
            socket.close();
            if (!socket.connected()) {
                socket.connect();
            }


            Log.i("SignallingClient", socket.connected() + " out");
            socket.emit("asd", "heeeeee");
            if (socket.connected()) {
                Log.i("SignallingClient", socket.connected() + "connec");

            } else {
                Log.i("SignallingClient", socket.connected() + "");

            }
            Log.i("SignallingClient", "init() called");


            socket.on("listenMessageAdmin", args -> {

                Log.e("args[0]", String.valueOf(args[0]));
                Log.e("args[1]", String.valueOf(args[1]));
                Log.e("args[2]", String.valueOf(args[2]));
                Log.e("args[3]", String.valueOf(args[3]));
                chat.recieve_msg(String.valueOf(args[0]),
                        String.valueOf(args[1]),
                        String.valueOf(args[2]),
                        String.valueOf(args[3]));


            });


            socket.on("new_product", args -> {

                String product_img = String.valueOf(args[4]);


                String store_id = String.valueOf(args[0]);
                String user_name = String.valueOf(args[1]);
                String product_id = String.valueOf(args[2]);
                String title = String.valueOf(args[3]);
                Log.e("sadsadsadasdsad", title);

                mNotification mNotification = new mNotification(context, title, user_name, product_img);
                // mNotification.notify();

            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }


//    1-like
//    2-share
//    3- follow




    public void like(String type, String store_id, String user_name, String user_image) {

        socket.emit("new_notification",type,store_id,user_name,user_image);
    }

    /**
     * @param name
     * @param imgUrl
     * @param user_id
     * @param product_id
     */
    public void share(String name, String imgUrl, String user_id, String product_id) {
        socket.emit("new_notification", user_id, product_id, name, imgUrl);
    }

    /**
     * @param name
     * @param imgUrl
     * @param user_id
     * @param product_id
     */
    public void follow(String name, String imgUrl, String user_id, String product_id) {

        socket.emit("new_notification", user_id, product_id, name, imgUrl);
    }


    /**
     * @param user_id
     * @param product_id
     * @param rate
     */
    public void Rate(String user_id, String product_id, String rate) {
        socket.emit("rate", user_id, product_id, rate);
    }

    /**
     * @param product_id
     * @param user_id
     * @param comment
     */
    public void comment(String product_id, String user_id, String comment) {
        socket.emit("new_comment", product_id, user_id, comment);
    }


    /**
     * @param user_id
     * @param product_id
     */
    public void delet_fav(String user_id, String product_id) {
        socket.emit("delete_favourite", user_id, product_id);
    }

    /**
     * @param user_id
     * @param product_id
     */
    public void add_fav(String user_id, String product_id) {
        socket.emit("favourite", user_id, product_id);
    }


    /**
     * @param store_id
     * @param user_id
     */
    public void Follow(String store_id, String user_id) {
        socket.emit("follow", store_id, user_id);
    }

    /**
     * @param product_id
     * @param user_id
     * @param quantity
     */
    public void AddCart(String product_id, String user_id, String quantity) {

        socket.emit("new_cart_product", product_id, user_id, quantity);
    }


    /**
     * @param store_id
     * @param product_id
     * @param user_id
     * @param user_address
     * @param user_phone
     */
    public void buy_now_order(String store_id, String product_id, String user_id, String user_address, String user_phone) {

        Log.e("Store_id", store_id);
        Log.e("product_id", product_id);
        Log.e("user_id", user_id);
        Log.e("user_address", user_address);
        Log.e("user_phone", user_phone);

        socket.emit("product_order", store_id, product_id, user_id, user_address, user_phone);
    }


    /**
     * @param user_id
     * @param product_id
     */
    public void RemoveFromCart(String user_id, String product_id) {
        socket.emit("delete_cart_product", user_id, product_id);

    }

    /**
     * @param user_id
     * @param product_id
     * @param quantity
     */
    public void Edite_cart(String user_id, String product_id, String quantity) {
        socket.emit("edit_cart_product", user_id, product_id, quantity);

    }


    public void close() {
        isChannelReady = false;
        isStarted = false;
        isInitiator = false;
        // instance.roomName = null;
        socket.emit("bye", roomName);
        socket.disconnect();
        socket.close();


        Log.d("SignallingClient", "close method ended");

    }

    public void close(String roomName) {
        socket.emit("bye", roomName);
        Log.v("darwish_get_out", roomName);

    }


    /**
     * @param message
     * @param username
     * @param user_id
     * @param RoomName
     */
    public void send_messsage(String message, String username, String user_id, String RoomName, String to) {

        Log.e("messsage", message);
        Log.e("name", username);
        Log.e("user_id", user_id);
        Log.e("Roomname", RoomName);
        Log.e("To", to);
        socket.emit("sendMessage", message, username, user_id, RoomName, to);
    }


    /**
     * @param Store_id
     * @param User_id
     */
    public void block_chat(String Store_id, String User_id) {
        socket.emit("block", Store_id, User_id);
    }

    /**
     * @param Store_id
     * @param User_id
     */
    public void unblock(String Store_id, String User_id) {
        socket.emit("unblock", Store_id, User_id);
    }

    public void uploaded(String message, String name, String type, String id, String to) {

        socket.emit("sendFileAdmin", message, name, type, id, to);
    }


    public interface SignalingInterface {
        void onRemoteHangUp(String msg);

        void onOfferReceived(JSONObject data);

        void onAnswerReceived(JSONObject data);

        void onIceCandidateReceived(JSONObject data);

        void onTryToStart();

        void onCreatedRoom();

        void onJoinedRoom();

        void onNewPeerJoined();

        void onRoomFull();

        void requestCall(String room, String username, String product_name, int id, String ip);

        void checkroom(int result);

        void CallCanceled();

        void Notification(String title, String Txt, String image, String Store_id);

        void recieve_msg(String message, String username, String type, String to);


    }

    public interface chat {

        /**
         * @param message
         * @param username
         * @param type
         * @param to
         */
        void recieve_msg(String message, String username, String type, String to);


    }

}
