package project.com.maktab.hw_6.model.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import project.com.maktab.hw_6.database.TaskBaseHelper;
import project.com.maktab.hw_6.database.TaskDbSchema;
import project.com.maktab.hw_6.database.UserCursorWrapper;

public class UserRepository {
    private static UserRepository mInstance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private UserRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(context).getWritableDatabase();
    }

    private long login(User user) {
        String whereClause = TaskDbSchema.UserTable.Cols.USER_NAME + " = ? AND " +
                TaskDbSchema.UserTable.Cols.PASSWORD + " = ? ";
        String[] whereArgs = new String[]{
                user.getName(),
                user.getPassword()
        };
        String[] cols = new String[]{
                TaskDbSchema.UserTable.Cols._ID
        };
        Cursor cursor = mDatabase.query(TaskDbSchema.UserTable.NAME, cols,
                whereClause, whereArgs, null, null, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                long id = cursor.getLong(cursor.getColumnIndex(TaskDbSchema.UserTable.Cols._ID));
                return id;

            }
        } finally {

            cursor.close();
        }

        return -1;
    }

    private long createUser(User user) {
        ContentValues values = getContentValues(user);
        long id = mDatabase.insert(TaskDbSchema.UserTable.NAME, null, values);
        return id;
    }

    public static UserRepository getInstance(Context context) {
        if (mInstance == null)
            mInstance = new UserRepository(context);

        return mInstance;
    }

    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.UserTable.Cols.USER_NAME, user.getName());
        values.put(TaskDbSchema.UserTable.Cols.PASSWORD, user.getPassword());

        return values;
    }


}
