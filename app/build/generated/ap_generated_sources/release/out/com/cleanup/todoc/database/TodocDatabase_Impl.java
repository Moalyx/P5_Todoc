package com.cleanup.todoc.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.ProjectDao_Impl;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.database.dao.TaskDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TodocDatabase_Impl extends TodocDatabase {
  private volatile ProjectDao _projectDao;

  private volatile TaskDao _taskDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Task` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `project_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `creationTimestamp` INTEGER NOT NULL, FOREIGN KEY(`project_id`) REFERENCES `Project`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        _db.execSQL("CREATE  INDEX `index_Task_project_id` ON `Task` (`project_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Project` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `color` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"49914baeec95df323b9b4f83f96cb317\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Task`");
        _db.execSQL("DROP TABLE IF EXISTS `Project`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTask = new HashMap<String, TableInfo.Column>(4);
        _columnsTask.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsTask.put("project_id", new TableInfo.Column("project_id", "INTEGER", true, 0));
        _columnsTask.put("name", new TableInfo.Column("name", "TEXT", true, 0));
        _columnsTask.put("creationTimestamp", new TableInfo.Column("creationTimestamp", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTask = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTask.add(new TableInfo.ForeignKey("Project", "NO ACTION", "NO ACTION",Arrays.asList("project_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTask = new HashSet<TableInfo.Index>(1);
        _indicesTask.add(new TableInfo.Index("index_Task_project_id", false, Arrays.asList("project_id")));
        final TableInfo _infoTask = new TableInfo("Task", _columnsTask, _foreignKeysTask, _indicesTask);
        final TableInfo _existingTask = TableInfo.read(_db, "Task");
        if (! _infoTask.equals(_existingTask)) {
          throw new IllegalStateException("Migration didn't properly handle Task(com.cleanup.todoc.model.Task).\n"
                  + " Expected:\n" + _infoTask + "\n"
                  + " Found:\n" + _existingTask);
        }
        final HashMap<String, TableInfo.Column> _columnsProject = new HashMap<String, TableInfo.Column>(3);
        _columnsProject.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsProject.put("name", new TableInfo.Column("name", "TEXT", true, 0));
        _columnsProject.put("color", new TableInfo.Column("color", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProject = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProject = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProject = new TableInfo("Project", _columnsProject, _foreignKeysProject, _indicesProject);
        final TableInfo _existingProject = TableInfo.read(_db, "Project");
        if (! _infoProject.equals(_existingProject)) {
          throw new IllegalStateException("Migration didn't properly handle Project(com.cleanup.todoc.model.Project).\n"
                  + " Expected:\n" + _infoProject + "\n"
                  + " Found:\n" + _existingProject);
        }
      }
    }, "49914baeec95df323b9b4f83f96cb317");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Task","Project");
  }

  @Override
  public ProjectDao projectDao() {
    if (_projectDao != null) {
      return _projectDao;
    } else {
      synchronized(this) {
        if(_projectDao == null) {
          _projectDao = new ProjectDao_Impl(this);
        }
        return _projectDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }
}
