package io.github.evgeniypetushkov.lapcounter.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = RunsEntity.class, version = 1, exportSchema = false)
@TypeConverters(RunsConverter.class)
abstract class RunsDatabase extends RoomDatabase {

    private static final String DB_NAME = "lapcounterruns.db";
    private static volatile RunsDatabase INSTANCE = null;

    synchronized static RunsDatabase get(Context ctxt) {
        if (INSTANCE == null) INSTANCE = create(ctxt);
        return (INSTANCE);
    }

    private static RunsDatabase create(Context ctxt) {
        RoomDatabase.Builder<RunsDatabase> b = Room.databaseBuilder(ctxt.getApplicationContext(), RunsDatabase.class, DB_NAME);
        return (b.build());
    }

    abstract RunStoreDAO runStoreDAO();
}
