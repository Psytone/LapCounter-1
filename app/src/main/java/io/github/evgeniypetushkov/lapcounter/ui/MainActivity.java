package io.github.evgeniypetushkov.lapcounter.ui;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import io.github.evgeniypetushkov.lapcounter.R;
import io.github.evgeniypetushkov.lapcounter.ui.history.HistoryView;
import io.github.evgeniypetushkov.lapcounter.ui.history.HistoryViewModel;
import io.github.evgeniypetushkov.lapcounter.ui.stopwatch.StopWatchView;
import io.github.evgeniypetushkov.lapcounter.ui.stopwatch.StopWatchViewModel;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private StopWatchViewModel stopWatchViewModel;
    private HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        LayoutInflater inflater = getLayoutInflater();

        View stopWatchLayout = inflater.inflate(R.layout.activity_main, null);
        View historyLayout = inflater.inflate(R.layout.history, null);

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(stopWatchLayout, historyLayout, getApplication()));

        stopWatchViewModel = ViewModelProviders.of(this).get(StopWatchViewModel.class);
        StopWatchView stopWatchView = new StopWatchView(stopWatchLayout, stopWatchViewModel);
        stopWatchViewModel.getIsRunning().observe(this, stopWatchView::toggleButtonsText);
        stopWatchViewModel.getRvLastPosition().observe(this, stopWatchView::scrollLapsRecyclerView);
        stopWatchViewModel.getCurrentTime().observe(this, stopWatchView::setTime);
        stopWatchViewModel.getGpsStatus().observe(this, stopWatchView::setStatus);

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        HistoryView historyView = new HistoryView(historyLayout, historyViewModel);
        historyViewModel.getSpinnerPosition().observe(this, historyView::setSpinnerPosition);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            stopWatchViewModel.subscribeGPS();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_rationale), 1, PERMISSIONS[0]);
        }
    }

    @Override
    protected void onPause() {
        stopWatchViewModel.unsubscribeGPS();
        super.onPause();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        stopWatchViewModel.subscribeGPS();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            if (!EasyPermissions.hasPermissions(this, PERMISSIONS))
                new AppSettingsDialog.Builder(this).build().show();
        } else {
            finish();
        }
    }
}