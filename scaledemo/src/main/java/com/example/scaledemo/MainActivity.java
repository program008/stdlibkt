package com.example.scaledemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.scaledemo.scaleview.*;
public class MainActivity extends Activity implements View.OnClickListener {

    private VerticalScaleView verticalScaleView;
    private HorizontalScaleView horizontalScaleView;
    private TextView heightTv;
    private TextView weightTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verticalScaleView = (VerticalScaleView) findViewById(R.id.vertical_scale);
        horizontalScaleView = (HorizontalScaleView) findViewById(R.id
                .horizontal_scale);
        heightTv = (TextView) findViewById(R.id.height);
        weightTv = (TextView) findViewById(R.id.weight);
        heightTv.setOnClickListener(this);
        weightTv.setOnClickListener(this);

        verticalScaleView.setRange(100, 200);
        horizontalScaleView.setRange(0, 48);
        horizontalScaleView.setDuration(98);
        verticalScaleView.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChanged(float value) {
                heightTv.setText(String.valueOf(value));
                heightTv.setTextColor(getResources().getColor(R.color.main_color));
            }
        });
        horizontalScaleView.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChanged(float value) {
                weightTv.setText(String.valueOf(value));
                weightTv.setTextColor(getResources().getColor(R.color.main_color));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.height:
                verticalScaleView.setVisibility(View.VISIBLE);
                horizontalScaleView.setVisibility(View.GONE);
                break;
            case R.id.weight:
                verticalScaleView.setVisibility(View.GONE);
                horizontalScaleView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}