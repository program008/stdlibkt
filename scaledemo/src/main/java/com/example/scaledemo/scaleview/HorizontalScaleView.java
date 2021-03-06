package com.example.scaledemo.scaleview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.scaledemo.R;

import java.math.BigDecimal;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: xuyuanyi@bongmi.com
 */

public class HorizontalScaleView extends View {

        private final int SCALE_WIDTH_BIG = 2;//大刻度线宽度

        private final int SCALE_WIDTH_SMALL = 2;//小刻度线宽度

        private final int LINE_WIDTH = 6;//指针线宽度

        private int rectPadding = 40;//圆角矩形间距

        private int rectWidth;//圆角矩形宽

        private int rectHeight;//圆角矩形高

        private int maxScaleLength;//大刻度长度

        private int midScaleLength;//中刻度长度

        private int minScaleLength;//小刻度长度

        private int scaleSpace;//刻度间距

        private int scaleSpaceUnit;//每大格刻度间距

        private int height, width;//view高宽

        private int ruleHeight;//刻度尺高

        private int max;//最大刻度

        private int min;//最小刻度

        private int borderLeft, borderRight;//左右边界值坐标

        private float midX;//当前中心刻度x坐标

        private float originMidX;//初始中心刻度x坐标

        private float minX;//最小刻度x坐标,从最小刻度开始画刻度

        private float lastX;

        private float originValue;//初始刻度对应的值

        private float currentValue;//当前刻度对应的值

        private Paint paint;//画笔

        private Context context;

        private String descri = "体重";//描述

        private String unit = "kg";//刻度单位

        private VelocityTracker velocityTracker;//速度监测

        private float velocity;//当前滑动速度

        private float a = 1000000;//加速度

        private boolean continueScroll;//是否继续滑动

        private boolean isMeasured;

        private int pointerPosition;

        private OnValueChangeListener onValueChangeListener;

