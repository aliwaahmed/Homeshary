package com.customer.shary.live.GetingDeviceInfo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GettingDeviceInfo {
    private static final GettingDeviceInfo ourInstance = new GettingDeviceInfo();
    private static Context mContext;
    StringBuilder loc ;
    public static GettingDeviceInfo getInstance(Context context) {
        mContext=context;
        return ourInstance;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                Log.e("mac",res1.toString());
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


    public String GetIp()
    {
        try {
            WifiManager wifiManager  = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifiInfo  =wifiManager.getConnectionInfo();
            String ip = Formatter.formatIpAddress(wifiInfo.getIpAddress());

            Log.e("ip", String.valueOf(ip));
            return ip;
        }
        catch (Exception e)
        {
            Log.e("IP Error ", e.getMessage());
            return  null;
        }

    }


    public String GetLocation()
    {

        loc=new StringBuilder();

        try {

            FusedLocationProviderClient client;
            client = LocationServices.getFusedLocationProviderClient(mContext);
            client.getLastLocation().addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    loc.append(String.valueOf(location.getLongitude()));
                    loc.append(",");
                    loc.append(String.valueOf(location.getLatitude()));

                    Log.e("loc",loc.toString());
                }

            }
            );

            return loc.toString();

        }catch (Exception e)
        {
            Log.e("location errror",e.getMessage());
            return null;
        }

    }



}
