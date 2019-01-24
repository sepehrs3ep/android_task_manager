package project.com.maktab.hw_6.model.task;

import android.content.Context;

import java.util.List;
import java.util.UUID;

import project.com.maktab.hw_6.database.orm.App;

public class TaskRepository {
    private static TaskRepository mInstance;
    private Context mContext;
    private DaoSession mDaoSession;
    private TaskDao mTaskDao;


    private TaskRepository(Context context) {
        mDaoSession = App.getInstance().getDaoSession();
        mTaskDao = mDaoSession.getTaskDao();

    }

    public void addTask(Task task) {
        mTaskDao.insert(task);

    }

    public void updateTask(Task task) {
        mTaskDao.update(task);
    }

    public void clearLists(Long userId) {
        mTaskDao.queryBuilder().
                where(TaskDao.Properties.MUserID.eq(userId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

    }

    public void removeTask(Task task) {
        mTaskDao.delete(task);
    }

    public List<Task> getDoneTaskList(Long userId) {
        return getDifLists(userId, Task.TaskType.DONE);
    }


    public List<Task> getUnDoneTaskList(Long userId) {
        return getDifLists(userId, Task.TaskType.UNDONE);
    }

    private List<Task> getDifLists(Long id, Task.TaskType taskType) {


        List<Task> list = mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUserID.eq(id))
                .where(TaskDao.Properties.MTaskType.eq(taskType.getIndex()))
                .list();
        return list;
    }


    public Task getTaskByID(UUID id) {
        List<Task> result =
        mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MID.eq(id))
                .list();
        if(result.size()>0)
            return result.get(0);
        return null;
    }

    public List<Task> getList(Long userId) {
        List<Task> list = mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUserID.eq(userId))
                .list();
        return list;
    }

    public static TaskRepository getInstance(Context context) {
        if (mInstance == null)
            mInstance = new TaskRepository(context);


        return mInstance;
    }


}
