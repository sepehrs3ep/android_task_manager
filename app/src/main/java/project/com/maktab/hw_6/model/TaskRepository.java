package project.com.maktab.hw_6.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.paperdb.Paper;
import project.com.maktab.hw_6.controller.activity.MainViewPagerActivity;

public class TaskRepository {
    private static TaskRepository mInstance;

    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
    }

    private List<Task> mTaskList;

    private TaskRepository() {
        mTaskList = new ArrayList<>();
    }

    public void addTask(Task task) {
        mTaskList.add(task);
    }

    public void clearLists() {
        mTaskList.clear();

    }

    public void removeTask(UUID id) {
        for (int i = 0; i < mTaskList.size(); i++) {
            if (mTaskList.get(i).getID().equals(id)) mTaskList.remove(i);
        }
    }

    public List<Task> getDoneTaskList() {
        List<Task> doneTaskList = new ArrayList<>();
        for (Task task : mTaskList) {
            if (task.getTaskType() == 1)
                doneTaskList.add(task);
        }
        return doneTaskList;
    }

    public List<Task> getUnDoneTaskList() {
        List<Task> unDoneTaskList = new ArrayList<>();
        for (Task task : mTaskList) {
            if (task.getTaskType() == -1)
                unDoneTaskList.add(task);
        }
        return unDoneTaskList;
    }


    public Task getTaskByID(UUID id) {
        for (Task task : mTaskList) {
            if (task.getID().equals(id))
                return task;
        }
        return null;
    }

    public List<Task> getList() {
        return mTaskList;
    }


    public static TaskRepository getInstance() {
        if (mInstance == null)
            mInstance = new TaskRepository();
        return mInstance;
    }
}
