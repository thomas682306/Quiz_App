package com.google.developers.mojimaster2.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.developers.mojimaster2.R;
import com.google.developers.mojimaster2.data.DataRepository;
import com.google.developers.mojimaster2.data.Smiley;

public class NotificationJobService extends JobService {

    public static final int NOTIFICATION_ID = 18;
    public static final String CHANNEL_ID = "notify-smiley";
    private boolean Jobcancelled=false;

    @Override
    public boolean onStartJob(JobParameters params) {

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        DataRepository repository = DataRepository.getInstance(getApplication());
        Smiley smiley = repository.getSmiley();

        if (notificationManager == null | smiley == null) {
            Log.i(NotificationJobService.class.getName(), "Failed to create notification");
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            CharSequence channelName = getString(R.string.notification_channel_name);
            String channelDescription = getString(R.string.notification_channel_description);

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(notificationChannel);


            NotificationCompat.Builder mbuilder= new NotificationCompat.Builder(getApplicationContext(),
                    CHANNEL_ID).setContentTitle("Smiley of the day:"+smiley.getEmoji()+"-"+ smiley.getCode())
                    .setContentText(smiley.getName())
                    .setSmallIcon(R.drawable.ic_list);
            notificationManager.notify(NOTIFICATION_ID, mbuilder.build());


        }
        return true;
    }



    @Override
    public boolean onStopJob(JobParameters params) {
        Jobcancelled=true;
        return true;
    }



}
