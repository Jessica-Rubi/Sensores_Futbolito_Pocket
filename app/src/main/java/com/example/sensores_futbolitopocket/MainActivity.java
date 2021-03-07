package com.example.sensores_futbolitopocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    vista animatedView = null;
    private Bitmap ball;

    private float xMax, yMax;
    private float xPos, xSen = 0.0f;
    private float yPos, ySen, porteria, porteria1  = 0.0f;

    private Bitmap campo;
    private int xMaxC, yMaxC;

    private int scoreA = 0;
    private int scoreB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        xMaxC = (int) size.x;
        yMaxC = (int) size.y;

        xMax = (float) size.x - 50;
        yMax = (float) size.y - 50;

        xPos = size.x / 2;
        yPos = size.y / 2;

//        porteria = yMax / 3;
//        porteria1 = porteria * 2;

//        Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.pelota);
//        final int dstWidth = 50;
//        final int dstHeight = 50;
//        ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        animatedView = new vista(this);
        setContentView(animatedView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xSen -= (float) event.values[0];
            ySen += (float) event.values[1];
            updateBall();
        }
    }

    private void updateBall() {
        xPos = xSen + (xMax / 2);
        yPos = ySen + (yMax / 2);
        if (xPos > xMax) {
            xPos = xMax;
        } else if (xPos < 0) {
            xPos = 0;
        }
        if (yPos > yMax) {
            xSen = 0;
            ySen = 0;
            xPos = xMax / 2;
            yPos = yMax / 2;
            scoreA++;
        } else if (yPos < 0) {
            xSen = 0;
            ySen = 0;
            xPos = xMax / 2;
            yPos = yMax / 2;
            scoreB++;
        }
    }


    public class vista extends AppCompatImageView {

        public vista(Context context) {
            super(context);
            Bitmap fieldSrc = BitmapFactory.decodeResource(getResources(), R.drawable.campo);
            campo = Bitmap.createScaledBitmap(fieldSrc, xMaxC, yMaxC, true);

            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.pelota);
            final int dstWidth = 50;
            final int dstHeight = 50;
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(campo, 0, 0, null);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText(scoreA + "", 100, yMax / 2, paint);
            canvas.drawText(scoreB + "", xMax - 100, yMax / 2, paint);
            canvas.drawBitmap(ball, xPos, yPos, null);
            invalidate();
        }
    }
}