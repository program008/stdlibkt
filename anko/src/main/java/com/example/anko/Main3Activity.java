package com.example.anko;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {
        LoopScaleView mLsv4;
        TextView mTvValue4;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main3);
                initView();
                mLsv4.setOnValueChangeListener(new ValueChangeListener(4));
        }

        public void initView() {

                mLsv4 = findViewById(R.id.lsv_4);
                mTvValue4 = findViewById(R.id.tv_value4);
        }

        class ValueChangeListener implements LoopScaleView.OnValueChangeListener {

                private int type;

                public ValueChangeListener(int type) {
                        this.type = type;
                }

                @Override
                public void OnValueChange(int newValue) {
                        switch (type) {
                                case 1:

                                        break;
                                case 2:

                                        break;
                                case 3:

                                        break;
                                case 4:
                                        mTvValue4.setText("长度 " + newValue + " cm");
                                        break;
                        }
                }
        }
}
