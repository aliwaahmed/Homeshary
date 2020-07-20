package com.customer.shary.live.DisplayProduct;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bumptech.glide.Glide;
import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.DisplayProduct.Comments.Adapter_comment;
import com.customer.shary.live.DisplayProduct.Comments.datamodelComment;
import com.customer.shary.live.DisplayProduct.Seller_Product_And_Related.Adapter_relateed;
import com.customer.shary.live.DisplayProduct.Seller_Product_And_Related.Adapter_saller;
import com.customer.shary.live.DisplayProduct.Seller_Product_And_Related.videoscallback;
import com.customer.shary.live.GetingDeviceInfo.GettingDeviceInfo;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.ConntedToSocket;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.Sockets.SocketCallBack;
import com.customer.shary.live.ads.loadads;
import com.customer.shary.live.auth.main;
import com.customer.shary.live.cart.Cart_Activity;
import com.customer.shary.live.googlemaps.googlemaps;
import com.customer.shary.live.payment.payment;
import com.customer.shary.live.ui.chat_history.chat.chat;
import com.customer.shary.live.ui.home.VideoCallAndVoiceCall.MyVideoCall;
import com.customer.shary.live.ui.home.datamodel.apidata;
import com.logapps.videoplayer.JCVideoPlayerStandard;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class DisplayVideoActivity extends AppCompatActivity
        implements SocketCallBack, videoscallback {

    JCVideoPlayerStandard jcVideoPlayerStandard;
    ConstraintLayout myView;
    ConntedToSocket conntedToSocket;
    VideoView videoView;
    String path;
    private TextView adsCount;
    private TextView skip;
    private android.os.CountDownTimer CountDownTimer;
    private ConstraintLayout constraintLayout;
    private ImageView closetxtads;
    private LinearLayout adstxt;

    public static int cart_product = 0;

    private ImageView Ads_Icon;
    public ArrayList<com.customer.shary.live.ui.home.datamodel.apidata> cart_product_list = new ArrayList<>();
    Context context;
    URLConnection conn;
    URL u;
    Thread ads;
    private TextView oldprice;
    boolean flag = false;

    private de.hdodenhof.circleimageview.CircleImageView storeImage;
    private TextView title_ads, Description;
    private GettingDeviceInfo getDeviceInfo;
    private ArrayList<datamodelComment> datamodelComments = new ArrayList<>();
    private LinearLayout addcart, _lin_chat, _lin_payment, _linCall;
    private TextView StoreName, display_desc, like, Share, newprice;
    private RecyclerView _next_videos, _Related, _comment;
    private Adapter_relateed Adapter_Related;
    private Adapter_saller adapter_Seller_Product;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager_related, lincomment;
    private Adapter_comment adapter_comment;
    private SignallingClient signallingClient;
    public static TextView cart_number, _add_to_cart, cahnnelname_toolbal;
    private ImageView _imgsendcomment, backbtn, cart, _share_btn;
    private EditText _commenttext;
    private ImageView _Follow;


    //TODO:RELATED PRODUCT
    private ArrayList<String> img;
    private ArrayList<String> list;
    private apidata apidata;
    private MediaObject mediaObjects;
    private ArrayList<apidata> Objects;


    //TODO:saller_products
    private ArrayList<String> img_saller;
    private ArrayList<String> list_saller;
    private apidata apidata_saller;
    private MediaObject mediaObjects_saller;
    private ArrayList<apidata> Objects_saller;


    public static Context context1;
    private SharedPreferences prefs;

    Observer<String> stringObserver;
    private String product_id;
    private boolean follow;
    private LinearLayout _lin_location;

    private TextView product_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);
        context1 = this;
        loadads.getInstance().mInterstitialAd(this);


        prefs = getSharedPreferences("login", MODE_PRIVATE);


        //TODO :FINIDVIEW BYID
        product_name=findViewById(R.id.product_name);
        storeImage = findViewById(R.id.storeImage);
        StoreName = findViewById(R.id.storename);
        display_desc = findViewById(R.id.display_desc);
        _linCall = findViewById(R.id._linCall);
        Share = findViewById(R.id.share);
        newprice = findViewById(R.id.newprice);
        cahnnelname_toolbal = findViewById(R.id.cahnnelname_toolbal);
        _add_to_cart = findViewById(R.id._add_to_cart);
        _next_videos = findViewById(R.id._next_videos);
        _Related = findViewById(R.id._top_Related);
        _comment = findViewById(R.id._comment);
        _lin_payment = findViewById(R.id._lin_payment);
        _imgsendcomment = findViewById(R.id._imgsendcomment);
        _commenttext = findViewById(R.id._commenttext);
        _Follow = findViewById(R.id._Follow);
        _share_btn = findViewById(R.id._share_btn);
        backbtn = findViewById(R.id.backbtn);
        cart = findViewById(R.id.imageView2);
        _lin_chat = findViewById(R.id._lin_chat);
        cart_number = findViewById(R.id.cart_number);
        addcart = findViewById(R.id.addcart);
        _lin_location = findViewById(R.id._lin_location);


        //TODO : ADS
        adstxt = findViewById(R.id.lin);
        closetxtads = findViewById(R.id.close);
        constraintLayout = findViewById(R.id.adstext);
        myView = (ConstraintLayout) findViewById(R.id.constraint);
        myView.setVisibility(View.GONE);
        adsCount = findViewById(R.id.adscount);
        skip = findViewById(R.id.skip);
        title_ads = findViewById(R.id.title_ads);
        Ads_Icon = findViewById(R.id.imageView);
        Description = findViewById(R.id.discription);
        oldprice = findViewById(R.id.oldprice);


        //TODO : INIT REC
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        linearLayoutManager_related = new LinearLayoutManager(this);
        linearLayoutManager_related.setOrientation(RecyclerView.HORIZONTAL);
        lincomment = new LinearLayoutManager(this);
        lincomment.setReverseLayout(true);
        signallingClient = new SignallingClient();
        signallingClient.init();
        getDeviceInfo = GettingDeviceInfo.getInstance(this);
        conntedToSocket = ConntedToSocket.getInstance();
        conntedToSocket.SocketCallback(this);
        context = this;
        conntedToSocket.Send_txt_ads_request();


        display_desc.setText(getIntent().getExtras().getString("description"));
        Share.setText(getIntent().getStringExtra("shares"));
        StoreName.setText(getIntent().getExtras().getString("store_name"));
        cahnnelname_toolbal.setText(getIntent().getExtras().getString("store_name"));
        cart_number.setText(String.valueOf(cart_product));
        oldprice.setText(getIntent().getStringExtra("oldprice"));
        newprice.setText(getIntent().getStringExtra("newprice"));
        product_id = String.valueOf(getIntent().getExtras().getInt("product_id"));

        if (getIntent().getExtras().getString("follow") != null) {
            if (getIntent().getExtras().getString("follow").equals("true")) {

                _Follow.setBackground(getResources().getDrawable(R.drawable.followed));
                follow = true;
            } else {
                follow = false;
                _Follow.setBackground(getResources().getDrawable(R.drawable.follo));
            }
        }
        //TODO : LOAD API
        Load_Comments(String.valueOf(getIntent().getExtras().getInt("product_id")));

        //TODO : load saller_product
        Saller_product(String.valueOf(getIntent().getExtras().getString("seller_id")));

        //TODO :load Related_product
        Related_product(product_id);

        Log.e("PRODUCTID", String.valueOf(getIntent().getExtras().getInt("product_id")));


        Glide.with(this).load(getIntent().getStringExtra("storeimage")).into(storeImage);



                product_name.setText( getIntent().getStringExtra("name"));


        cart_number.setVisibility(View.VISIBLE);

        cart_number.setText(String.valueOf(cart_product));

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());

                Log.e("CartLISt", cart_product_list.toString());

                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    if (flag == true) {
                        flag = false;
//                        if (cart_product > 1) {
//
//                            cart_product -= 1;
//                            cart_number.setText(String.valueOf(cart_product));
//                            cart_number.setVisibility(View.VISIBLE);
//                            _add_to_cart.setText(R.string.addtocrat);
                        SignallingClient.getInstance(getApplicationContext()).RemoveFromCart(prefs.getString("login_id", ""),
                                String.valueOf(getIntent().getExtras().getInt("productid")));
//                            Log.e("Sizeeeeeeeeeeeeee", String.valueOf(cart_product_list.size()));
                        _add_to_cart.setText("Add");
                        Related_product(product_id);


                    } else {
                        flag = true;
//                        cart_product += 1;
//                        cart_number.setText(String.valueOf(cart_product));
//                        cart_number.setVisibility(View.VISIBLE);
//                        _add_to_cart.setText(R.string.remove_from_cart);
                        SignallingClient.getInstance(getApplicationContext()).AddCart(String.valueOf(getIntent().getExtras().getInt("productid")),
                                prefs.getString("login_id", "-1"), "1");
//                        Log.e("Sizeeeeeeeeeeeeee", String.valueOf(cart_product_list.size()));

                        _add_to_cart.setText("Remove");
                        Related_product(product_id);


                    }

                }

            }


        });


        Log.e("aliali", getIntent().getExtras().getString("store_name"));


        adstxt.setVisibility(View.INVISIBLE);


        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(getIntent().getExtras().getString("video")
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        jcVideoPlayerStandard.setSaveEnabled(true);
        jcVideoPlayerStandard.fullscreenButton.setVisibility(View.GONE);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());

                videoView.pause();
                myView.setVisibility(View.INVISIBLE);
                jcVideoPlayerStandard.setVisibility(View.VISIBLE);

                if (jcVideoPlayerStandard.startButton.getVisibility() == View.VISIBLE) {
                    jcVideoPlayerStandard.startButton.performClick();
                } else {
                    jcVideoPlayerStandard.startVideo();
                }
                CountDownTimer.cancel();
                conntedToSocket.Send_txt_ads_request();

            }
        });


        jcVideoPlayerStandard.startVideo();


        oldprice.setPaintFlags(oldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        conntedToSocket.Send_Ads_Request();


        String location = getDeviceInfo.GetLocation();
        conntedToSocket.Enter_TO_Show_ads(getDeviceInfo.GetIp(), getDeviceInfo.getMacAddr(), location, String.valueOf(getIntent().getExtras().getInt("productid")));


        adapter_comment = new Adapter_comment(datamodelComments, getApplicationContext());
        _comment.setLayoutManager(lincomment);
        _comment.setAdapter(adapter_comment);


        _imgsendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());

                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    datamodelComment datamodelComment = new datamodelComment();
                    datamodelComment.setContent(_commenttext.getText().toString());
                    datamodelComments.add(datamodelComment);
                    adapter_comment.notifyDataSetChanged();

                    signallingClient.comment(String.valueOf(getIntent().getExtras().getInt("product_id")), prefs.getString("login_id", "-1"),
                            _commenttext.getText().toString());
                    _commenttext.setText("");
                }
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        _Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());


                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    if (follow) {
                        follow = false;

                        _Follow.setBackground(getResources().getDrawable(R.drawable.follo));
                    } else {
                        follow = true;
                        signallingClient.Follow(getIntent().getExtras().getString("seller_id"), prefs.getString("login_id", ""));


                        _Follow.setBackground(getResources().getDrawable(R.drawable.followed));
                    }
                }


            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());

                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    if (videoView != null) {
                        if (videoView.isPlaying() && (videoView != null)) {
                            videoView.pause();
                            videoView.setVisibility(View.GONE);
                        }
                    }
                    AndroidNetworking.cancel("downloadTest");
                    jcVideoPlayerStandard.release();
                    conntedToSocket.CloseAds(String.valueOf(getIntent().getExtras().getInt("productid")));
                    Intent in = new Intent(getApplicationContext(), Cart_Activity.class);
                    in.putParcelableArrayListExtra("products", cart_product_list);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }

            }
        });
        _lin_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    if (videoView != null) {
                        if (videoView.isPlaying() && (videoView != null)) {
                            videoView.pause();
                            videoView.setVisibility(View.GONE);
                        }
                    }
                    AndroidNetworking.cancel("downloadTest");
                    jcVideoPlayerStandard.release();
                    conntedToSocket.CloseAds(getIntent().getStringExtra("productid"));
