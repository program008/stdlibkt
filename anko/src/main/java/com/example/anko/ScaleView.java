package com.example.anko;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigDecimal;

/**
 * 刻度尺
 * Created by Administrator on 2016/9/14.
 */
public class ScaleView extends View {

        private Paint mLinePaint;

        private Paint mTextPaint;

        private Paint mRulerPaint;

        private float progrees = 5;

        private int max = 180;

        private int min = 0;

        private boolean isCanMove;

        private OnScrollListener mScrollListener;

        public ScaleView(Context context) {
                super(context);
                init();
        }

        public ScaleView(Context context, AttributeSet attrs) {
                super(context, attrs);
                init();
        }

        public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        private void init() {
                mLinePaint = new Paint();
                mLinePaint.setColor(getResources().getColor(R.color.scale_line1, null));
                mLinePaint.setAntiAlias(true);//抗锯齿
                mLinePaint.setStyle(Paint.Style.STROKE);
                mLinePaint.setStrokeWidth(2);
                mTextPaint = new Paint();
                mTextPaint.setColor(getResources().getColor(R.color.scale_text, null));
                mTextPaint.setAntiAlias(true);
                mTextPaint.setStyle(Paint.Style.FILL);
                mTextPaint.setStrokeWidth(2);
                mTextPaint.setTextSize(18);
                //
                mRulerPaint = new Paint();
                mRulerPaint.setAntiAlias(true);
                mRulerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mRulerPaint.setColor(Color.RED);
                mRulerPaint.setStrokeWidth(2);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(setMeasureWidth(widthMeasureSpec), setMeasureHeight(heightMeasureSpec));
        }

        private int setMeasureHeight(int spec) {
                int mode = MeasureSpec.getMode(spec);
                int size = MeasureSpec.getSize(spec);
                int result = Integer.MAX_VALUE;
                switch (mode) {
                        case MeasureSpec.AT_MOST:
                                size = Math.min(result, size);
                                break;
                        case MeasureSpec.EXACTLY:
                                break;
                        default:
                                size = result;
                                break;
                }
                return size;
        }

        private int setMeasureWidth(int spec) {
                int mode = MeasureSpec.getMode(spec);
                int size = MeasureSpec.getSize(spec);
                int result = Integer.MAX_VALUE;
                switch (mode) {
                        case MeasureSpec.AT_MOST:
                                size = Math.min(result, size);
                                break;
                        case MeasureSpec.EXACTLY:
                                break;
                        default:
                                size = result;
                                break;
                }
                return size;
        }

        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                canvas.save();
                for (int i = min; i < max; i++) {
                        if (i % 10 == 0) {
                                canvas.drawLine(10, 0, 10, 32, mLinePaint);
                                String text = i / 10 * 10 + "";
                                Rect rect = new Rect();
                                float txtWidth = mTextPaint.measureText(text);
                                mTextPaint.getTextBounds(text, 0, text.length(), rect);
                                mTextPaint.setTextSize(18);
                                canvas.drawText(text, 10 - txtWidth / 2, 32 + rect.height() + 10, mTextPaint);
                        }
                        else {
                                canvas.drawLine(10, 10, 10, 20, mLinePaint);
                        }
                        canvas.translate(6, 0);
                }
                canvas.restore();
                int scale = (int) ((progrees - 5) / 6);
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer), progrees - 2, 0, mRulerPaint);
                Log.d("ScaleView", "progrees:" + progrees + ", " + scale);
                if (mScrollListener != null) {
                        mScrollListener.onScaleScroll(scale);
                }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                                isCanMove = true;
                                break;
                        case MotionEvent.ACTION_MOVE:
                                if (!isCanMove) {
                                        return false;
                                }
                                progrees = event.getX();
                                int scale = (int)((progrees-5)/6);
                                if (scale<=max){
                                        invalidate();
                                }
                                break;
                }
                return true;
        }

        /**
         * 设置回调监听
         *
         * @param listener
         */
        public void setOnScrollListener(OnScrollListener listener) {
                this.mScrollListener = listener;
        }

        public interface OnScrollListener {

                void onScaleScroll(int scale);
        }
}