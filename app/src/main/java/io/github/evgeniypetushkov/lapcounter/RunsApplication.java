package io.github.evgeniypetushkov.lapcounter;

import android.app.Application;

import io.github.evgeniypetushkov.lapcounter.data.RunsData;

public class RunsApplication extends Application {
    public final RunsData runsData = new RunsData(this);
}