//                    Intent in = new Intent(getApplicationContext(), chat.class);
//                    in.putExtra("products", getIntent().getExtras().getString("product_id"));
//                    in.putExtra("seller_room", getIntent().getExtras().getString("seller_room"));
//                    in.putExtra("seller_id", getIntent().getExtras().getString("seller_id"));
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(in);


                    _commenttext.requestFocus();

                }
            }
        });
        _lin_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());

                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    if (videoView != null) {
                        if (videoView.isPlaying() && (videoView != null)) {
                            videoView.pause();
                            videoView.setVisibility(View.GONE);
                        }
                    }
                    AndroidNetworking.cancel("downloadTest");
                    jcVideoPlayerStandard.release();

                    conntedToSocket.CloseAds(product_id);
                    Intent in = new Intent(getApplicationContext(), payment.class);

                    in.putExtra("products", product_id);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }
            }
        });


        _share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadads.getInstance().mInterstitialAd(getApplicationContext());

                String description = getIntent().getStringExtra("description");
                String price = getIntent().getStringExtra("newprice");
                String link = getIntent().getStringExtra("product_link");
                String product_img = getIntent().getStringExtra("product_img");
                Log.e("description", description);
                Log.e("price", price);
                Log.e("link", link);
                Log.e("product_img", product_img);

                Intent shareIntent = new Intent();
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, description + "\n" +
                        link
                        + "\n" + "Price "
                        + price + "\n");
               shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "send"));
            }
        });


        _linCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());

                SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    dialog();
                }
            }
        });


        _lin_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadads.getInstance().mInterstitialAd(getApplicationContext());


                Intent map = new Intent(getApplicationContext(), googlemaps.class);

                map.putExtra("saller_id", String.valueOf(getIntent().getExtras().getString("seller_id")));

                startActivity(map);


            }
        });


    }

    @Override
    public void getAds(String s) {
        ads = new Thread(new Runnable() {
            @Override
            public void run() {

                AndroidNetworking.download(s, String.valueOf(getApplicationContext().getCacheDir()), "ads.mp4")
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                // do anything with progress
                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        path = getApplicationContext().getCacheDir() + "/" + "ads.mp4";
                                        load_Video();
                                    }
                                });
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });


            }
        });
        ads.start();


    }

    @Override
    public void txtads(String img, String title1, String description) {


        Log.e("title", title1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adstxt.setVisibility(View.VISIBLE);

                Animation animation1 =
                        AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
                constraintLayout.setAnimation(animation1);
                animation1.start();

                Description.setText(title1);
                title_ads.setText(description);
                Glide.with(getApplicationContext()).load(img).into(Ads_Icon);
                closetxtads.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("close", "close");

                        adstxt.setVisibility(View.GONE);
                        new CountDownTimer(20000, 1000) {
                            @Override
                            public void onTick(long l) {
                            }

                            @Override
                            public void onFinish() {
                                conntedToSocket.Send_txt_ads_request();
                            }
                        }.start();

                    }
                });
            }
        });


    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();


    }


    public void dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        // ...Irrelevant code for customizing the buttons and title

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.call_dialog, null);
        dialogBuilder.setView(dialogView);

        LinearLayout _voice_call = (LinearLayout) dialogView.findViewById(R.id._voice_call);
        _voice_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+"01015124020"));
                startActivity(callIntent);



            }
        });

        LinearLayout _whats_call =(LinearLayout) dialogView.findViewById(R.id._whats_call);

        _whats_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        });


        LinearLayout mail =(LinearLayout) dialogView.findViewById(R.id.mail);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                gmail();
            }
        });




        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.create().show();
            }
        });
    }
    public void gmail() {


        try {

//            ArrayList<String> emailList;
//            emailList = new ArrayList<>();
//            emailList.add("mailto:alisamihakemy@gmail.com");
//            emailList.add("mailto:alisamihakemy2@gmail.com");
//            emailList.add("mailto:alisamihakemy3@gmail.com");
//            emailList.add("mailto:alisamihakemy4@gmail.com");
//            emailList.add("mailto:alisamihakemy5@gmail.com");
//
//
//
//            Intent email = new  Intent(Intent.ACTION_SEND);
//
//          email.putExtra(android.content.Intent.EXTRA_EMAIL,
//                  new String[] { "someone@gmail.com" });
//
//            email.putExtra(android.content.Intent.EXTRA_SUBJECT, "order");
//            email.putExtra(android.content.Intent.EXTRA_TEXT, product_name.getText().toString());
//
//
//            email.setType("message/rfc822");  //set the email recipient
//
//            startActivity(Intent.createChooser(email, "Choose an Email client :"));









            // TODO : SEND MESSAGE TO APP ADMIN
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto: alisamihakemy@gmail.com"));
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        } catch (Exception e) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("NOT Support ")
                    .setCancelable(false)
                    .setPositiveButton("cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    }).show();
        }


    }

    public void whatsapp() {


        try {
            PackageManager packageManager = this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + "+201030410591" + "&text=" + URLEncoder.encode("hello", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            } else {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("NOT Support ")
                        .setCancelable(false)
                        .setPositiveButton("cancel", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        } catch (Exception e) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("NOT Support ")
                    .setCancelable(false)
                    .setPositiveButton("cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).show();
        }


    }


    public void load_Video() {


        jcVideoPlayerStandard.setVisibility(View.GONE);
        myView.setVisibility(View.VISIBLE);
        //     frameLayout.removeView(myView);
        //     frameLayout.addView(myView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
        myView.setAnimation(animation);
        myView.setVisibility(View.VISIBLE);

        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(path);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                videoView.start();
                adstxt.setVisibility(View.GONE);


                final int[] adsCounttime = {(videoView.getDuration() + 1000) / 1000};
                CountDownTimer = new CountDownTimer(videoView.getDuration(), 1000) {
                    public void onTick(long millisUntilFinished) {
                        adsCounttime[0]--;
                        Log.e("count", String.valueOf(adsCounttime[0]));
                        adsCount.setText(String.valueOf(adsCounttime[0]));
                        if (adsCounttime[0] <= 0) {
                            videoView.pause();
                            myView.setVisibility(View.INVISIBLE);
                            jcVideoPlayerStandard.setVisibility(View.VISIBLE);

                            if (jcVideoPlayerStandard.startButton.getVisibility() == View.VISIBLE) {
                                jcVideoPlayerStandard.startButton.performClick();
                            } else {
                                jcVideoPlayerStandard.startVideo();
                            }
                            CountDownTimer.cancel();
                            conntedToSocket.Send_txt_ads_request();
                        }

                    }

                    public void onFinish() {
                    }
                }.start();
            }
        });


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //  frameLayout.removeView(myView);
                Log.e("complete", "complete");
                videoView.pause();
                myView.setVisibility(View.INVISIBLE);
                jcVideoPlayerStandard.setVisibility(View.VISIBLE);

                if (jcVideoPlayerStandard.startButton.getVisibility() == View.VISIBLE) {
                    jcVideoPlayerStandard.startButton.performClick();
                } else {
                    jcVideoPlayerStandard.startVideo();
                }
                if (CountDownTimer != null) {
                    CountDownTimer.cancel();
                }
                conntedToSocket.Send_txt_ads_request();


            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                myView.setVisibility(View.GONE);
                videoView.pause();
                myView.setVisibility(View.INVISIBLE);
                jcVideoPlayerStandard.setVisibility(View.VISIBLE);

                if (jcVideoPlayerStandard.startButton.getVisibility() == View.VISIBLE) {
                    jcVideoPlayerStandard.startButton.performClick();
                } else {
                    jcVideoPlayerStandard.startVideo();
                }
                if (CountDownTimer != null) {
                    CountDownTimer.cancel();
                }
                conntedToSocket.Send_txt_ads_request();
                return false;
            }
        });


    }


    private String downloadfile(String vidurl) {

        File file = new File(getCacheDir(), "ads");
        if (file.exists()) {
            file.delete();
        }
        try {
            u = new URL(vidurl);
            conn = u.openConnection();
            int contentLength = conn.getContentLength();
            DataInputStream stream = new DataInputStream(u.openStream());
            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();
            DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));
            fos.write(buffer);
            fos.flush();
            fos.close();

            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            return null; // swallow a 404
        } catch (IOException e) {
            return null; // swallow a 404
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ((videoView != null)) {

            if (videoView.isPlaying()) {
                videoView.pause();
                videoView.setVisibility(View.GONE);
            }
            AndroidNetworking.cancel("downloadTest");
            conntedToSocket.CloseAds(String.valueOf(getIntent().getExtras().getInt("productid")));
        }
        jcVideoPlayerStandard.release();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoView != null) {
            if (videoView.isPlaying() && (videoView != null)) {
                videoView.pause();
                videoView.setVisibility(View.GONE);
            }
        }
        AndroidNetworking.cancel("downloadTest");
        conntedToSocket.CloseAds(String.valueOf(getIntent().getExtras().getInt("productid")));
        jcVideoPlayerStandard.release();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            if (videoView.isPlaying() && (videoView != null)) {
                videoView.pause();
                videoView.setVisibility(View.GONE);
            }
        }
        AndroidNetworking.cancel("downloadTest");
        conntedToSocket.CloseAds(String.valueOf(getIntent().getExtras().getInt("productid")));
        jcVideoPlayerStandard.release();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoView != null) {
            if (videoView.isPlaying() && (videoView != null)) {
                videoView.pause();
                videoView.setVisibility(View.GONE);
            }
        }
        AndroidNetworking.cancel("downloadTest");
        jcVideoPlayerStandard.release();

        conntedToSocket.CloseAds(String.valueOf(getIntent().getExtras().getInt("productid")));

