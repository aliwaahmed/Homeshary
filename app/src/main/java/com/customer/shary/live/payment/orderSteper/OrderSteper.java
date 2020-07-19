package com.customer.shary.live.payment.orderSteper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.badoualy.stepperindicator.StepperIndicator;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.customer.shary.live.R;

import java.util.ArrayList;
import java.util.List;

public class OrderSteper extends AppCompatActivity {

    StepperIndicator stepperIndicator ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_steper);

        VerticalStepView mSetpview0 = (VerticalStepView) findViewById(R.id.step_view);

        List<String> list0 = new ArrayList<>();
        list0.add("one");
        list0.add("two");
        list0.add("three");
        list0.add("four");
        list0.add("four");
        list0.add("five");


        mSetpview0.setStepsViewIndicatorComplectingPosition(list0.size() - 1)
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor
                        (ContextCompat.getColor(getApplicationContext(),
                                android.R.color.holo_green_light))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor
                        (ContextCompat.getColor(getApplicationContext(),
                                R.color.green))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor
                        (ContextCompat.getColor(getApplicationContext(),
                                android.R.color.holo_green_light))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor
                        (ContextCompat.getColor(getApplicationContext(),
                                R.color.green))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon
                        (ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon
                        (ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon
                        (ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.attention));//设置StepsViewIndicator AttentionIcon
    }
}
