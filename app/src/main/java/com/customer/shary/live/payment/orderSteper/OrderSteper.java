package com.customer.shary.live.payment.orderSteper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.customer.shary.live.R;

import java.util.ArrayList;
import java.util.List;

public class OrderSteper extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_steper);

        TextView textView  =findViewById(R.id.textView8);
        textView.setText(getString(R.string.order_status1) +
                getString(R.string.order_status));


        ImageView back =findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
