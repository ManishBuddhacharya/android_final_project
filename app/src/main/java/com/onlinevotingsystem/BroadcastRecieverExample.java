package com.onlinevotingsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import createChannel.CreateChannel;

public class BroadcastRecieverExample extends BroadcastReceiver {
    private NotificationManagerCompat notificationManagerCompat;
    Context context;

    public BroadcastRecieverExample(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean noConnectivity;

        notificationManagerCompat = NotificationManagerCompat.from(context);

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity){
                Toast.makeText(context,"Disconnected", Toast.LENGTH_LONG).show();
                DisplayNotification();
            }else {
                Toast.makeText(context,"Connected", Toast.LENGTH_LONG).show();
                DisplayNotification1();
            }
        }
    }

    private void DisplayNotification1() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_vote)
                .setContentTitle("No Connectivity")
                .setContentText("No connection. please check you wifi or mobile data. ")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        notificationManagerCompat.notify(1, builder.build());

    }

    private void DisplayNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CreateChannel.CHANNEL_2)
                .setSmallIcon(R.drawable.ic_vote)
                .setContentTitle("Connected")
                .setContentText("You have been connected to a network")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        notificationManagerCompat.notify(2, builder.build());
    }
}
