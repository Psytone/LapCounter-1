package io.github.evgeniypetushkov.lapcounter.ui.stopwatch;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.evgeniypetushkov.lapcounter.R;

public class StopWatchView {

    @BindView(R.id.start)
    Button startButton;
    @BindView(R.id.lap)
    Button lapButton;
    @BindView(R.id.reset)
    Button resetButton;
    @BindView(R.id.time)
    TextView timeView;
    @BindView(R.id.status)
    TextView statusView;
    @BindView(R.id.list)
    RecyclerView lapsRecyclerView;

    public StopWatchView(View stopWatchLayout, StopWatchViewModel stopWatchViewModel) {
        ButterKnife.bind(this, stopWatchLayout);

        lapsRecyclerView.setHasFixedSize(true);
        lapsRecyclerView.setLayoutManager(stopWatchViewModel.getLayoutManager());
        lapsRecyclerView.setAdapter(stopWatchViewModel.getRVAdapter());

        startButton.setOnClickListener((v) -> stopWatchViewModel.onStartClicked());
        lapButton.setOnClickListener((v) -> stopWatchViewModel.onLapClicked());
        resetButton.setOnClickListener((v) -> stopWatchViewModel.onResetClicked());
    }

    public void toggleButtonsText(boolean isRunning) {
        if (isRunning) {
            startButton.setText(R.string.stop_button);
            lapButton.setText(R.string.lap_button);
        } else {
            startButton.setText(R.string.start_button);
            lapButton.setText(R.string.save_button);
        }
    }

    public void setStatus(String status) {
        statusView.setText(status);
    }

    public void setTime(String time) {
        timeView.setText(time);
    }

    public void scrollLapsRecyclerView(int lastPosition) {
        lapsRecyclerView.scrollToPosition(lastPosition);
    }
}
