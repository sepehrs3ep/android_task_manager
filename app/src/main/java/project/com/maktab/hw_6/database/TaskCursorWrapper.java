package project.com.maktab.hw_6.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import project.com.maktab.hw_6.database.TaskDbSchema;
import project.com.maktab.hw_6.model.task.Task;

public class TaskCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {

        UUID id = UUID.fromString(getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.UUID)));
        String title = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.TITLE));
        String description = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.DESCRIPTION));
        Date date = new Date(getLong(getColumnIndex(TaskDbSchema.TaskTable.Cols.DATE)));
        String type = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.TYPE));
        long userId = getLong(getColumnIndex(TaskDbSchema.TaskTable.Cols.USER_ID));

        Task task = new Task(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDate(date);
        task.setTaskType(type);
        task.setUserID(userId);

        return task;
    }

}
