package com.example.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

import static com.example.push.MainActivity.KEY_ORDER;
import static com.example.push.MainActivity.SMS_ACTION;

/**
 * Push消息通知
 * Created by HONGBO on 2017/11/3.
 */

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.d("ThingPush", "接收到title为:" + title + ",消息内容为:" + message);
            Intent newOrderIntent = new Intent(SMS_ACTION);
            newOrderIntent.putExtra(KEY_ORDER, message);
            LocalBroadcastManager.getInstance(context).sendBroadcast(newOrderIntent);
        }
    }
}
