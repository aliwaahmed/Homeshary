package com.customer.shary.live.ui.chat_history.chat.bottomsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.customer.shary.live.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static android.app.Activity.RESULT_OK;


public class bottomSheet extends BottomSheetDialogFragment {

    Context context;
    ImageView img, video, _send_file;
    private getfile getfile ;
    private static final  int Gallary =0;
    private static final  int Video =1001;

    public bottomSheet newInstance(Context context,getfile getfile) {
        bottomSheet fragment = new bottomSheet();
        this.context = context;
        this.getfile =getfile;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View v = View.inflate(getContext(), R.layout.fragment_bottomsheet, null);
        dialog.setContentView(v);
        ((View) v.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));



        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(), R.anim.slide);
        v.setAnimation(animation1);
        animation1.start();




        img = v.findViewById(R.id._send_media);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallary);
            }
        });

        video = v.findViewById(R.id._send_voice);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent,Video);
            }
        });
        _send_file = v.findViewById(R.id._send_file);
        _send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent,1111);
            }
        });
    }


    private void browseDocuments() {

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 1111);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == Gallary && resultCode == RESULT_OK  && null != data) {

            getfile.file( getPath(data.getData()),
                        "1");

            Log.e("d","alialiali");

        }
        else if (requestCode == Video && resultCode == RESULT_OK  && null != data)
        {
            getfile.file( getPath(data.getData()),
                    "2");
            Log.e("d","alialiali22222");


        }

        else if (requestCode == 1111 && resultCode == RESULT_OK  && null != data)
        {
            getfile.file(getPath(data.getData()),
                    "3");
            Log.e("d","alialiali22222");


        }



    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    public interface getfile {

        void file(String path ,String type);


    }



}





