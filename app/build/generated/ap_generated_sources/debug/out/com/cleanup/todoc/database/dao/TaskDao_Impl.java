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
import com.cleanup.todoc.model.Task;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTask;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTask;

  public TaskDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTask = new EntityInsertionAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Task`(`id`,`project_id`,`name`,`creationTimestamp`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.projectId);
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        stmt.bindLong(4, value.creationTimestamp);
      }
    };
    this.__deletionAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Task` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public long insertTask(Task task) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfTask.insertAndReturnId(task);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTask(Task task) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfTask.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Task>> getAllTasks() {
    final String _sql = "SELECT * FROM Task";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Task>>() {
      private Observer _observer;

      @Override
      protected List<Task> compute() {
        if (_observer == null) {
          _observer = new Observer("Task") {
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
          final int _cursorIndexOfProjectId = _cursor.getColumnIndexOrThrow("project_id");
          final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfCreationTimestamp = _cursor.getColumnIndexOrThrow("creationTimestamp");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            final long _tmpProjectId;
            _tmpProjectId = _cursor.getLong(_cursorIndexOfProjectId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpCreationTimestamp;
            _tmpCreationTimestamp = _cursor.getLong(_cursorIndexOfCreationTimestamp);
            _item = new Task(_tmpProjectId,_tmpName,_tmpCreationTimestamp);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
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

  @Override
  public Cursor getTasksWithCursor() {
    final String _sql = "SELECT * FROM Task";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _tmpResult = __db.query(_statement);
    return _tmpResult;
  }
}
