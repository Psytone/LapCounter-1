package io.github.evgeniypetushkov.lapcounter.data;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "runs")
public class RunsEntity {

    @PrimaryKey
    @NonNull
    public final String title;
    @TypeConverters(RunsConverter.class)
    public final ArrayList<String> laps;

    public RunsEntity(@NonNull String title, ArrayList<String> laps) {
        this.laps = laps;
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
