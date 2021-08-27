package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getAllProjects();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProject(Project project);

    @Delete
    void deleteProject(Project project);
}
