package project.com.maktab.hw_6.model.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import project.com.maktab.hw_6.database.orm.App;
import project.com.maktab.hw_6.model.user.User;

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
        mTaskDao.deleteByKey(userId);
    }

    public void removeTask(Task task) {
        mTaskDao.delete(task);
    }

    public List<Task> getDoneTaskList(Long userId) {
        return getDifLists(userId, TaskType.DONE);
    }


    public List<Task> getUnDoneTaskList(Long userId) {
        return getDifLists(userId,TaskType.UNDONE);
    }

    private List<Task> getDifLists(Long id, String taskType) {


        List<Task> list = mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUserID.eq(id))
                .where(TaskDao.Properties.MTaskType.eq(taskType))
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
