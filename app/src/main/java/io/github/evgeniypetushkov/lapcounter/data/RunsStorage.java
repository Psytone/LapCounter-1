package io.github.evgeniypetushkov.lapcounter.data;

import android.os.Handler;

import java.util.ArrayList;

import io.github.evgeniypetushkov.lapcounter.RunsApplication;

public class RunsStorage {

    private final Handler handler;
    private final RunsDatabaseWrapper runsDatabaseWrapper;
    private ArrayList<RunsEntity> runsEntitiesList;
    private UpdateListener updateListener;

    RunsStorage(RunsApplication runsApplication) {
        handler = new Handler();
        runsDatabaseWrapper = new RunsDatabaseWrapper(runsApplication);
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setupRunsEntitiesList() {
        new Thread(() -> {
            runsEntitiesList = (ArrayList<RunsEntity>) runsDatabaseWrapper.getRunsDAO().selectAll();
            handler.post(() -> updateListener.onRunsUpdated(-1));
        }).start();
    }

    public void add(String title, ArrayList<String> laps) {
        RunsEntity runsEntity = new RunsEntity(title, laps);
        if (!runsEntitiesList.isEmpty() && runsEntitiesList.get(runsEntitiesList.size() - 1).title.equals(title)) {
            runsEntitiesList.set(runsEntitiesList.size() - 1, runsEntity);
            runsDatabaseWrapper.update(runsEntity, () -> updateListener.onRunsUpdated(runsEntitiesList.size() - 1));
        } else {
            runsEntitiesList.add(runsEntity);
            runsDatabaseWrapper.add(runsEntity, () -> updateListener.onRunsUpdated(runsEntitiesList.size() - 1));
        }
    }

    public void delete(int selectedPosition) {
        if (!runsEntitiesList.isEmpty() && (selectedPosition != -1)) {
            RunsEntity runsEntity = runsEntitiesList.get(selectedPosition);
            runsEntitiesList.remove(runsEntity);
            if (selectedPosition == runsEntitiesList.size()) selectedPosition--;
            int pos = selectedPosition;
            runsDatabaseWrapper.delete(runsEntity, () -> updateListener.onRunsUpdated(pos));
        }
    }

    public void deleteAll() {
        runsEntitiesList.clear();
        runsDatabaseWrapper.deleteAll(() -> updateListener.onRunsUpdated(-1));
    }

    public ArrayList<String> getTitlesList() {
        ArrayList<String> titlesList = new ArrayList<>();
        if (!runsEntitiesList.isEmpty()) {
            for (RunsEntity runs : runsEntitiesList) {
                titlesList.add(runs.title);
            }
        }
        return titlesList;
    }

    public ArrayList<String> getSelectedLapsList(int selectedPosition) {
        ArrayList<String> selectedLapsList = new ArrayList<>();
        if (!runsEntitiesList.isEmpty() && (selectedPosition != -1)) {
            selectedLapsList.addAll(runsEntitiesList.get(selectedPosition).laps);
        }
        return selectedLapsList;
    }

    public interface UpdateListener {
        void onRunsUpdated(int selectedPosition);
    }
}
