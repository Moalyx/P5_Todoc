package com.cleanup.todoc.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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

    public TaskViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.projectDataRepository = projectDataRepository;
        this.taskDataRepository = taskDataRepository;
        this.executor = executor;
    }

    //------------PROJECT----------
    public LiveData<List<Project>> getAllProjects() {
        return projectDataRepository.getAllProjects();
    }



    //----------TASK----------
    public LiveData<List<Task>> getAllTasks() {
        return taskDataRepository.getAllTasks();
    }


    public void insertTask(Task task) {
        executor.execute(() -> taskDataRepository.insertTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> taskDataRepository.deleteTask(task));
    }

}
