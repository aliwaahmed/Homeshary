package com.customer.shary.live.mNetwork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Network {

    private Context context;
    RequestQueue queue;
    int status;

    public Network(Context context) {
        this.context = context;
    }


    //like button
    public int sendlike(final String user_id, final String productid) {


        queue = Volley.newRequestQueue(context);
        String url = "https://shary.live/api/v1/product_like";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");
                            Log.e("likesss", response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

            //getting params ...
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("product_id", productid);
                return params;
            }
        };
        queue.add(stringRequest);
        return status;
    }

    //dislike button
    public int senddislike(final String user_id, final String productid) {
        queue = Volley.newRequestQueue(context);


        String url = "http://getstores.net/api/v1/product_dislike";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            status = jsonObject.getInt("status");


                            Log.e("dislikeee", response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //getting params ...
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("product_id", productid);
                return params;
            }
        };
        queue.add(stringRequest);
        return status;
    }


    //press follow
    public int follow(final String user_id, final String productid) {

        queue = Volley.newRequestQueue(context);

        String url = "http://getstores.net/api/v1/product_follow";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("followooooooo", response);
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("product_id", productid);
                return params;
            }
        };
        queue.add(request);

        return status ;
    }

    //share button

    public int share(final String user_id, final String productid) {

        queue = Volley.newRequestQueue(context);

        String url = "http://getstores.net/api/v1/product_share";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("shareeeee" , response);
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("product_id", productid);
                return params;
            }
        };
        queue.add(request);

        return status ;

    }


    public int unfollow(final String user_id, final String productid) {

        queue = Volley.newRequestQueue(context);

        String url = "http://getstores.net/api/v1/product_unfollow";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("product_id", productid);
                return params;
            }
        };

        queue.add(request);
        return status ;
    }

    //add comment
    public int uploadComment(final String user_id, final String productid , final String comment) {


        queue = Volley.newRequestQueue(context);
        String url = "https://shary.live/api/v1/product_comment";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");
                            Log.e("commenttttt", response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //getting params ...
            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<String , String>();
                params.put("user_id", user_id);
                params.put("product_id", productid);
                params.put("comment" , comment);
                return params;
            }
        };
        queue.add(stringRequest);
        return status;
    }

}
