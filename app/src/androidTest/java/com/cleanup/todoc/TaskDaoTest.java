package com.cleanup.todoc;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private TodocDatabase database;

    // DATA FOR TESTS
    private static Project PROJECT = new Project(1L, "Tartampion", 0xFFEADAD1);
    private static long PROJECT_ID = PROJECT.getId();
    private static Task task1 = new Task(PROJECT_ID, "test1", new Date().getTime());
    private static Task task2 = new Task(PROJECT_ID, "test2", new Date().getTime());

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        this.database.close();
    }

    @Test
    public void insertOneProjectAndGetProjects() throws InterruptedException {
        List<Project> projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertEquals(projects.size(), 0);
        this.database.projectDao().insertProject(PROJECT);
        projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertEquals(projects.size(), 1);
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertOneTaskAndGetTask() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 0);

        this.database.projectDao().insertProject(PROJECT);
        this.database.taskDao().insertTask(task1);

        tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 1);
    }

    @Test
    public void insertTasksAndDelete() throws InterruptedException {
        this.database.projectDao().insertProject(PROJECT);
        this.database.taskDao().insertTask(task1);
        this.database.taskDao().insertTask(task2);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 2);
        this.database.taskDao().deleteTask(tasks.get(0));
        tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(tasks.size(), 1);
    }
}