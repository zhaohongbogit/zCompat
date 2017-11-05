package com.hongbo.led;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.contrib.driver.pwmservo.Servo;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String PWM_BUS = "PWM0";
    private Servo mServo;

    Handler handler = new Handler();
    private double angle = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupServo();
        try {
            mServo.setAngle(30);
        } catch (IOException e) {
            Log.e(TAG, "Error setting the angle", e);
        }
//        handler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            angle += 20d;
//            if (angle > 180d) {
//                angle = 0d;
//            }
            try {
                mServo.setAngle(0);
                Thread.sleep(1000);
                mServo.setAngle(90);
                Thread.sleep(500);
                mServo.setAngle(180);
                Thread.sleep(500);
                mServo.setAngle(0);
                Thread.sleep(500);
                mServo.setAngle(180);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyServo();
    }

    private void setupServo() {
        try {
            mServo = new Servo(PWM_BUS);
            mServo.setAngleRange(0f, 180f);
            mServo.setEnabled(true);
        } catch (IOException e) {
            Log.e(TAG, "Error creating Servo", e);
        }
    }

    private void destroyServo() {
        if (mServo != null) {
            try {
                mServo.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Servo");
            } finally {
                mServo = null;
            }
        }
    }
}
