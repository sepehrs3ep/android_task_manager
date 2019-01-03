package project.com.maktab.hw_6.model.task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository {
    private static TaskRepository mInstance;


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
            if (mTaskList.get(i).getID().equals(id)) {
                mTaskList.remove(i);
                break;
            }
        }
    }

    public List<Task> getDoneTaskList() {
        List<Task> doneTaskList = new ArrayList<>();
        for (Task task : mTaskList) {
            if (task.getTaskType() == TaskType.DONE)
                doneTaskList.add(task);
        }
        return doneTaskList;
    }

    public List<Task> getUnDoneTaskList() {
        List<Task> unDoneTaskList = new ArrayList<>();
        for (Task task : mTaskList) {
            if (task.getTaskType() == TaskType.UNDONE)
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
