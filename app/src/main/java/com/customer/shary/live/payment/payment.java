package com.customer.shary.live.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.ads.loadads;
import com.customer.shary.live.payment.order.myorder;
import com.customer.shary.live.payment.orderSteper.OrderSteper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class payment extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    private EditText _delivery_name, _delivery_address, _delivery_phone;
    private Button _confirm;
    private ProgressDialog progressDialog;
    private ImageView _product_img;
    private TextView _title, _description, _new_price, _old_price;
    private ProgressDialog dialog;
    private String deliever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dilvary_data);

        loadads.getInstance().mInterstitialAd(this);
        _delivery_name = findViewById(R.id._delivery_name);
        _delivery_address = findViewById(R.id._delivery_address);
        _delivery_phone = findViewById(R.id._delivery_phone);
        _confirm = findViewById(R.id._confirm);
        _new_price = findViewById(R.id._new_price);
        _old_price = findViewById(R.id._old_price);
        _product_img = findViewById(R.id._product_img);
        _title = findViewById(R.id._title);
        _description = findViewById(R.id._description);
        dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage(getResources().getString(R.string.load));
        dialog.setCancelable(false);
        dialog.show();
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Confirm Your Data");
        progressDialog.show();


        Log.e("product_id", String.valueOf(getIntent().getExtras().getString("products")));

        Log.e("product", String.valueOf(getIntent().getExtras().getString("products")));
        getProduct(String.valueOf(getIntent().getExtras().getString("products")));


        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String value = ((RadioButton) findViewById(rg.getCheckedRadioButtonId()))
                                .getText().toString();
                deliever = value;
                Log.e("delieverali",value);


            }
        });


        _confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_delivery_address.getText().toString().isEmpty()) {
                    _delivery_address.setError("ENTER DATA");
                } else if (_delivery_name.getText().toString().isEmpty()) {
                    _delivery_name.setError("ENTER DATA");
                } else if (_delivery_phone.getText().toString().isEmpty()) {
                    _delivery_phone.setError("ENTER DATA");
                } else if (_confirm.getText().toString().isEmpty()) {
                    _confirm.setError("ENTER DATA");
                } else if (deliever != null) {
                    Log.e("PRODUCT_ID", getIntent().getExtras().getString("products"));
                    post_request(sharedPreferences.getString("login_id", "-1"), _delivery_address.getText().toString()
                            , _delivery_phone.getText().toString(),
                            getIntent().getExtras().getString("products"), deliever
                    );
                } else {

                }
            }
        });


        _delivery_name.setText(sharedPreferences.getString("name", "-2"));


    }

    /**
     * @param Product_id
     */
    public void getProduct(String Product_id) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://shary.live/api/v1/product/view/" + Product_id).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {


            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                String res = response.body().string();

                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject jsonArray = jsonObject.getJSONObject("data");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                _old_price.setText(jsonArray.getString("price").toString());
                                _new_price.setText(jsonArray.getString("new_price").toString());
                                _description.setText(jsonArray.getString("details").toString());
                                _title.setText(jsonArray.getString("name").toString());
                                Glide.with(getApplicationContext()).load(jsonArray.getString("video_thumb"))
                                        .into(_product_img);
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.e("res", res);

            }
        });


    }


    /**
     * @param user_id
     * @param addr
     * @param phone
     * @param Product_id
     */
    public void post_request(String user_id, String addr, String phone, String Product_id, String s) {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("user_id", user_id)
                .add("addr", addr)
                .add("phone", phone)
                .add("product_id", Product_id)
                .add("name",_delivery_name.getText().toString())
                .add("deliever_method", s)
                .build();

        Request request = new Request.Builder()
                .url("https://shary.live/api/v1/product_order")
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                String res = response.body().string();

                Log.e("responsealihere",res.toString());

                try {

                    JSONObject jsonObject = new JSONObject(res);
                    if(jsonObject.getInt("status")==200) {


                    JSONObject jsonArray = jsonObject.getJSONObject("data");
                    String s = jsonArray.getString("store_id");
                    SignallingClient.getInstance(getApplicationContext()).buy_now_order(s,
                            Product_id, user_id, addr, phone);
                    Log.e("saller_id", s);
                        Log.e("order", res);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //dialog();

                                Intent intent =new Intent(getApplicationContext(), myorder.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();


                            }
                        });

                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


//    public void dialog() {
//
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//
//        // ...Irrelevant code for customizing the buttons and title
//
//        LayoutInflater inflater = this.getLayoutInflater();
//
//        View dialogView = inflater.inflate(R.layout.your_order_load, null);
//        dialogBuilder.setView(dialogView);
//
//
//        Button _submit = dialogView.findViewById(R.id._dismiss);
//
//        AlertDialog show = dialogBuilder.show();
//
//        _submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("rate", "rate");
//
//                onBackPressed();
//
//                show.dismiss();
//
//            }
//        });
//
//
//    }


}
