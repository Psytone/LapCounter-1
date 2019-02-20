package io.github.evgeniypetushkov.lapcounter.data;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.evgeniypetushkov.lapcounter.RunsApplication;

class RunsDatabaseWrapper {

    private final Handler handler = new Handler();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RunStoreDAO runsDAO;
    private RunsDatabase runsDatabase;

    RunsDatabaseWrapper(RunsApplication runsApplication) {
        executorService.execute(() -> {
            runsDatabase = RunsDatabase.get(runsApplication);
            runsDAO = runsDatabase.runStoreDAO();
        });
    }

    void add(RunsEntity re, Runnable onComplete) {
        executorService.execute(() -> {
            runsDAO.insert(re);
            handler.post(onComplete);
        });
    }

    void update(RunsEntity re, Runnable onComplete) {
        executorService.execute(() -> {
            runsDAO.update(re);
            handler.post(onComplete);
        });
    }

    void delete(RunsEntity re, Runnable onComplete) {
        executorService.execute(() -> {
            runsDAO.delete(re);
            handler.post(onComplete);
        });
    }

    void deleteAll(Runnable onComplete) {
        executorService.execute(() -> {
            runsDatabase.clearAllTables();
            handler.post(onComplete);
        });
    }

    RunStoreDAO getRunsDAO() {
        return runsDAO;
    }
}




