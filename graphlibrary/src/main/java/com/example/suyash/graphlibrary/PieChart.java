package com.example.suyash.graphlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by suyash on 6/12/18.
 */

public class PieChart extends View {
    Paint mPaint, mBitmapPaint;

    ArrayList<DataPoint> dataPoints;
    float width=0, height=0;
    private int LABEL_SIZE = 20;
    float diameter;
    boolean init = true;

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0);
        LABEL_SIZE = typedArray.getInteger(R.styleable.PieChart_label_text_size, 20);


        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(60);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        dataPoints = new ArrayList<>();

    }

    public PieChart(Context context, float width, float height) {
        super(context);

        this.width = width;
        this.height = height;

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(60);


        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        dataPoints = new ArrayList<>();
    }

    public void setLabelTextSize(int LABEL_SIZE) {
        this.LABEL_SIZE = LABEL_SIZE;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged((int) width, (int) height, oldw, oldh);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (width == 0 && height == 0 && init){
            width = this.getMeasuredWidth();
            height = this.getMeasuredHeight();
            Log.d("TAG: width = ",""+width);
            Log.d("TAG: height = ",""+height);

            boolean widthMatchParent = (ViewGroup.LayoutParams.MATCH_PARENT==getLayoutParams().width || ViewGroup.LayoutParams.WRAP_CONTENT==getLayoutParams().width);
            if(!widthMatchParent){width = width/2;}
            boolean heightMatchParent = (ViewGroup.LayoutParams.MATCH_PARENT==getLayoutParams().height || ViewGroup.LayoutParams.WRAP_CONTENT==getLayoutParams().height);
            if(!heightMatchParent){height = height/2;}

            Bitmap bitmap1 = drawPieChart();
            Bitmap bitmap2 = drawIndex();
            canvas.drawBitmap(bitmap1, 0, 0, mBitmapPaint);
            canvas.drawBitmap(bitmap2, 0, height / 2, mBitmapPaint);
        }
    }


    public void setPoints(ArrayList<DataPoint> pointList) {
        this.dataPoints = pointList;
    }

    private Bitmap drawPieChart() {
        if(height >= (2*width)) diameter = width;
        else diameter = height/2;
        Bitmap bitmap = Bitmap.createBitmap((int) diameter, (int) diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        RectF oval = new RectF();
        oval.set(-canvas.getWidth() / 3, -canvas.getHeight() / 3, canvas.getWidth() / 3, canvas.getHeight() / 3);
        float startAngle = 0;
        for (int i = 0; i < dataPoints.size(); i++) {
            mPaint.setColor(dataPoints.get(i).color);
            float sweepAngle = (dataPoints.get(i).percentage * 360) / 100;
            canvas.drawArc(oval, startAngle, sweepAngle, true, mPaint);
            startAngle += sweepAngle;
        }

        if (startAngle < 360) {
            dataPoints.add(new DataPoint("Other", ((360 - startAngle) * 100) / 360, Color.parseColor("#99A3A4")));
            mPaint.setColor(Color.parseColor("#99A3A4"));
            canvas.drawArc(oval, startAngle, 360 - startAngle, true, mPaint);
        }
        return bitmap;
    }

    private Bitmap drawIndex() {
        if(height >= (2*width)) diameter = width;
        else diameter = height/2;
        Bitmap bitmap = Bitmap.createBitmap((int) diameter, (int) diameter, Bitmap.Config.ARGB_8888);
        Canvas index = new Canvas(bitmap);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(width / 100);
        mPaint.setTextSize(LABEL_SIZE);

        for (int i = 0; i < dataPoints.size(); i++) {
            mPaint.setColor(dataPoints.get(i).color);
            index.drawCircle(width / 25, (width / 10) + (i * (width / 10)), width / 25, mPaint);
            mPaint.setColor(Color.BLACK);
            index.drawText(dataPoints.get(i).category + " : " + dataPoints.get(i).percentage + "% ", width / 10, (width / 8) + (i * (width / 10)), mPaint);
        }
        return bitmap;

    }

}
