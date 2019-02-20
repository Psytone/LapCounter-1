package io.github.evgeniypetushkov.lapcounter.data;

import android.location.Location;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.github.evgeniypetushkov.lapcounter.LapListener;
import io.github.evgeniypetushkov.lapcounter.RunsApplication;
import io.github.evgeniypetushkov.lapcounter.StopWatch;

public class RunsData {

    public final ArrayList<String> lapArray = new ArrayList<>();
    public final ArrayList<Location> locationArray = new ArrayList<>();
    public final StopWatch stopWatch = new StopWatch();
    public final RunsStorage runsStorage;
    private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>();
    private LapListener lapListener;
    private Location startLocation;
    private boolean isAwayFromStart = false;

    public RunsData(RunsApplication runsApplication) {

        isRunning.setValue(false);

        runsStorage = new RunsStorage(runsApplication);
    }

    public LiveData<Boolean> getIsRunning() {
        return isRunning;
    }

    public void subscribeLapListener(LapListener lapListener) {
        this.lapListener = lapListener;
    }

    public void unsubscribeLapListener() {
        lapListener = null;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public LapListener getLapListener() {
        return lapListener;
    }

    public boolean isAwayFromStart() {
        return isAwayFromStart;
    }

    public void setAwayFromStart(boolean awayFromStart) {
        isAwayFromStart = awayFromStart;
    }

}
