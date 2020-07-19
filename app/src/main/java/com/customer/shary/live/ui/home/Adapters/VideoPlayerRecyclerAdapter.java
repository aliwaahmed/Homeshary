package com.customer.shary.live.ui.home.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import ro.andreidobrescu.emojilike.Emoji;
import ro.andreidobrescu.emojilike.EmojiCellView;
import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.OnEmojiSelectedListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.customer.shary.live.DisplayProduct.DisplayVideoActivity;
import com.customer.shary.live.MainActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.ads.Main2Activity;
import com.customer.shary.live.auth.main;
import com.customer.shary.live.favourite.favourite_Activity;
import com.customer.shary.live.mNetwork.Network;
import com.customer.shary.live.ui.home.Callback.callback;
import com.customer.shary.live.ui.home.datamodel.apidata;
import com.customer.shary.live.ui.home.datamodel.collection;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


import static android.content.Context.MODE_PRIVATE;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<collection> data;
    private RequestManager requestManager;
    Context context;
    com.customer.shary.live.ui.home.Callback.callback callback;
    int pos;
    private MediaPlayer mp;
    Network network;


    SharedPreferences prefs;
    private Activity activity;


    public VideoPlayerRecyclerAdapter(Activity activity, callback callback, Context context, ArrayList<collection> data, RequestManager requestManager) {
        this.data = data;
        this.requestManager = requestManager;
        this.context = context;
        network = new Network(context);
        Log.d("sizeeee", String.valueOf(data.size()));
        this.callback = callback;
        this.activity = activity;
        prefs = context.getSharedPreferences("login", MODE_PRIVATE);

    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getTypeee().equals("1")) {
            return 1;
        } else if (data.get(position).getTypeee().equals("0")) {

            return 0;

        } else if (data.get(position).getTypeee().equals("2")) {

            return 2;

        } else {
            return -1;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        if (i == 1) {
            return new VideoPlayerViewHolder(
                    LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.layout_video_list_item, viewGroup, false), context);

        } else if (i == 0) {
            return new VideoPlayerViewHolder(
                    LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.adv_withus_layout, viewGroup, false), context);

        } else {
            return new VideoPlayerViewHolder(
                    LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.unifiednativeadview, viewGroup, false), context);

        }

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {


        if (data.get(i).getTypeee().equals("1")) {
            pos = i;
            ((VideoPlayerViewHolder) viewHolder).onBind(data.get(i).getApidata().getMediaObject(), requestManager);


            if (data.get(i).getApidata().getDetails().length() > 100) {
                String text = data.get(i).getApidata().getDetails().substring(0, 98) + " <font color=\"yellow\">...More</font>";
                ((VideoPlayerViewHolder) viewHolder).description.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

            } else {
                String text = data.get(i).getApidata().getDetails() + " <font color=\"yellow\">...More</font>";
                ((VideoPlayerViewHolder) viewHolder).description.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            }


            ((VideoPlayerViewHolder) viewHolder)._comment_num.setText(String.valueOf(data.get(i).getApidata().getComment_number()));
            Log.e("erewrewrewrwewr", String.valueOf(data.get(i).getApidata().getComment_number()));
            ((VideoPlayerViewHolder) viewHolder).title.setText(data.get(i).getApidata().getName());
            ((VideoPlayerViewHolder) viewHolder).num_view.setText(data.get(i).getApidata().getViews());
            ((VideoPlayerViewHolder) viewHolder).num_share.setText(data.get(i).getApidata().getShare());
            ((VideoPlayerViewHolder) viewHolder).num_likes.setText(data.get(i).getApidata().getLikes());

            ((VideoPlayerViewHolder) viewHolder).profileName.setText(data.get(i).getApidata().getStore_name());
            ((VideoPlayerViewHolder) viewHolder)._Rate.setText(data.get(i).getApidata().getRate_count());


            ((VideoPlayerViewHolder) viewHolder).description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DisplayVideoActivity.class);

                    intent.putExtra("name",data.get(i).getApidata().getName().toString());
                    intent.putExtra("video", data.get(i).getApidata().getMediaObject().getMedia_url());
                    intent.putExtra("store_name", data.get(i).getApidata().getStore_name());
                    intent.putExtra("storeimage", data.get(i).getApidata().getStore_image());
                    intent.putExtra("channelname", data.get(i).getApidata().getStore_name());
                    intent.putExtra("description", data.get(i).getApidata().getDetails());
                    intent.putExtra("likes", data.get(i).getApidata().getLikes());
                    intent.putExtra("desc", data.get(i).getApidata().getDetails());
                    intent.putExtra("shares", data.get(i).getApidata().getShare());
                    intent.putExtra("views", data.get(i).getApidata().getViews());
                    intent.putExtra("newprice", data.get(i).getApidata().getNew_price());
                    intent.putExtra("oldprice", data.get(i).getApidata().getPrice());
                    intent.putExtra("productid", data.get(i).getApidata().getProduct_id());
                    intent.putExtra("seller_id", data.get(i).getApidata().getSeller_id());
                    intent.putExtra("product_id", data.get(i).getApidata().getProduct_id());
                    intent.putExtra("category", data.get(i).getApidata().getCategory());
                    intent.putExtra("product_link", data.get(i).getApidata().getProduct_link());
                    intent.putExtra("product_img", data.get(i).getApidata().getMediaObject().getThumbnail());
                    intent.putExtra("seller_room", data.get(i).getApidata().getSeller_room());
                    intent.putExtra("follow", data.get(i).getApidata().getUser_store_follow());

                    intent.putExtra("pos", i);

                    intent.putParcelableArrayListExtra("api_data", data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.e("iddddddddddd", String.valueOf(data.get(i).getApidata().getProduct_id()));
                }
            });

            ((VideoPlayerViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DisplayVideoActivity.class);
                    intent.putExtra("name",data.get(i).getApidata().getName().toString());

                    intent.putExtra("video", data.get(i).getApidata().getMediaObject().getMedia_url());
                    intent.putExtra("store_name", data.get(i).getApidata().getStore_name());
                    intent.putExtra("storeimage", data.get(i).getApidata().getStore_image());
                    intent.putExtra("channelname", data.get(i).getApidata().getStore_name());
                    intent.putExtra("description", data.get(i).getApidata().getDetails());
                    intent.putExtra("description", data.get(i).getApidata().getDetails());
                    intent.putExtra("likes", data.get(i).getApidata().getLikes());
                    intent.putExtra("desc", data.get(i).getApidata().getDetails());
                    intent.putExtra("shares", data.get(i).getApidata().getShare());
                    intent.putExtra("views", data.get(i).getApidata().getViews());
                    intent.putExtra("newprice", data.get(i).getApidata().getNew_price());
                    intent.putExtra("oldprice", data.get(i).getApidata().getPrice());
                    intent.putExtra("productid", data.get(i).getApidata().getProduct_id());
                    intent.putExtra("seller_id", data.get(i).getApidata().getSeller_id());
                    intent.putExtra("product_id", data.get(i).getApidata().getProduct_id());
                    intent.putExtra("category", data.get(i).getApidata().getCategory());
                    intent.putExtra("product_link", data.get(i).getApidata().getProduct_link());
                    intent.putExtra("product_img", data.get(i).getApidata().getMediaObject().getThumbnail());
                    intent.putExtra("seller_room", data.get(i).getApidata().getSeller_room());
                    intent.putExtra("follow", data.get(i).getApidata().getUser_store_follow());

                    Log.e("selleeee", data.get(i).getApidata().getSeller_room());

                    intent.putExtra("pos", i);

                    intent.putParcelableArrayListExtra("api_data", data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.e("iddddddddddd", String.valueOf(data.get(i).getApidata().getProduct_id()));
                }
            });

            ((VideoPlayerViewHolder) viewHolder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DisplayVideoActivity.class);
                    intent.putExtra("name",data.get(i).getApidata().getName().toString());

                    intent.putExtra("video", data.get(i).getApidata().getMediaObject().getMedia_url());
                    intent.putExtra("store_name", data.get(i).getApidata().getStore_name());
                    intent.putExtra("storeimage", data.get(i).getApidata().getStore_image());
                    intent.putExtra("channelname", data.get(i).getApidata().getStore_name());
                    intent.putExtra("description", data.get(i).getApidata().getDetails());
                    intent.putExtra("description", data.get(i).getApidata().getDetails());
                    intent.putExtra("likes", data.get(i).getApidata().getLikes());
                    intent.putExtra("desc", data.get(i).getApidata().getDetails());
                    intent.putExtra("shares", data.get(i).getApidata().getShare());
                    intent.putExtra("views", data.get(i).getApidata().getViews());
                    intent.putExtra("newprice", data.get(i).getApidata().getNew_price());
                    intent.putExtra("oldprice", data.get(i).getApidata().getPrice());
                    intent.putExtra("productid", data.get(i).getApidata().getProduct_id());
                    intent.putExtra("seller_id", data.get(i).getApidata().getSeller_id());
                    intent.putExtra("product_id", data.get(i).getApidata().getProduct_id());
                    intent.putExtra("category", data.get(i).getApidata().getCategory());
                    intent.putExtra("product_link", data.get(i).getApidata().getProduct_link());
                    intent.putExtra("product_img", data.get(i).getApidata().getMediaObject().getThumbnail());
                    intent.putExtra("seller_room", data.get(i).getApidata().getSeller_room());
                    intent.putExtra("follow", data.get(i).getApidata().getUser_store_follow());

                    Log.e("selleeee", data.get(i).getApidata().getSeller_room());

                    intent.putExtra("pos", i);

                    intent.putParcelableArrayListExtra("api_data", data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.e("iddddddddddd", String.valueOf(data.get(i).getApidata().getProduct_id()));

                }
            });


            ((VideoPlayerViewHolder) viewHolder)._Fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);

                    if (sharedPreferences.getString("status", "-1").equals("-1")) {
                        Intent intent = new Intent(context, main.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else {
                        Toasty.success(context, "Add Sucessfully").show();

                        SignallingClient.getInstance(context).add_fav(prefs.getString("login_id", "-1"), String.valueOf(data.get(i).getApidata().getProduct_id()));

                    }


//                ((VideoPlayerViewHolder) viewHolder)._Fav.setImageResource(R.drawable.favsharylove);
//                Intent intent = new Intent(view.getContext(), favourite_Activity.class);
//                intent.putExtra("video", data.get(i).getApidata().getMediaObject().getMedia_url());
//                intent.putExtra("store_name", data.get(i).getApidata().getStore_name());
//                intent.putExtra("storeimage", data.get(i).getApidata().getStore_image());
//                intent.putExtra("channelname", data.get(i).getApidata().getStore_name());
//                intent.putExtra("description", data.get(i).getApidata().getDetails());
//                intent.putExtra("likes", data.get(i).getApidata().getLikes());
//                intent.putExtra("desc", data.get(i).getApidata().getDetails());
//                intent.putExtra("shares", data.get(i).getApidata().getShare());
//                intent.putExtra("views", data.get(i).getApidata().getViews());
//                intent.putExtra("newprice", data.get(i).getApidata().getNew_price());
//                intent.putExtra("oldprice", data.get(i).getApidata().getPrice());
//                intent.putExtra("productid", data.get(i).getApidata().getProduct_id());
//                intent.putExtra("seller_id", data.get(i).getApidata().getSeller_id());
//                intent.putExtra("product_id", data.get(i).getApidata().getProduct_id());
//                intent.putExtra("category", data.get(i).getApidata().getCategory());
//                intent.putExtra("product_link", data.get(i).getApidata().getProduct_link());
//                intent.putExtra("product_img", data.get(i).getApidata().getMediaObject().getThumbnail());
//                intent.putExtra("seller_room",data.get(i).getApidata().getSeller_room());
//                intent.putExtra("follow",data.get(i).getApidata().getUser_store_follow());
//                intent.putExtra("pos", i);
//                intent.putParcelableArrayListExtra("api_data", data);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);


                }
            });


            //like & dislike
            if (data.get(i).getApidata().getUser_like().equals("true")) {
                ((VideoPlayerViewHolder) viewHolder).like_btn.setImageResource(R.drawable.like);
            }


            Glide.with(context).load(data.get(i).getApidata().getStore_image()).into(((VideoPlayerViewHolder) viewHolder).profileImg);


            ((VideoPlayerViewHolder) viewHolder).share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                final int REQUEST = 112;

                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        if (!hasPermissions(context, PERMISSIONS)) {
                            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, REQUEST );

                        } else {
                            Target target = new Target() {

                                @Override
                                public void onPrepareLoad(Drawable arg0) {
                                }

                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                                    File file = new File(context.getCacheDir().getPath() + "/" + "fileame.jpg");
                                    try {
                                        file.createNewFile();
                                        FileOutputStream ostream = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                        ostream.close();
                                        Uri imageUri = Uri.parse(file.getAbsolutePath().toString());
                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.setType( "*/*");
                                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                                data.get(i).getApidata().getDetails() + "\n" +
                                                data.get(i).getApidata().getProduct_link()
                                                + "\n" + "Price "
                                                + data.get(i).getApidata().getNew_price() + "\n");
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                                        context.startActivity(Intent.createChooser(shareIntent, "send"));

                                        SignallingClient.getInstance(context).share(prefs.getString("name", "ali") + "\n" + context.getResources().getString(R.string.share100),
                                                data.get(i).getApidata().getMediaObject().getThumbnail(),
                                                prefs.getString("login_id", "-1"), String.valueOf(data.get(i).getApidata().getProduct_id()));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onBitmapFailed(Drawable drawable) {


                                }


                            };

                            Log.e("thumb",data.get(i).getApidata().getMediaObject().getThumbnail().toString());
                            Picasso.with(context).load(data.get(i).getApidata().getMediaObject().getThumbnail()).into(target);

                        }
                    } else {
                        //do here
                    }














                }
            });


            Log.e("product_link", "onBindViewHolder: " + data.get(i).getApidata().getProduct_link());
            ((VideoPlayerViewHolder) viewHolder).like_btn.setFocusable(true);
            ((VideoPlayerViewHolder) viewHolder).like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });


            EmojiConfig.with(activity)
                    .on(((VideoPlayerViewHolder) viewHolder).like_btn)
                    .open(((VideoPlayerViewHolder) viewHolder).emojiLikeView)
                    .addEmoji(new Emoji(R.drawable.love, ""))
                    .addEmoji(new Emoji(R.drawable.like, ""))
                    .setEmojiAnimationSpeed(.2f)
                    .setTouchDownDelay(2)
                    .setTouchUpDelay(5)
                    .setEmojiCellViewFactory(EmojiCellView.WithImageAndText::new)
                    .setOnEmojiSelectedListener(new OnEmojiSelectedListener() {
                        @Override
                        public void onEmojiSelected(Emoji emoji) {

                            if (emoji.getDrawable() == R.drawable.like) {
                                if (mp != null) {
                                    mp.release();
                                }
                                ((VideoPlayerViewHolder) viewHolder).like_btn.setImageResource(emoji.getDrawable());
                                mp = MediaPlayer.create(context, R.raw.reactionsound);
                                mp.start();

                                network.sendlike(prefs.getString("login_id", "-1"), String.valueOf(data.get(i).getApidata().getProduct_id()));


                                    SignallingClient.getInstance(context).like(
                                            "1",data.get(i).getSeller_id(), prefs.getString("name", "ali"),
                                            data.get(i).getApidata().getMediaObject().getThumbnail());



                                callback.position(i, "like");

                            }
                            if (emoji.getDrawable() == R.drawable.love) {
                                if (mp != null) {
                                    mp.release();
                                }
                                ((VideoPlayerViewHolder) viewHolder).like_btn.setImageResource(emoji.getDrawable());
                                mp = MediaPlayer.create(context, R.raw.reactionsound);
                                mp.start();

                                network.sendlike(prefs.getString("login_id", "-1"), String.valueOf(data.get(i).getApidata().getProduct_id()));

                                SignallingClient.getInstance(context).like(
                                        "1",data.get(i).getSeller_id(), prefs.getString("name", "ali"),
                                        data.get(i).getApidata().getMediaObject().getThumbnail());




                                callback.position(i, "like");

                            }
                        }

                    })
                    .setBackgroundImageResource(R.drawable.bg_overlay).setup();

            ((VideoPlayerViewHolder) viewHolder).Rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.get(i).getApidata().getUser_rate().equals("false")) {

                        dialog(prefs.getString("login_id", "-1"), String.valueOf(data.get(i).getApidata().getProduct_id()), i);


                    } else {
                        // Toasty.warning(context, "Already rated").show();
                    }
                }
            });

            Log.e("eeeeeeee", data.get(i).getApidata().getUser_rate());
            if (data.get(i).getApidata().getUser_rate().equals("true")) {

                ((VideoPlayerViewHolder) viewHolder).Rate.setBackgroundResource(R.drawable.ratesharystar);
            } else {
                ((VideoPlayerViewHolder) viewHolder).Rate.setBackgroundResource(R.drawable.rateshary);

            }
        } else if (data.get(i).getTypeee().equals("0")) {

            ((VideoPlayerViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            ((VideoPlayerViewHolder) viewHolder).contact_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }


    }


    @Override
    public int getItemCount() {

        return data.size();

    }

    /**
     * @param user_id
     * @param product_id
     */
    public void dialog(String user_id, String product_id, int i) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // ...Irrelevant code for customizing the buttons and title

        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.rate_dialog, null);
        dialogBuilder.setView(dialogView);
        RatingBar mRatingBar = dialogView.findViewById(R.id.ratingBar);

        ImageView close = dialogView.findViewById(R.id.imageView3);
        Button _submit = dialogView.findViewById(R.id._submit);

        AlertDialog show = dialogBuilder.show();

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("rate", "rate");
                data.get(i).getApidata().setUser_rate("true");
                notifyDataSetChanged();
                SignallingClient.getInstance(context).Rate(user_id, product_id, String.valueOf(mRatingBar.getRating()));

                show.dismiss();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show.dismiss();

            }
        });


    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}

