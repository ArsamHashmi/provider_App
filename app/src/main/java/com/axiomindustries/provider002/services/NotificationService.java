package com.axiomindustries.provider002.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.axiomindustries.provider002.MyProvider;
import com.axiomindustries.provider002.database.ContractNotification;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("OverrideAbstract")
public class NotificationService extends NotificationListenerService{

    private NotificationServiceReceiver notificationServiceReceiver;

    @Override
    public void onCreate(){                                                                 // Register notification service receiver in onCreate
        super.onCreate();
        notificationServiceReceiver = new com.axiomindustries.provider002.services.NotificationServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ccom.axiomindustries.provider002.NOTIFICATION_APP_A");
        registerReceiver(notificationServiceReceiver, intentFilter);
        Log.d("onNotificationService:", "Broadcast receiver registered");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(notificationServiceReceiver);
    }

    @Override
    public IBinder onBind(Intent intent){
        return super.onBind(intent);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification){

        /*
         we have several options here to filter out null values and junk messages but, I'll let it be as such for now.
         Tickers and notification extras are null in many notifications
         We get several junk notifications like "Checking for messages", "2 conversations", etc.
         */

        @SuppressLint("SimpleDateFormat")
        String currentDateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        String notification_ticker  = String.valueOf(statusBarNotification.getNotification().tickerText);
        String notification_package = statusBarNotification.getPackageName();
        String notification_extra   = String.valueOf(statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT));

        byte[] ico= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
               Drawable drawable = statusBarNotification.getNotification().getSmallIcon().loadDrawable(this);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                ico = stream.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("ticker:",notification_ticker);
        Log.d("pkg:",notification_package);
        Log.d("extra:",notification_extra);
        Log.d("timestamp:",currentDateAndTime);

        ContentValues values = new ContentValues();
        values.put(ContractNotification.COLUMN_NOTIFICATION_TIME, currentDateAndTime);
        values.put(ContractNotification.COLUMN_NOTIFICATION_TICKER, notification_ticker);
        values.put(ContractNotification.COLUMN_NOTIFICATION_EXTRA, notification_extra);
        values.put(ContractNotification.COLUMN_NOTIFICATION_PACKAGE, notification_package);
        values.put(ContractNotification.COLUMN_NOTIFICATION_ICON, ico);
        Uri uri = getContentResolver().insert(MyProvider.CONTENT_URI, values);

        if(uri!=null)
            Log.d("Success","New record inserted");
    }
}
