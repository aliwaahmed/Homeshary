package com.customer.shary.live.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.customer.shary.live.MainActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.auth.main;


public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);


        Thread thread = new Thread(){
            @Override
            public void run() {

                try{

                    sleep(2000);
                    Intent i = new Intent(getApplicationContext() , MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }

                catch (InterruptedException e){
                    e.printStackTrace();

                }
            }
        };
        thread.start();
    }
}
