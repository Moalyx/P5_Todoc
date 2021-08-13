package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getAllTasks ();

    @Query("SELECT * FROM Task")
    Cursor getTasksWithCursor();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(Task task);

    @Delete
    void deleteTask(Task task);






}
