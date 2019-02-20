package io.github.evgeniypetushkov.lapcounter;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StopWatch {

    private long firstStart;
    private long lapsed;
    private long start;
    private long stop;
    private long lap;
    private long previous;
    private String startTimeDate;

    public void start() {
        start = SystemClock.elapsedRealtime();
        if (firstStart == 0) {
            firstStart = start;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            startTimeDate = sdf.format(new Date());
        }
    }

    public void stop() {
        stop = SystemClock.elapsedRealtime();
        lapsed = lapsed + stop - start;
    }

    public String lap() {

        return msToText(longLap());
    }

    long longLap() {
        previous = lap;
        lap = SystemClock.elapsedRealtime() - start + lapsed;
        return lap - previous;
    }

    public void reset() {
        lapsed = 0;
        lap = 0;
        previous = 0;
        start = 0;
        stop = 0;
        firstStart = 0;
        startTimeDate = null;
    }

    public String getTime() {
        long running = SystemClock.elapsedRealtime() - start + lapsed;
        return msToText(running);
    }

    public String msToText(long ms) {
        long millis = ms % 1000;
        long second = (ms / 1000) % 60;
        long minute = (ms / (1000 * 60)) % 60;
        long hour = (ms / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d.%03d", hour, minute, second, millis);
        return time;
    }

    public long getFirstStart() {
        return firstStart;
    }

    public long getLapsed() {
        return lapsed;
    }

    public String getStartTimeDate() {
        return startTimeDate;
    }
}
