package project.com.maktab.hw_6.model.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import project.com.maktab.hw_6.database.TaskBaseHelper;
import project.com.maktab.hw_6.database.TaskCursorWrapper;
import project.com.maktab.hw_6.database.TaskDbSchema;

public class TaskRepository {
    private static TaskRepository mInstance;
    private SQLiteDatabase mDatabase;
    private Context mContext;


    private TaskRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    public void addTask(Task task) {
        ContentValues values = getContentValues(task);
        Log.d("yeh test", "add test" + values.toString());
        mDatabase.insert(TaskDbSchema.TaskTable.NAME, null, values);
    }

    public void updateTask(Task task) {
        ContentValues values = getContentValues(task);
        String whereClause = TaskDbSchema.TaskTable.Cols.UUID + " = ? ";
        String[] whereArgs = new String[]{task.getID().toString()};

        Log.d("yeh test", "update test" + values.toString());
        mDatabase.update(TaskDbSchema.TaskTable.NAME, values, whereClause, whereArgs);


    }

    public void clearLists() {
        mDatabase.execSQL(" delete from " + TaskDbSchema.TaskTable.NAME);
    }

    public void removeTask(UUID id) {
        String whereClause = TaskDbSchema.TaskTable.Cols.UUID + " = ? ";
        String[] args = new String[]{id.toString()};
        mDatabase.delete(TaskDbSchema.TaskTable.NAME, whereClause, args);
    }

    public List<Task> getDoneTaskList() {
        String whereClause = TaskDbSchema.TaskTable.Cols.TYPE + " = ? ";
        String[] whereArgs = new String[]{"done"};
        List<Task> crimes = new ArrayList<>();
        TaskCursorWrapper cursor = queryTask(whereClause, whereArgs);

        try {
            if (cursor.getCount() == 0)
                return crimes;

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                crimes.add(cursor.getTask());

                cursor.moveToNext();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            cursor.close();
        }

        return crimes;
    }


    public List<Task> getUnDoneTaskList() {
        String whereClause = TaskDbSchema.TaskTable.Cols.TYPE + " = ? ";
        String[] whereArgs = new String[]{"undone"};
        List<Task> crimes = new ArrayList<>();
        TaskCursorWrapper cursor = queryTask(whereClause, whereArgs);

        try {
            if (cursor.getCount() == 0)
                return crimes;

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                crimes.add(cursor.getTask());

                cursor.moveToNext();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            cursor.close();
        }

        return crimes;

    }


    public Task getTaskByID(UUID id) {
        String whereClause = TaskDbSchema.TaskTable.Cols.UUID + " = ? ";
        String[] args = new String[]{id.toString()};
        Task task = null;

        try (TaskCursorWrapper cursorWrapper = queryTask(whereClause, args)) {

            if (cursorWrapper.getCount() == 0)
                return null;

            cursorWrapper.moveToFirst();

            task = cursorWrapper.getTask();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return task;
    }

    public List<Task> getList() {
//        return cursorGetList(null, null);
        List<Task> crimes = new ArrayList<>();
        TaskCursorWrapper cursor = queryTask(null, null);

        try {
            if (cursor.getCount() == 0)
                return crimes;

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                crimes.add(cursor.getTask());

                cursor.moveToNext();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            cursor.close();
        }

        return crimes;

    }

 /*   private List<Task> cursorGetList(List<Task> list, String whereClause, String[] whereArgs) {
        try (TaskCursorWrapper cursorWrapper = queryTask(whereClause, whereArgs)) {
            if (cursorWrapper.getCount() == 0) return list;

            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {
                list.add(cursorWrapper.getTask());

            }

        }
        return list;
    }*/


    public static TaskRepository getInstance(Context context) {
        if (mInstance == null)
            mInstance = new TaskRepository(context);


        return mInstance;
    }

    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();

        values.put(TaskDbSchema.TaskTable.Cols.UUID, task.getID().toString());
        values.put(TaskDbSchema.TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskDbSchema.TaskTable.Cols.DESCRIPTION, task.getDescription());
        values.put(TaskDbSchema.TaskTable.Cols.DATE, task.getDate().getTime());
        values.put(TaskDbSchema.TaskTable.Cols.TYPE, task.getTaskType());

        return values;
    }

    private TaskCursorWrapper queryTask(String whereClause, String[] args) {
        Cursor cursor = mDatabase.query(TaskDbSchema.TaskTable.NAME, null, whereClause,
                args, null, null, null);

        return new TaskCursorWrapper(cursor);

    }

}
