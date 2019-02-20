package io.github.evgeniypetushkov.lapcounter.ui.history;

import android.app.Application;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.github.evgeniypetushkov.lapcounter.R;
import io.github.evgeniypetushkov.lapcounter.RunsApplication;
import io.github.evgeniypetushkov.lapcounter.data.RunsData;
import io.github.evgeniypetushkov.lapcounter.data.RunsStorage;
import io.github.evgeniypetushkov.lapcounter.ui.RVAdapter;

public class HistoryViewModel extends AndroidViewModel implements AdapterView.OnItemSelectedListener, RunsStorage.UpdateListener {

    private final RunsData runsData;
    private final RVAdapter rvAdapter;
    private final ArrayAdapter<String> spinAdapter;
    private final RunsApplication runsApplication;
    private final MutableLiveData<Integer> spinnerPosition = new MutableLiveData<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        runsApplication = (RunsApplication) application;
        runsData = runsApplication.runsData;

        runsData.runsStorage.setUpdateListener(this);

        rvAdapter = new RVAdapter(new ArrayList<>(), runsApplication);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(runsApplication, R.style.AppTheme);
        spinAdapter = new ArrayAdapter<>(contextThemeWrapper, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        runsData.runsStorage.setupRunsEntitiesList();
    }

    public LiveData<Integer> getSpinnerPosition() {
        return spinnerPosition;
    }

    private void setSpinnerPosition(int position) {
        spinnerPosition.setValue(position);
    }

    @Override
    public void onRunsUpdated(int selectedPosition) {
        spinAdapter.clear();
        ArrayList<String> titlesList = runsData.runsStorage.getTitlesList();
        spinAdapter.addAll(titlesList);
        setSpinnerPosition(selectedPosition);
        rvAdapter.updateList(runsData.runsStorage.getSelectedLapsList(selectedPosition));
    }

    LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(runsApplication);
    }

    RVAdapter getRVAdapter() {
        return rvAdapter;
    }

    ArrayAdapter<String> getSpinAdapter() {
        return spinAdapter;
    }

    void onDeleteAllClicked() {
        runsData.runsStorage.deleteAll();
    }

    void onDeleteClicked(int spinnerPosition) {
        runsData.runsStorage.delete(spinnerPosition);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rvAdapter.updateList(runsData.runsStorage.getSelectedLapsList(position));
        setSpinnerPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        rvAdapter.updateList(runsData.runsStorage.getSelectedLapsList(-1));
    }
}
