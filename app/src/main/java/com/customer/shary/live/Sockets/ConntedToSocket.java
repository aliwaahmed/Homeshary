package com.customer.shary.live.Sockets;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.customer.shary.live.DisplayProduct.DisplayVideoActivity;
import com.customer.shary.live.MainActivity;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;


public class ConntedToSocket {

    private  static final ConntedToSocket ourInstance = new ConntedToSocket();
    private  SocketCallBack socketCallBack;
    private Socket mSocket;
    private SharedPreferences sharedPreferences ;


    private  ConntedToSocket() {
            try {




                String result = "";
                SharedPreferences sharedPreferences = DisplayVideoActivity.context1.getSharedPreferences("login", Context.MODE_PRIVATE);
                StringBuilder query = new StringBuilder("user_id=" +  sharedPreferences.getString("login_id","-2")+ "&username=" + "ali" + "&room=" + "logapps" + "&url=" + "https://shary.live:7777" + "&title=" + "my title");
                IO.Options options = new IO.Options();
                result = query.toString();
                options.query = result;
                mSocket = IO.socket("https://shary.live:7777",options);
                mSocket.close();
                mSocket.connect();
                Log.e("connect","connect");



            } catch (URISyntaxException e) {

            }
    }

    public static ConntedToSocket getInstance() {
        return ourInstance;
    }

    public void SocketCallback (SocketCallBack socketCallBack )
    {
        this.socketCallBack=socketCallBack;
        mSocket.on("ads", args -> {

            socketCallBack.getAds(String.valueOf(args[0]));

        });
        mSocket.on("text_ads",args -> {

            socketCallBack.txtads(String.valueOf(args[2]),String.valueOf(args[1]),String.valueOf(args[0]));

        });
    }

    public void Send_Ads_Request()
    {
        mSocket.emit("ads_request");
    }

    public void Send_txt_ads_request()
    {
        mSocket.emit("text_ads_request");
    }


    /**
     *
     * @param user_ip
     * @param user_mac_address
     * @param user_location
     * @param product_id
     */
    public void Enter_TO_Show_ads(String user_ip,String user_mac_address,String user_location,String product_id)
    {
        mSocket.emit("view_product_open",user_ip,user_mac_address,user_location,product_id);
    }


    public void CloseAds(String s)
    {
        mSocket.emit("view_product_close",s);
    }





}
