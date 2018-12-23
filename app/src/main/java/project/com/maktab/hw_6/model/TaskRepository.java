package project.com.maktab.hw_6.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository {
    private static TaskRepository mInstance;
    private List<Task> mTaskListAll;
    private List<Task> mTaskListDone;

    private TaskRepository() {
        mTaskListAll = new ArrayList<>();
        mTaskListDone = new ArrayList<>();
    }

    public void addToAll(String title, String desc) {
        Task task = getTask(title, desc);
        mTaskListAll.add(task);
    }

    public void removeFromAll(UUID id) {
        for (int i = 0; i < mTaskListAll.size(); i++) {
            if (mTaskListAll.get(i).getID().equals(id)) mTaskListAll.remove(i);
        }
    }

    public void addToDone(Task task) {
        mTaskListDone.add(task);
        removeFromAll(task.getID());
    }

    private Task getTask(String title, String desc) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        return task;
    }

    public Task getTaskByID(UUID id) {
        for (Task task : mTaskListAll) {
            if (task.getID().equals(id))
                return task;
        }
        return null;
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
