package com.cool.baigu.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by baigu on 2017/8/16.
 */

public class CustomWidget extends View {
    private Paint paint = new Paint();;
    private Bitmap slide_button = BitmapFactory.decodeResource(getResources(), R.mipmap.slide_button);
    private Bitmap background = BitmapFactory.decodeResource(getResources(), R.mipmap.background);
    private float touchX;
    private float aFloat;

    public CustomWidget(Context context) {
        super(context);
    }

    public CustomWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                widthSize = background.getWidth();
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                heightSize = slide_button.getHeight();
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, paint);

        if(aFloat < 0) {
            aFloat = 0;
        }
        if(aFloat > background.getWidth() - slide_button.getWidth()) {
            aFloat = background.getWidth() - slide_button.getWidth();
        }
        canvas.drawBitmap(slide_button, 0 + aFloat, 0, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                aFloat += event.getX() - touchX;
                touchX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(aFloat <= slide_button.getWidth() / 4) {
                    aFloat = 0;
                } else {
                    aFloat = background.getWidth() - slide_button.getWidth();
                }
                break;
        }

        invalidate();

        return true;
    }
}
