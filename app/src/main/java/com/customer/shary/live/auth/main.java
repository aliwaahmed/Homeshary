package com.customer.shary.live.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.customer.shary.live.MainActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.auth.apimodel.apilogin;
import com.customer.shary.live.mNetwork.Network;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class main extends AppCompatActivity {


    EditText email, password;
    TextView forget_pass, new_account;
    Button login_button, skipBtn;
    apilogin apilogin;
    public ArrayList<com.customer.shary.live.auth.apimodel.apilogin> loginObjects;
    Network network;
    RequestQueue queue;
    int status;
    private static final String TAG = "AndroidClarified";
    private SignInButton googleSignInButton;
    CallbackManager callbackManager;

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("key", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("err", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("err", "printHashKey()", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String status = prefs.getString("status", "No name defined");

        if (status.equals("200")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forget_pass = findViewById(R.id.forgetPass);
        new_account = findViewById(R.id.newAccount);
        login_button = findViewById(R.id.login_btn);
        skipBtn = findViewById(R.id._skip_btn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        network = new Network(this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        newAccout();
        forgetPassword();


        callbackManager = CallbackManager.Factory.create();


        printHashKey(this);


        String EMAIL = "email";

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                Log.e("bool", String.valueOf(isLoggedIn));
                // Log.e("bool", String.valueOf(loginResult.getAccessToken()));

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.e("s", object.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                SharedPreferences.Editor editor =
                                        getSharedPreferences("login", MODE_PRIVATE).edit();
                                editor.putString("status", "200");
                                editor.apply();
                                finish();

                            }
                        });
                    }
                });
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

                Log.e("err", error.getMessage().toString());
            }
        });
        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });


        loginProccess();
    }

    private void loginProccess() {

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                login();
                if (email.length() == 0) {
                    Toasty.error(main.this, R.string.missing_email, Toast.LENGTH_SHORT).show();
                } else if (password.length() == 0) {
                    Toasty.error(main.this, R.string.missing_pass, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(main.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);


                }

            }
        });

    }

    //login
    public int login() {


        loginObjects = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://shary.live/api/v1/user/login";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(String response) {

                        Log.e("login", response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONArray cart = jsonObject.getJSONArray("cart");
                            Log.e("login", String.valueOf(cart.length()));
                            status = jsonObject.getInt("status");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                apilogin = new apilogin();
                                apilogin.setName(jsonArray.getJSONObject(i).getString("name"));
                                apilogin.setEmail(jsonArray.getJSONObject(i).getString("email"));

                                loginObjects.add(apilogin);


                            }


                            if (status == 200) {
                                //save data into Shared Preferences
                                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                                editor.putString("status", String.valueOf(status));
                                editor.putString("email", jsonArray.getJSONObject(0).getString("email"));
                                editor.putString("name", jsonArray.getJSONObject(0).getString("name"));
                                editor.putString("image", jsonArray.getJSONObject(0).getString("image"));
                                editor.putString("login_id", jsonArray.getJSONObject(0).getString("id"));
                                editor.putString("room", jsonArray.getJSONObject(0).getString("room_name"));
                                editor.putString("cart_count", String.valueOf(cart.length()));
                                editor.apply();
                                //intent to login
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }


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
                params.put("user", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;

            }

        };
        queue.add(request);
        return status;
    }

    // forget password
    private void forgetPassword() {
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main.this, SendCodeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    //new account
    private void newAccout() {

        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main.this, Signup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            Log.e("request", String.valueOf(requestCode));
        switch (requestCode) {
            case 101:


                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);


                Log.e(TAG, "alisami" + "ali");


                break;
        }

    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else {
            Log.d(TAG, "Not logged in");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
            editor.putString("status", "200");
            editor.putString("email", account.getEmail());
            editor.putString("name", account.getDisplayName());
            editor.putString("image", account.getPhotoUrl().toString());
            editor.putString("room", "logapps");
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();

            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


}
