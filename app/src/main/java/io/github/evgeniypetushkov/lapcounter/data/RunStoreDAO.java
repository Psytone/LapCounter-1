package io.github.evgeniypetushkov.lapcounter.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RunStoreDAO {

    @Query("SELECT * FROM runs ORDER BY title")
    List<RunsEntity> selectAll();

    @Query("SELECT * FROM runs WHERE title=:title")
    RunsEntity findById(String title);

    @Insert
    void insert(RunsEntity runsEntity);

    @Delete
    void delete(RunsEntity runsEntity);

    @Update
    void update(RunsEntity runsEntity);
}
