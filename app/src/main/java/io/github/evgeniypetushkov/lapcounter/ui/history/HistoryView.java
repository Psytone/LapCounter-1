package io.github.evgeniypetushkov.lapcounter.ui.history;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.evgeniypetushkov.lapcounter.R;

public class HistoryView {

    @BindView(R.id.spinner)
    AppCompatSpinner spinner;
    @BindView(R.id.deleteAll)
    Button deleteAll;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.hlist)
    RecyclerView lapsRecyclerView;

    public HistoryView(View historyLayout, HistoryViewModel historyViewModel) {
        ButterKnife.bind(this, historyLayout);

        lapsRecyclerView.setHasFixedSize(true);
        lapsRecyclerView.setLayoutManager(historyViewModel.getLayoutManager());
        lapsRecyclerView.setAdapter(historyViewModel.getRVAdapter());

        spinner.setOnItemSelectedListener(historyViewModel);
        spinner.setAdapter(historyViewModel.getSpinAdapter());

        deleteAll.setOnClickListener((v) -> historyViewModel.onDeleteAllClicked());
        delete.setOnClickListener((v) -> historyViewModel.onDeleteClicked(spinner.getSelectedItemPosition()));
    }

    public void setSpinnerPosition(int position) {
        spinner.setSelection(position);
    }
}
