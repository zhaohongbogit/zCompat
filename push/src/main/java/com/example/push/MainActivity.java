package com.example.push;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.things.contrib.driver.pwmservo.Servo;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String SMS_ACTION = "com.hongbo.sms";
    public static final String KEY_ORDER = "key_order";

    private static final String PWM_BUS = "PWM0";
    private Servo mServo;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupServo();
        //是否收到push
        IntentFilter filter = new IntentFilter(SMS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        closeLed(); //启动后默认关闭LED
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到新订单
            ci = 10;
            handler.post(runnable); //启动闪灯提醒
        }
    };

    private int ci = 10;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ci--;
            if (ci > 0) {
                showLed();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeLed();
                handler.postDelayed(runnable, 100);
            }
        }
    };

    /**
     * LED亮
     */
    private void showLed() {
        try {
            mServo.setEnabled(true);
            mServo.setAngle(180);
        } catch (IOException e) {
            Log.e(TAG, "Error setting the angle", e);
        }
    }

    private void closeLed() {
        try {
            mServo.setEnabled(false);
        } catch (IOException e) {
            Log.e(TAG, "Error setting the enable", e);
        }
    }

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
