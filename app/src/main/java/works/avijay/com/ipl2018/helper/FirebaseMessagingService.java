package works.avijay.com.ipl2018.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import works.avijay.com.ipl2018.LiveScores;
import works.avijay.com.ipl2018.MainActivity;
import works.avijay.com.ipl2018.R;

/**
 * Created by avinashk on 03/03/18.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d("FIREBASE", "Message data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().get("notification_type").equals("card")){
                Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image_url"));
                createCardNotification(remoteMessage.getData().get("title"), bitmap);
            }else if(remoteMessage.getData().get("notification_type").equals("score")){
                createScoreNotification(remoteMessage.getData().get("title"));
            }
        }
    }


    public void createScoreNotification(String title){
        Intent intent = new Intent(this, LiveScores.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(title);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.bat_ball_icon);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


    public void createCardNotification(String title, Bitmap image){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(title);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.bat_ball_icon);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(image));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }



    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
