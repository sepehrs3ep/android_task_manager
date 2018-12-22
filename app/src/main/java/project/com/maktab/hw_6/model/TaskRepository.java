package project.com.maktab.hw_6.model;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository mInstance;
    private List<Task> mTaskListAll;
    private List<Task> mTaskListDone;

    private TaskRepository() {
        mTaskListAll = new ArrayList<>();
        mTaskListDone = new ArrayList<>();
    }

    public void addToAll(String title, String desc, String date, String time) {
        Task task = getTask(title, desc, date, time);
        mTaskListAll.add(task);
    }

    public void addToDone(String title, String desc, String date, String time) {
        Task task = getTask(title, desc, date, time);
        mTaskListDone.add(task);
    }

    private Task getTask(String title, String desc, String date, String time) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        task.setDate(date);
        task.setTime(time);
        return task;
    }

    public List<Task> getAllList() {
        return mTaskListAll;
    }

    public List<Task> getDoneList() {
        return mTaskListDone;
    }

    public static TaskRepository getInstance() {
        if (mInstance == null)
            mInstance = new TaskRepository();
        return mInstance;
    }
}
