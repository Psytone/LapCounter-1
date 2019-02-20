package io.github.evgeniypetushkov.lapcounter;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import io.github.evgeniypetushkov.lapcounter.data.RunsData;
import io.github.evgeniypetushkov.lapcounter.ui.MainActivity;

public class GPSService extends Service implements LocationListener {

    private final static String CHANNEL_LAPCOUNTER = "channel_lapcounter";
    private final static int FOREGROUND_ID = 1;
    private LocationManager locationManager;
    private RunsData runsData;
    private long lapTime;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        RunsApplication runsApplication = (RunsApplication) getApplication();
        runsData = runsApplication.runsData;
        lapTime = 0L;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; //return null because we don't need this abstract method.
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(FOREGROUND_ID, buildNotification());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if (runsData.getStartLocation() == null) {
            runsData.setStartLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        return (START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!runsData.isAwayFromStart()) {
            runsData.setAwayFromStart(location.distanceTo(runsData.getStartLocation()) > 25);
            return;
        }

        if (location.distanceTo(runsData.getStartLocation()) >= 15) return;

        runsData.locationArray.add(location);
        if (runsData.locationArray.size() <= 1) return;

        Location previous = runsData.locationArray.get(runsData.locationArray.size() - 2);
        if (location.distanceTo(runsData.getStartLocation()) < previous.distanceTo(runsData.getStartLocation())) {
            lapTime = lapTime + runsData.stopWatch.longLap();
            return;
        }

        runsData.lapArray.add(runsData.stopWatch.msToText(lapTime));
        runsData.setAwayFromStart(false);
        runsData.locationArray.clear();
        lapTime = 0L;
        if (runsData.getLapListener() != null) runsData.getLapListener().onLap();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private Notification buildNotification() {
        NotificationManager mgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mgr.getNotificationChannel(CHANNEL_LAPCOUNTER) == null) {
            mgr.createNotificationChannel(new NotificationChannel(CHANNEL_LAPCOUNTER,
                    "Lapcounter", NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, CHANNEL_LAPCOUNTER);
        Intent startActivity = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, startActivity, 0);

        b.setOngoing(true)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_timer_white_108dp)
                .setContentIntent(pendingIntent);
        return (b.build());
    }
}