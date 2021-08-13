package com.cleanup.todoc.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telecom.Call;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.net.ContentHandler;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {


    // --- SINGLETON ---
    private static volatile TodocDatabase INSTANCE;

    // --- DAO ---
    public abstract ProjectDao projectDao();

    public abstract TaskDao taskDao();

    // --- INSTANCE ---
    public static TodocDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, "TodocDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1L);
                contentValues.put("name", "Projet Tartampion");
                contentValues.put("color", 0xFFEADAD1);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues);

                contentValues.put("id", 2L);
                contentValues.put("name", "Projet Lucidia");
                contentValues.put("color", 0xFFB4CDBA);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues);

                contentValues.put("id", 3L);
                contentValues.put("name", "Projet Circus");
                contentValues.put("color", 0xFFA3CED2);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues);

            }
        };
    }


}
