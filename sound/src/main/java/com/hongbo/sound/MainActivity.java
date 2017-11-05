package com.hongbo.sound;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String GPIO_SOUND = "PWM0";
    private static final String GPIO_BUTTON = "BCM5";

    Gpio btn; //按钮
    Pwm sound; //蜂鸣器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();

        try {
            sound = service.openPwm(GPIO_SOUND);
            sound.setPwmFrequencyHz(10);
            sound.setPwmDutyCycle(50);

            btn = service.openGpio(GPIO_BUTTON);
            btn.setDirection(Gpio.DIRECTION_IN);
            btn.setActiveType(Gpio.ACTIVE_LOW);
            btn.setEdgeTriggerType(Gpio.EDGE_BOTH);
            btn.registerGpioCallback(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GpioCallback callback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                Log.d(TAG, "Btn is gpio " + gpio.getValue());
                setSound(gpio.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    };

    private void setSound(boolean isPressed) throws IOException {
        if (isPressed) {
            sound.setEnabled(true);
            sound.setPwmDutyCycle(5);
        } else {
            sound.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btn != null) {
            try {
                btn.unregisterGpioCallback(callback);
                btn.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                btn = null;
            }
        }
        if (sound != null) {
            try {
                sound.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                sound = null;
            }
        }
    }
}
