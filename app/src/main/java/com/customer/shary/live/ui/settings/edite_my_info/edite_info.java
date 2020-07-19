package com.customer.shary.live.ui.settings.edite_my_info;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.customer.shary.live.R;
import com.customer.shary.live.auth.SendCodeActivity;
import com.customer.shary.live.auth.Signup;
import com.customer.shary.live.ui.home.HomeViewModel;
import com.customer.shary.live.ui.settings.edite_my_info.model.userdatamodel;
import com.customer.shary.live.ui.settings.edite_my_info.viewmodel.viewmodel;
import com.customer.shary.live.ui.settings.settingViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class edite_info extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 0;
    private com.customer.shary.live.ui.settings.edite_my_info.viewmodel.viewmodel viewmodel;
    private EditText _edit_name, _edit_email, _edit_address, _edit_phone, _edit_password;
    private ImageView _customer_image, upload_image;
    private Button _confirm;
    private String imgDecodableString;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        _edit_name = findViewById(R.id._edit_name);
        _edit_email = findViewById(R.id._edit_email);
        _edit_address = findViewById(R.id._edit_address);
        _edit_phone = findViewById(R.id._edit_phone);
        _customer_image = findViewById(R.id._customer_image);
        _confirm = findViewById(R.id._confirm);
        _edit_password = findViewById(R.id._edit_password);
        upload_image = findViewById(R.id.upload_image);
        prefs = getSharedPreferences("login", MODE_PRIVATE);

        viewmodel = ViewModelProviders.of(this).get(viewmodel.class);

        viewmodel.load(getApplicationContext()).observe(this, new Observer<userdatamodel>() {
            @Override
            public void onChanged(userdatamodel userdatamodel) {

                _edit_name.setText(userdatamodel.getName());
                _edit_email.setText(userdatamodel.getEmail());
                _edit_address.setText(userdatamodel.getAddr());
                _edit_phone.setText(userdatamodel.getPhone());

                Glide.with(getApplicationContext()).load(userdatamodel.getImg()).into(_customer_image);
            }
        });


        _confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imgDecodableString!=null) {
                    post_new_data(_edit_name.getText().toString(),
                            _edit_email.getText().toString(),
                            _edit_address.getText().toString(),
                            _edit_phone.getText().toString(),
                            imgDecodableString,
                            _edit_password.getText().toString());
                }
                else
                {
                    post_new_data(_edit_name.getText().toString(),
                            _edit_email.getText().toString(),
                            _edit_address.getText().toString(),
                            _edit_phone.getText().toString(),
                            "",
                            _edit_password.getText().toString());
                }

            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(edite_info.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


    }

    /**
     * @param name
     * @param email
     * @param addr
     * @param phone
     * @param image
     */
    public void post_new_data(String name, String email, String addr, String phone, String image, String password) {

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder mMultiplebody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mMultiplebody.addFormDataPart("user_id", prefs.getString("login_id", "-1"));
        mMultiplebody.addFormDataPart("name", name);
        mMultiplebody.addFormDataPart("email", email);
        mMultiplebody.addFormDataPart("address", addr);
        mMultiplebody.addFormDataPart("phone", phone);
        mMultiplebody.addFormDataPart("password", password);
        if(!image.equals("")) {
            mMultiplebody.addFormDataPart("image", "user_image",
                    RequestBody.create(MediaType.parse("image/*"), new File(image)));
        }
        mMultiplebody.build();


        RequestBody requestBody1 = mMultiplebody.build();

        Request request = new Request.Builder().url("https://shary.live/api/v1/user/edituser").post(requestBody1).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                String res = response.body().string();
                Log.e("res",res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject jsonArray = jsonObject.getJSONObject("data");
                    if (jsonObject.getInt("status") == 200) {


                        SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                        editor.putString("email", jsonArray.getString("email"));
                        editor.putString("name", jsonArray.getString("name"));
                        editor.putString("image", jsonArray.getString("store_image"));
                        editor.putString("login_id", String.valueOf(jsonArray.getInt("id")));
                        editor.commit();

                        Log.e("email",jsonArray.getString("email"));
                        Log.e("name",jsonArray.getString("name"));
                        Log.e("image",jsonArray.getString("store_image"));
                        Log.e("login_id",String.valueOf(jsonArray.getInt("id")));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent("filter_string");
                                intent.putExtra("key", "My Data");
                                // put your all data using put extra

                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                            }
                        });


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            _customer_image.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);

            imgDecodableString=filePath;
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


    }



}
