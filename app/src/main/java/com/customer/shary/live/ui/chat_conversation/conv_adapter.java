package com.customer.shary.live.ui.chat_conversation;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;
import com.customer.shary.live.ui.chat_conversation.datamodel.Msg_Object;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

public class conv_adapter extends RecyclerView.Adapter<viewholder> {


    private ArrayList<Msg_Object> msg_objects ;
    private Context mContext;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer  = new MediaPlayer();
    ;



    public conv_adapter(ArrayList<Msg_Object> msg_objects, Context mContext) {
        this.msg_objects = msg_objects;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {

        //TODO : TEXT
        if(msg_objects.get(position).getMessage_type().equals("0"))
        {
            if(msg_objects.get(position).getReceiver().equals("0")) {
                return 1;
            }
            else
            {
                return 2;
            }
        }
        //TODO : IMAGE
        else if(msg_objects.get(position).getMessage_type().equals("1"))
        {
            if(msg_objects.get(position).getReceiver().equals("0")) {
                return 3;
            }
            else
            {
                return 4;
            }
        }


        //TODO : Video
        else if(msg_objects.get(position).getMessage_type().equals("2"))
        {
            if(msg_objects.get(position).getReceiver().equals("0")) {
                return 5;
            }
            else
            {
                return 6;
            }

        }

        //TODO : audio
        else if(msg_objects.get(position).getMessage_type().equals("3"))
        {
            if(msg_objects.get(position).getReceiver().equals("0")) {
                return 7;
            }
            else
            {
                return 8;
            }

        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_sent_message_text,parent,false));
        }
        else  if (viewType==3)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_sent_message_img,parent,false));

        }

        else  if (viewType==5)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_sent_message_video,parent,false));

        }
        else  if (viewType==7)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_sent_voice_message,parent,false));

        }

        if(viewType==2)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_received_message_text,parent,false));
        }
        else  if (viewType==4)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_received_message_img,parent,false));

        }

        else  if (viewType==6)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_received_message_video,parent,false));

        }
        else  if (viewType==8)
        {
            return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.row_received_message_voice,parent,false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        if(msg_objects.get(position).getMessage_type()=="0") {
            holder.tv_message_content.setText(msg_objects.get(position).getMessage());

        }
        else  if(msg_objects.get(position).getMessage_type()=="1") {
            Glide.with(mContext).load(msg_objects.get(position).getImage()).into(holder.img_msg);
        }
        else if(msg_objects.get(position).getMessage_type()=="2")
        {
            Glide.with(mContext).load(msg_objects.get(position).getFile()).into(holder.thumb_img);

        }

        else if(msg_objects.get(position).getMessage_type()=="3")
        {
            holder.voice_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if(mediaPlayer!=null&&countDownTimer!=null)
                        {
                            countDownTimer.cancel();
                            countDownTimer=null;
                            mediaPlayer.release();
                            mediaPlayer=null;
                            holder.voice_play_btn.setImageResource(R.drawable.exo_controls_play);
                        }
                        else
                        {
                            mediaPlayer=new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            try {
                                mediaPlayer.setDataSource(mContext, Uri.parse(msg_objects.get(position).getFile()));
                                mediaPlayer.prepare();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            countDownTimer = new CountDownTimer(mediaPlayer.getDuration(),1000)
                            {

                                @Override
                                public void onTick(long l) {
                                    holder.voice_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                                    Log.e("ad","da");
                                }
                                @Override
                                public void onFinish() {
                                    holder.voice_seekbar.setMax(0);

                                }
                            };
                            holder.voice_play_btn.setImageResource(R.drawable.exo_icon_pause);
                            countDownTimer.start();
                            mediaPlayer.start();
                            holder.voice_seekbar.setMax(mediaPlayer.getDuration());
                            holder.voice_play_btn.setImageResource(R.drawable.exo_icon_pause);
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    holder.voice_play_btn.setImageResource(R.drawable.exo_controls_play);
                                }
                            });
                        }
                }
            });

        }
        int hours = new Time(System.currentTimeMillis()).getHours();

        holder.tv_time.setText(String.valueOf(hours)+":"+ new Time(System.currentTimeMillis()).getMinutes());


    }

    @Override
    public int getItemCount() {
        return msg_objects.size();
    }
}


class  viewholder extends RecyclerView.ViewHolder
{

    public TextView tv_message_content;
    public ImageView img_msg,thumb_img;
    public SeekBar voice_seekbar;
    public ImageView voice_play_btn;
    public TextView tv_time ;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        tv_message_content=itemView.findViewById(R.id.tv_message_content);
        img_msg=itemView.findViewById(R.id.img_msg);
        thumb_img=itemView.findViewById(R.id.thumb_img);
        voice_play_btn=itemView.findViewById(R.id.voice_play_btn);
        voice_seekbar=itemView.findViewById(R.id.voice_seekbar);
        tv_time=itemView.findViewById(R.id.tv_time);
    }
}
