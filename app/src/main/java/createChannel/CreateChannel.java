package createChannel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class CreateChannel {
    Context context;
    public final static String CHANNEL_1 = "chanel1";
    public final static String CHANNEL_2 = "chanel2";

    public CreateChannel(Context context) {
        this.context = context;
    }

    public  void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel1 = new NotificationChannel(CHANNEL_1, "channel 1", NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription("This is Channel 1");

            NotificationChannel chanel2 = new NotificationChannel(CHANNEL_2, "channel 1", NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription("This is Channel 2");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanel1);
            manager.createNotificationChannel(chanel2);
        }

    }
}
