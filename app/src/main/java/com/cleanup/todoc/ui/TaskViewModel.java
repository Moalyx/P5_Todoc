package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final ProjectDataRepository projectDataSource;
    private final TaskDataRepository taskDataSource;
    private final Executor executor;

    @Nullable
    private LiveData<List<Project>> projects;

    public TaskViewModel(ProjectDataRepository projectData, TaskDataRepository taskData, Executor executor) {
        this.projectDataSource = projectData;
        this.taskDataSource = taskData;
        this.executor = executor;
    }

    public void init() {
        if (getAllProjects() == null)
            projects = projectDataSource.getAllProjects();
    }

    //PROJECT
    public LiveData<List<Project>> getAllProjects() {
        return projectDataSource.getAllProjects();
    }

    public void insertProject(Project project) {
        executor.execute(() -> {
            projectDataSource.insertProject(project);
        });
    }

    //TASK
    public LiveData<List<Task>> getAllTasks() {
        return taskDataSource.getAllTasks();
    }


    public void insertTask(Task task) {
        executor.execute(() -> {
            taskDataSource.insertTask(task);
        });
    }

    public void deleteTask(Task task) {
        executor.execute(() -> {
            taskDataSource.deleteTask(task);
        });
    }

}
