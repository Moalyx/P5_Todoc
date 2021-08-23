package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    private TodocDatabase database;

     private static Project projet1 = new Project(1L, "Projet Tartampion", 0xFFEADAD1);
     private static Project projet2 = new Project(2L, "Projet Lucidia", 0xFFB4CDBA);
     private static Project projet3 = new Project(3L, "Projet Circus", 0xFFA3CED2);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDatabase() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDatabase() {
        this.database.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        List<Project> projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertTrue(projects.isEmpty());

        this.database.projectDao().insertProject(projet1);
        projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertTrue(projects.get(0).getId() == projet1.getId()
                && projects.get(0).getName().equals(projet1.getName())
                && projects.get(0).getColor() == projet1.getColor());

        this.database.projectDao().insertProject(projet2);
        projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertTrue(projects.get(1).getId() == projet2.getId()
                && projects.get(1).getName().equals(projet2.getName())
                && projects.get(1).getColor() == projet2.getColor());

        this.database.projectDao().insertProject(projet3);
        projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertTrue(projects.get(2).getId() == projet3.getId()
                && projects.get(2).getName().equals(projet3.getName())
                && projects.get(2).getColor() == projet3.getColor());

    }

    @Test
    public void insertAndDeleteProject() throws InterruptedException {
        List<Project> projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        this.database.projectDao().insertProject(projet1);
        projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertTrue(projects.get(0).getId() == projet1.getId()
                && projects.get(0).getName().equals(projet1.getName())
                && projects.get(0).getColor() == projet1.getColor());
        this.database.projectDao().deleteProject(projet1);
        projects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects());
        assertTrue(projects.isEmpty());
    }
}