        private Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                        if (null != onValueChangeListener) {
                                float v = (float) (Math.round(currentValue * 10)) / 10;//保留一位小数
                                onValueChangeListener.onValueChanged(v);
                        }
                }
        };

        public HorizontalScaleView(Context context) {
                super(context);
                this.context = context;
                init();
        }

        public HorizontalScaleView(Context context, AttributeSet attrs) {
                super(context, attrs);
                this.context = context;
                init();
        }

        public HorizontalScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                this.context = context;
                init();
        }

        private void init() {
                //初始化画笔
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setDither(true);

        }

        //设置刻度范围
        public void setRange(int min, int max) {
                this.min = min;
                this.max = max;
                originValue =  (max + min) / 2;;
                currentValue = 0;
        }

        public void setDuration(int duration) {
                this.duration = duration;
                invalidate();
        }

        private int duration = 0;

        //设置刻度单位
        public void setUnit(String unit) {
                this.unit = unit;
        }

        //设置刻度描述
        public void setDescri(String descri) {
                this.descri = descri;
        }

        //设置value变化监听
        public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
                this.onValueChangeListener = onValueChangeListener;
        }

        private boolean isCanMove;

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (!isMeasured) {
                        width = getMeasuredWidth();
                        height = getMeasuredHeight();
                        ruleHeight = height * 2 / 3;
                        maxScaleLength = height / 10;
                        midScaleLength = height / 12;
                        minScaleLength = maxScaleLength / 2;
                        scaleSpace = height / 60 > 8 ? height / 60 : 8;
                        scaleSpaceUnit = scaleSpace * 10 + SCALE_WIDTH_BIG + SCALE_WIDTH_SMALL * 9;
                        rectWidth = scaleSpaceUnit;
                        rectHeight = scaleSpaceUnit / 2;

                        borderLeft = width / 2 - ((min + max) / 2 - min) * scaleSpaceUnit;
                        borderRight = width / 2 + ((min + max) / 2 - min) * scaleSpaceUnit;
                        midX = 0;
                        originMidX = 0;
                        minX = 0;
                        isMeasured = true;
                }

        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //画刻度线
                for (int i = min; i <= max; i++) {

                        //画刻度数字
                        Rect rect = new Rect();
                        String str = String.valueOf(i * 10);
                        paint.setColor(getResources().getColor(R.color.scale_text));
                        paint.setTextSize(18);
                        paint.setStrokeWidth(2);
                        paint.getTextBounds(str, 0, str.length(), rect);
                        int w = rect.width();
                        int h = rect.height();
                        canvas.drawText(str, minX + (i - min) * scaleSpaceUnit - w / 2 - SCALE_WIDTH_BIG / 2, maxScaleLength * 3 / 2, paint);

                        if (i <= duration / 10.0) {
                                paint.setColor(getResources().getColor(R.color.scale_line2));
                        }
                        else {
                                paint.setColor(getResources().getColor(R.color.scale_line1));
                        }
                        //画大刻度线
                        paint.setStrokeWidth(SCALE_WIDTH_BIG);
                        canvas.drawLine(minX + (i - min) * scaleSpaceUnit, 0, minX + (i - min) * scaleSpaceUnit, maxScaleLength, paint);


                        if (i == max) {
                                continue;//最后一条不画中小刻度线
                        }

//                        //画中刻度线
//                        paint.setStrokeWidth(SCALE_WIDTH_SMALL);
//                        canvas.drawLine(minX + (i - min) * scaleSpaceUnit + scaleSpaceUnit / 2, ruleHeight, minX + (i - min) * scaleSpaceUnit + scaleSpaceUnit / 2, ruleHeight - midScaleLength, paint);


                        //画小刻度线
                        for (int j = 1; j < 10; j++) {
                                paint.setStrokeWidth(SCALE_WIDTH_SMALL);
                                canvas.drawLine(minX + (i - min) * scaleSpaceUnit + (SCALE_WIDTH_SMALL + scaleSpace) * j, minScaleLength / 2, minX + (i - min) * scaleSpaceUnit + (SCALE_WIDTH_SMALL + scaleSpace) * j, minScaleLength * 3 / 2, paint);
                        }

                }

                //画指针线
                //paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                //canvas.drawLine(width / 2, 0, width / 2, ruleHeight, paint);
                float v = (float) (Math.round(currentValue * 10));//保留一位小数
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer), pointerPosition, 0, paint);
                //Log.d("HorizontalScaleView", "v:" + v);
                Log.d("HorizontalScaleView", "pointerPosition:" + pointerPosition);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                float x = event.getX();
                switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                                isCanMove = true;
                                lastX = x;
                                continueScroll = false;
                                //初始化速度追踪
                                if (velocityTracker == null) {
                                        velocityTracker = VelocityTracker.obtain();
                                }
                                else {
                                        velocityTracker.clear();
                                }
                                break;
                        case MotionEvent.ACTION_MOVE:
                                Log.d("HorizontalScaleView", "x:" + x);
                                if (x < pointerPosition + scaleSpaceUnit * 5 && x > pointerPosition - scaleSpaceUnit * 5) {
                                        int offsetX = (int) (lastX - x);
                                        if (!isCanMove) {
                                                return false;
                                        }
                                        pointerPosition += (int) ((offsetX) / (SCALE_WIDTH_SMALL + scaleSpace));
                                        if (pointerPosition <= max) {
                                                invalidate();
                                        }
                                        lastX = x;
                                        return true;
                                }

                                velocityTracker.addMovement(event);
                                int offsetX = (int) (lastX - x);
                                minX -= offsetX;
                                midX -= offsetX;
                                calculateCurrentScale();
                                invalidate();
                                lastX = x;
                                break;
                        case MotionEvent.ACTION_UP:
                                confirmBorder();
                                //当前滑动速度
                                velocityTracker.computeCurrentVelocity(1000);
                                velocity = velocityTracker.getXVelocity();
                                float minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
                                if (Math.abs(velocity) > minVelocity) {
                                        continueScroll = true;
                                        continueScroll();
                                }
                                else {
                                        velocityTracker.recycle();
                                        velocityTracker = null;
                                }
                                break;
                        case MotionEvent.ACTION_CANCEL:
                                velocityTracker.recycle();
                                velocityTracker = null;
                                break;
                }
                return true;
        }

        //计算当前刻度
        private void calculateCurrentScale() {
                float offsetTotal = midX - originMidX;
                int offsetBig = (int) (offsetTotal / scaleSpaceUnit);//移动的大刻度数
                float offsetS = offsetTotal % scaleSpaceUnit;
                int offsetSmall = (new BigDecimal(offsetS / (scaleSpace + SCALE_WIDTH_SMALL)).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();//移动的小刻度数 四舍五入取整
                float offset = offsetBig + offsetSmall * 0.1f;
                if (originValue - offset > max) {
                        currentValue = max;
                } else if (originValue - offset < min) {
                        currentValue = min;
                } else {
                        currentValue = originValue - offset;
                }
                mHandler.sendEmptyMessage(0);
        }

        //指针线超出范围时 重置回边界处
        private void confirmBorder() {
                if (midX < borderLeft) {
                        midX = borderLeft;
                        minX = borderLeft - (borderRight - borderLeft) / 2;
                        //minX = borderLeft;
                        postInvalidate();
                }
                else if (midX > borderRight) {
                        midX = borderRight;
                        //minX = borderLeft + (borderRight - borderLeft) / 2;
                        minX = borderRight-borderLeft;
                        //minX = borderRight;
                        postInvalidate();
                }
        }

        //手指抬起后继续惯性滑动
        private void continueScroll() {
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                float velocityAbs = 0;//速度绝对值
                                if (velocity > 0 && continueScroll) {
                                        velocity -= 50;
                                        minX += velocity * velocity / a;
                                        midX += velocity * velocity / a;
                                        velocityAbs = velocity;
                                }
                                else if (velocity < 0 && continueScroll) {
                                        velocity += 50;
                                        minX -= velocity * velocity / a;
                                        midX -= velocity * velocity / a;
                                        velocityAbs = -velocity;
                                }
                                calculateCurrentScale();
                                confirmBorder();
                                postInvalidate();
                                if (continueScroll && velocityAbs > 0) {
                                        post(this);
                                }
                                else {
                                        continueScroll = false;
                                }
                        }
                }).start();
        }
}
