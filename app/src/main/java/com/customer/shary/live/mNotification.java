package com.customer.shary.live;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Random;

public  class mNotification {

    //   private static RemoteViews contentView;
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static final int NotificationID = Integer.MAX_VALUE;
    private static NotificationCompat.Builder mBuilder;
    private static NotificationCompat.BigPictureStyle Style ;
    private Context context;
    private String title;
    private String msg;
    private String path;

    /**
     *
     * @param context
     * @param title
     * @param msg
     * @param path
     */
    public mNotification(Context context, String title, String msg, String path) {
        this.context = context;
        this.title = title;
        this.msg = msg;
        this.path = path;

        Handler handler =new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(context).load(path).into(target);
            }
        });
    }


    Target target =new Target() {
        @Override

        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            RunNotification(context,title, msg, path,bitmap);

        }

        @Override
        public void onBitmapFailed(Drawable drawable) {

        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


   private void RunNotification(Context context,String title,String msg,String path,Bitmap bitmap) {


        String channelId = "channel_id";

        Style=new NotificationCompat.BigPictureStyle().setSummaryText(msg);
        Style.bigPicture(bitmap);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context, channelId);

//        contentView = new RemoteViews(context.getPackageName(), R.layout.notificationlayout);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//
//        contentView.setTextViewText(R.id.title,title);
//        contentView.setTextViewText(R.id.text,msg);
//        Intent switchIntent = new Intent(this, BackgroundService.switchButtonListener.class);
//        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 1020, switchIntent, 0);
//        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //     mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false); // to remove notification when swape
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        // mBuilder.setOnlyAlertOnce(true);


        mBuilder.setStyle(Style);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContentText(msg);
        mBuilder.setContentTitle(title);


        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

        }

        notification = mBuilder.build();
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notification);
    }


}
