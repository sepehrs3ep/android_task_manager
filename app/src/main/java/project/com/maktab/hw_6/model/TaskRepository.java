package project.com.maktab.hw_6.model;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository mInstance;
    private List<Task> mTaskList;

    private TaskRepository() {
        mTaskList = new ArrayList<>();
    }
    public void addTask(String title,String desc,String date,String time){
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        task.setDate(date);
        task.setTime(time);
        mTaskList.add(task);
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
