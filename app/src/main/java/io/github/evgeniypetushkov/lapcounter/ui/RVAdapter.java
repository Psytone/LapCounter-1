package io.github.evgeniypetushkov.lapcounter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.evgeniypetushkov.lapcounter.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LapsViewHolder> {

    private final Context appContext;
    private ArrayList<String> lapList;

    public RVAdapter(ArrayList<String> lapList, Context appContext) {
        this.lapList = lapList;
        this.appContext = appContext;
    }

    public void updateList(ArrayList<String> lapsList) {
        this.lapList = lapsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LapsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lap_item, parent, false);
        return new LapsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LapsViewHolder holder, int position) {
        holder.lapTextView.setText(appContext.getString(R.string.lap_string, position + 1, lapList.get(position)));
    }

    @Override
    public int getItemCount() {
        return lapList.size();
    }

    static class LapsViewHolder extends RecyclerView.ViewHolder {
        TextView lapTextView;

        LapsViewHolder(TextView v) {
            super(v);
            lapTextView = v;
        }
    }
}
