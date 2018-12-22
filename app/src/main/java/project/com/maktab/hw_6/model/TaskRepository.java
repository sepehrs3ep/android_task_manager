package project.com.maktab.hw_6.model;

import java.util.List;

public class TaskRepository {
    private static TaskRepository mInstance;
    private List<Task> mTaskList;

    private TaskRepository() {

    }

    public List<Task> getTaskList() {
        return mTaskList;
    }

    public static TaskRepository getInstance() {
        if (mInstance == null)
            mInstance = new TaskRepository();
        return mInstance;
    }
}
