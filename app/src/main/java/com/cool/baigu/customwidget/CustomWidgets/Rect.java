package com.cool.baigu.customwidget.CustomWidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by baigu on 2017/8/14.
 */

public class Rect extends View {
    public Rect(Context context) {
        super(context);
    }

    public Rect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        RectF rect = new RectF(0, 0, 500, 500);
        Paint paint = new Paint();
        paint.setARGB(255,120,120,255);
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(50, 100);


    }



}
