package io.github.evgeniypetushkov.lapcounter.ui.stopwatch;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.github.evgeniypetushkov.lapcounter.GPSService;
import io.github.evgeniypetushkov.lapcounter.LapListener;
import io.github.evgeniypetushkov.lapcounter.R;
import io.github.evgeniypetushkov.lapcounter.RunsApplication;
import io.github.evgeniypetushkov.lapcounter.data.RunsData;
import io.github.evgeniypetushkov.lapcounter.ui.RVAdapter;

public class StopWatchViewModel extends AndroidViewModel implements Runnable, LapListener, LocationListener {

    private final LocationManager locationManager;
    private final RVAdapter rvAdapter;
    private final RunsApplication runsApplication;
    private final Intent startGPSServiceIntent;
    private final Handler handler;
    private final RunsData runsData;

    private final MutableLiveData<Integer> rvLastPosition = new MutableLiveData<>();
    private final MutableLiveData<String> currentTime = new MutableLiveData<>();
    private final MutableLiveData<String> gpsStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRunning;

    public StopWatchViewModel(@NonNull Application application) {
        super(application);
        handler = new Handler();
        runsApplication = (RunsApplication) application;
        runsData = runsApplication.runsData;

        isRunning = (MutableLiveData<Boolean>) runsData.getIsRunning();
        startGPSServiceIntent = new Intent(runsApplication, GPSService.class);
        locationManager = (LocationManager) runsApplication.getSystemService(Context.LOCATION_SERVICE);
        rvAdapter = new RVAdapter(runsData.lapArray, runsApplication);

        if (isRunning.getValue()) handler.post(this);

        currentTime.setValue(runsData.stopWatch.msToText(runsData.stopWatch.getLapsed()));
    }

    public LiveData<Boolean> getIsRunning() {
        return isRunning;
    }

    public LiveData<Integer> getRvLastPosition() {
        return rvLastPosition;
    }

    public LiveData<String> getCurrentTime() {
        return currentTime;
    }

    public LiveData<String> getGpsStatus() {
        return gpsStatus;
    }

    @SuppressWarnings({"MissingPermission"})
    void onStartClicked() {
        if (!isRunning.getValue()) {
            runsData.stopWatch.start();
            isRunning.setValue(true);
            handler.post(this);
            runsApplication.startService(startGPSServiceIntent);
        } else {
            runsData.stopWatch.stop();
            isRunning.setValue(false);
            handler.removeCallbacks(this);
            runsApplication.stopService(startGPSServiceIntent);
        }
    }

    void onLapClicked() {
        if (isRunning.getValue()) {
            runsData.lapArray.add(runsData.stopWatch.lap());
            rvAdapter.notifyDataSetChanged();
            rvLastPosition.setValue(runsData.lapArray.size() - 1);
        } else {
            if ((runsData.stopWatch.getFirstStart() != 0) && (!runsData.lapArray.isEmpty())) {
                ArrayList<String> laps = new ArrayList<>(runsData.lapArray);
                String title = runsData.stopWatch.getStartTimeDate();
                runsData.runsStorage.add(title, laps);
            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    void onResetClicked() {
        handler.removeCallbacks(this);
        runsData.lapArray.clear();
        rvAdapter.notifyDataSetChanged();
        runsData.stopWatch.reset();
        isRunning.setValue(false);
        currentTime.setValue(runsData.stopWatch.msToText(0L));
        runsApplication.stopService(startGPSServiceIntent);
    }

    LinearLayoutManager getLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(runsApplication);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        return manager;
    }

    RVAdapter getRVAdapter() {
        return rvAdapter;
    }

    @Override // stopwatch isRunning
    public void run() {
        currentTime.setValue(runsData.stopWatch.getTime());
        handler.post(this);
    }

    @Override
    public void onLap() {
        rvAdapter.notifyDataSetChanged();
        rvLastPosition.setValue(runsData.lapArray.size() - 1);
    }

    @SuppressWarnings({"MissingPermission"})
    public void subscribeGPS() {
        runsData.subscribeLapListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsStatus.setValue(runsApplication.getString(R.string.gps_status_enabled));
        } else {
            gpsStatus.setValue(runsApplication.getString(R.string.gps_status_disabled));
        }
    }

    @SuppressWarnings({"MissingPermission"})
    public void unsubscribeGPS() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double accuracy = location.getAccuracy();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        gpsStatus.setValue(runsApplication.getString(R.string.accuracy_status, accuracy, latitude, longitude));
    }

    @Override
    public void onProviderDisabled(String s) {
        gpsStatus.setValue(runsApplication.getString(R.string.gps_status_disabled));
    }

    @Override
    public void onProviderEnabled(String s) {
        gpsStatus.setValue(runsApplication.getString(R.string.gps_status_enabled));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // required for interface, not used
    }

    @Override
    protected void onCleared() {
        runsData.unsubscribeLapListener();
        super.onCleared();
    }
}
