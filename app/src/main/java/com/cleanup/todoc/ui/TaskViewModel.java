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

    private final ProjectDataRepository projectDataRepository;
    private final TaskDataRepository taskDataRepository;
    private final Executor executor;

    @Nullable
    private LiveData<List<Project>> projects;

    public TaskViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.projectDataRepository = projectDataRepository;
        this.taskDataRepository = taskDataRepository;
        this.executor = executor;
    }

    public void init() {
        if (getAllProjects() == null)
            projects = projectDataRepository.getAllProjects();
    }

    //PROJECT
    public LiveData<List<Project>> getAllProjects() {
        return projectDataRepository.getAllProjects();
    }

    public void insertProject(Project project) {
        executor.execute(() -> {
            projectDataRepository.insertProject(project);
        });
    }

    //TASK
    public LiveData<List<Task>> getAllTasks() {
        return taskDataRepository.getAllTasks();
    }


    public void insertTask(Task task) {
        executor.execute(() -> {
            taskDataRepository.insertTask(task);
        });
    }

    public void deleteTask(Task task) {
        executor.execute(() -> {
            taskDataRepository.deleteTask(task);
        });
    }

}
