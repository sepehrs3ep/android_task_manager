package project.com.maktab.hw_6.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TaskBaseHelper extends SQLiteOpenHelper {
    public TaskBaseHelper(Context context) {
        super(context, TaskDbSchema.NAME, null, TaskDbSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                " create table task " + " (" +
                        "_id integer primary key autoincrement " + ", "
                        + TaskDbSchema.TaskTable.Cols.UUID + ", "
                        + TaskDbSchema.TaskTable.Cols.TITLE + ", "
                        + TaskDbSchema.TaskTable.Cols.DESCRIPTION + ", "
                        + TaskDbSchema.TaskTable.Cols.DATE + ", "
                        + TaskDbSchema.TaskTable.Cols.TYPE
                        + " )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