//
//        Display d = getWindowManager()
//                .getDefaultDisplay();
//        Point p = new Point();
//        d.getSize(p);
//        int width = p.x;
//        int height = p.y;
//
//        Rational ratio = new Rational(width, height);
//        PictureInPictureParams.Builder
//                pip_Builder
//                = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            pip_Builder = new PictureInPictureParams
//                    .Builder();
//            pip_Builder.setAspectRatio(ratio).build();
//
//            enterPictureInPictureMode(pip_Builder.build());
//        }


    }

    public void Load_Comments(String productid) {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/product_comments/" + productid)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {


                String res = response.body().string();
                Log.e("res", res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        datamodelComment datamodelComment = new datamodelComment();


                        datamodelComment.setId(jsonArray.getJSONObject(i).getString("id"));
                        datamodelComment.setUser_id(jsonArray.getJSONObject(i).getString("user_id"));
                        datamodelComment.setName(jsonArray.getJSONObject(i).getString("user_name"));
                        datamodelComment.setContent(jsonArray.getJSONObject(i).getString("content"));
                        datamodelComment.setImg(jsonArray.getJSONObject(i).getString("user_image"));

                        datamodelComments.add(datamodelComment);

                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter_comment = new Adapter_comment(datamodelComments, getApplicationContext());
                            _comment.setLayoutManager(lincomment);
                            _comment.setAdapter(adapter_comment);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public void video_clicked_related(String product_id, int newpos) {

        Intent intent = new Intent(getApplicationContext(), DisplayVideoActivity.class);
        jcVideoPlayerStandard.onStatePause();
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }
        int pos = 0;
        for (int i = 0; i < Objects.size(); i++) {
            if (String.valueOf(Objects.get(i).getProduct_id()).equals(product_id)) {
                pos = i;
            }
        }
        intent.putExtra("video", Objects.get(pos).getMediaObject().getMedia_url());
        intent.putExtra("store_name", Objects.get(pos).getStore_name());
        intent.putExtra("storeimage", Objects.get(pos).getStore_image());
        intent.putExtra("channelname", Objects.get(pos).getStore_name());
        intent.putExtra("description", Objects.get(pos).getDetails());
        intent.putExtra("likes", Objects.get(pos).getLikes());
        intent.putExtra("desc", Objects.get(pos).getDetails());
        intent.putExtra("shares", Objects.get(pos).getShare());
        intent.putExtra("views", Objects.get(pos).getViews());
        intent.putExtra("newprice", Objects.get(pos).getNew_price());
        intent.putExtra("oldprice", Objects.get(pos).getPrice());
        intent.putExtra("productid", Objects.get(pos).getProduct_id());
        intent.putExtra("seller_id", Objects.get(pos).getSeller_id());
        intent.putExtra("product_id", Objects.get(pos).getProduct_id());
        intent.putExtra("category", Objects.get(pos).getCategory());
        intent.putExtra("product_link", Objects.get(pos).getProduct_link());
        intent.putExtra("product_img", Objects.get(pos).getMediaObject().getThumbnail());
        intent.putExtra("pos", pos);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    public void video_clicked_saller(String product_id, int pos1) {
        Intent intent = new Intent(getApplicationContext(), DisplayVideoActivity.class);
        jcVideoPlayerStandard.onStatePause();
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }
        int pos = 0;
        for (int i = 0; i < Objects_saller.size(); i++) {
            if (String.valueOf(Objects_saller.get(i).getProduct_id()).equals(product_id)) {
                pos = i;
            }
        }

        intent.putExtra("video", Objects_saller.get(pos).getMediaObject().getMedia_url());
        intent.putExtra("store_name", Objects_saller.get(pos).getStore_name());
        intent.putExtra("storeimage", Objects_saller.get(pos).getStore_image());
        intent.putExtra("channelname", Objects_saller.get(pos).getStore_name());
        intent.putExtra("description", Objects_saller.get(pos).getDetails());
        intent.putExtra("likes", Objects_saller.get(pos).getLikes());
        intent.putExtra("desc", Objects_saller.get(pos).getDetails());
        intent.putExtra("shares", Objects_saller.get(pos).getShare());
        intent.putExtra("views", Objects_saller.get(pos).getViews());
        intent.putExtra("newprice", Objects_saller.get(pos).getNew_price());
        intent.putExtra("oldprice", Objects_saller.get(pos).getPrice());
        intent.putExtra("productid", Objects_saller.get(pos).getProduct_id());
        intent.putExtra("seller_id", Objects_saller.get(pos).getSeller_id());
        intent.putExtra("product_id", Objects_saller.get(pos).getProduct_id());
        intent.putExtra("category", Objects_saller.get(pos).getCategory());
        intent.putExtra("product_link", Objects.get(pos).getProduct_link());
        intent.putExtra("product_img", Objects_saller.get(pos).getMediaObject().getThumbnail());
        intent.putExtra("pos", pos);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * @param saller_id
     */
    public void Saller_product(String saller_id) {

        Log.e("saller_id", saller_id);

        Objects_saller = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/products/user/" + saller_id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {


                String res = response.body().string();
                Log.e("aaa", res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {

                        if ((jsonArray.getJSONObject(i).getJSONArray("video").length() > 0)) {

                            img_saller = new ArrayList<>();
                            apidata_saller = new apidata();
                            list_saller = new ArrayList<>();

                            if (i == 0) {
                                apidata_saller.setMediatype(1);
                            }

                            apidata_saller.setMediatype(0);
                            apidata_saller.setUser_rate(jsonArray.getJSONObject(i).getString("user_rate"));
                            apidata_saller.setProduct_link(jsonArray.getJSONObject(i).getString("product_link"));
                            apidata_saller.setProduct_id(jsonArray.getJSONObject(i).getInt("id"));
                            apidata_saller.setName(jsonArray.getJSONObject(i).getString("name"));

                            apidata_saller.setViews(jsonArray.getJSONObject(i).getString("views"));
                            apidata_saller.setDetails(jsonArray.getJSONObject(i).getString("details"));
                            apidata_saller.setCategory(jsonArray.getJSONObject(i).getString("category"));
                            apidata_saller.setStore_name(jsonArray.getJSONObject(i).getString("store_name"));
                            apidata_saller.setVideo_link(jsonArray.getJSONObject(i).getString("video_link") + jsonArray.getJSONObject(i).getJSONArray("video").get(0));
                            apidata_saller.setLikes(jsonArray.getJSONObject(i).getString("likes"));
                            apidata_saller.setUser_store_follow(jsonArray.getJSONObject(i).getString("user_store_follow"));
                            apidata_saller.setUser_share(jsonArray.getJSONObject(i).getString("user_share"));
                            apidata_saller.setComment_number(jsonArray.getJSONObject(i).getInt("comments"));
                            apidata_saller.setSeller_id(jsonArray.getJSONObject(i).getString("user_id"));
                            apidata_saller.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            apidata_saller.setNew_price(jsonArray.getJSONObject(i).getString("new_price"));
                            apidata_saller.setUser_like(jsonArray.getJSONObject(i).getString("user_like"));
                            apidata_saller.setStore_image(jsonArray.getJSONObject(i).getString("store_image"));
                            apidata_saller.setImage_link(jsonArray.getJSONObject(i).getString("video_thumb"));
                            apidata_saller.setShare(jsonArray.getJSONObject(i).getString("share"));
                            apidata_saller.setRate_count(String.valueOf(jsonArray.getJSONObject(i).getInt("rate")));
                            apidata_saller.setVideo(list);
                            for (int x = 0; x < jsonArray.getJSONObject(i).getJSONArray("video").length(); x++) {

                                list_saller.add(apidata_saller.getVideo_link() + jsonArray.getJSONObject(i).getJSONArray("video").get(x));
                            }

                            mediaObjects_saller = new MediaObject("",
                                    apidata_saller.getVideo_link()
                                    , apidata_saller.getImage_link(), "");
                            apidata_saller.setMediaObject(mediaObjects_saller);
                            Objects_saller.add(apidata_saller);


                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter_Seller_Product = new Adapter_saller(DisplayVideoActivity.this,
                                    Objects_saller, getApplicationContext());
                            _next_videos.setLayoutManager(linearLayoutManager);
                            _next_videos.setAdapter(adapter_Seller_Product);
                            adapter_Seller_Product.notifyDataSetChanged();

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                }


            }
        });

    }


    /**
     * @param product_id
     */
    public void Related_product(String product_id) {

        Log.e("product_id_related", product_id);

        Objects = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/product/related/" + product_id + "/" + prefs.getString("login_id", "-1"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {


                String res = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray cart = jsonObject.getJSONArray("cart");

                    Log.e("related_len", String.valueOf(jsonArray.length()));

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            if ((jsonArray.getJSONObject(i).getJSONArray("video").length() > 0)) {

                                img = new ArrayList<>();
                                apidata = new apidata();
                                list = new ArrayList<>();

                                if (i == 0) {
                                    apidata.setMediatype(1);
                                }

                                apidata.setMediatype(0);
                                apidata.setUser_rate(jsonArray.getJSONObject(i).getString("user_rate"));
                                apidata.setProduct_link(jsonArray.getJSONObject(i).getString("product_link"));
                                apidata.setProduct_id(jsonArray.getJSONObject(i).getInt("id"));
                                apidata.setName(jsonArray.getJSONObject(i).getString("name"));
                                apidata.setViews(jsonArray.getJSONObject(i).getString("views"));
                                apidata.setDetails(jsonArray.getJSONObject(i).getString("details"));
                                apidata.setCategory(jsonArray.getJSONObject(i).getString("category"));
                                apidata.setStore_name(jsonArray.getJSONObject(i).getString("store_name"));
                                apidata.setVideo_link(jsonArray.getJSONObject(i).getString("video_link") + jsonArray.getJSONObject(i).getJSONArray("video").get(0));
                                apidata.setLikes(jsonArray.getJSONObject(i).getString("likes"));
                                apidata.setUser_store_follow(jsonArray.getJSONObject(i).getString("user_store_follow"));
                                apidata.setUser_share(jsonArray.getJSONObject(i).getString("user_share"));
                                apidata.setComment_number(jsonArray.getJSONObject(i).getInt("comments"));
                                apidata.setSeller_id(jsonArray.getJSONObject(i).getString("user_id"));
                                apidata.setPrice(jsonArray.getJSONObject(i).getString("price"));
                                apidata.setNew_price(jsonArray.getJSONObject(i).getString("new_price"));
                                apidata.setUser_like(jsonArray.getJSONObject(i).getString("user_like"));
                                apidata.setStore_image(jsonArray.getJSONObject(i).getString("store_image"));
                                apidata.setImage_link(jsonArray.getJSONObject(i).getString("video_thumb"));
                                apidata.setShare(jsonArray.getJSONObject(i).getString("share"));
                                apidata.setRate_count(String.valueOf(jsonArray.getJSONObject(i).getInt("rate")));
                                apidata.setVideo(list);


                                apidata.setVideo(list);
                                for (int x = 0; x < jsonArray.getJSONObject(i).getJSONArray("video").length(); x++) {

                                    list.add(apidata.getVideo_link() + jsonArray.getJSONObject(i).getJSONArray("video").get(x));
                                }

                                mediaObjects = new MediaObject("",
                                        apidata.getVideo_link()
                                        , apidata.getImage_link(), "");
                                apidata.setMediaObject(mediaObjects);

                                Objects.add(apidata);


                            }


                        }


                        Log.e("Objectssize", String.valueOf(Objects.size()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Adapter_Related = new Adapter_relateed(DisplayVideoActivity.this
                                        , Objects, getApplicationContext());
                                _Related.setLayoutManager(linearLayoutManager_related);
                                _Related.setAdapter(Adapter_Related);

                                if (cart.length() > 0) {
                                    cart_number.setVisibility(View.VISIBLE);

                                    cart_product = cart.length();
                                    cart_number.setText(String.valueOf(cart_product));


                                }


                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Related_Video_Error", e.getMessage().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume", "puse");
        if(videoView!=null)
        {
            videoView.pause();
            videoView.setVisibility(View.GONE);
            myView.setVisibility(View.GONE);
            jcVideoPlayerStandard.setVisibility(View.VISIBLE);
            jcVideoPlayerStandard.startVideo();
        }
        _add_to_cart.setText("Add To Cart ");
        Related_product(product_id);
    }

}
