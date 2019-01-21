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
                " create table " + TaskDbSchema.TaskTable.NAME + " (" +
                        "_id integer primary key autoincrement, "
                        + TaskDbSchema.TaskTable.Cols.UUID + ", "
                        + TaskDbSchema.TaskTable.Cols.TITLE + ", "
                        + TaskDbSchema.TaskTable.Cols.DESCRIPTION + ", "
                        + TaskDbSchema.TaskTable.Cols.DATE + ", "
                        + TaskDbSchema.TaskTable.Cols.TYPE + ", "
                        + TaskDbSchema.TaskTable.Cols.USER_ID
                        + " )"
        );
        db.execSQL(" create table " + TaskDbSchema.UserTable.NAME + " (" +
                TaskDbSchema.UserTable.Cols._ID + " integer primary key autoincrement, "
                + TaskDbSchema.UserTable.Cols.UUID + ", "
                + TaskDbSchema.UserTable.Cols.USER_NAME + ", "
                + TaskDbSchema.UserTable.Cols.EMAIL + ", "
                + TaskDbSchema.UserTable.Cols.DATE + ", "
                + TaskDbSchema.UserTable.Cols.PASSWORD + ", "
                + TaskDbSchema.UserTable.Cols.IMAGE
//                + TaskDbSchema.UserTable.Cols.IMAGE + " BLOB "
                + ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
