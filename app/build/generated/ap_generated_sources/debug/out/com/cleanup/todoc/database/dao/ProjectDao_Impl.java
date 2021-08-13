package com.cleanup.todoc.database.dao;

import android.arch.lifecycle.ComputableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.cleanup.todoc.model.Project;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjectDao_Impl implements ProjectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfProject;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfProject;

  public ProjectDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProject = new EntityInsertionAdapter<Project>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Project`(`id`,`name`,`color`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Project value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        stmt.bindLong(3, value.getColor());
      }
    };
    this.__deletionAdapterOfProject = new EntityDeletionOrUpdateAdapter<Project>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Project` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Project value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertProject(Project project) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfProject.insert(project);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteProject(Project project) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfProject.handle(project);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Project>> getAllProjects() {
    final String _sql = "SELECT * FROM Project";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Project>>() {
      private Observer _observer;

      @Override
      protected List<Project> compute() {
        if (_observer == null) {
          _observer = new Observer("Project") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfColor = _cursor.getColumnIndexOrThrow("color");
          final List<Project> _result = new ArrayList<Project>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Project _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            _item = new Project(_tmpId,_tmpName,_tmpColor);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }
}
