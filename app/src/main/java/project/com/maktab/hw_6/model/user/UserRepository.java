package project.com.maktab.hw_6.model.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import project.com.maktab.hw_6.database.TaskBaseHelper;
import project.com.maktab.hw_6.database.TaskCursorWrapper;
import project.com.maktab.hw_6.database.TaskDbSchema;
import project.com.maktab.hw_6.database.UserCursorWrapper;

public class UserRepository {
    private static UserRepository mIntsance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private UserRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(context).getWritableDatabase();
    }
/*
    private int login(User user) {
        UserCursorWrapper cursorWrapper = mDatabase.query(TaskDbSchema.UserTable.NAME,,)

        if(cursorWrapper.getUser().getId()==user.getId())


        return -1;
    }*/

    private void createUser(User user) {
        ContentValues values = getContentValues(user);
        mDatabase.insert(TaskDbSchema.UserTable.NAME, null, values);
    }

    public static UserRepository getInstance(Context context) {
        if (mIntsance == null)
            mIntsance = new UserRepository(context);
        return mIntsance;
    }

    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.UserTable.Cols.USER_NAME, user.getName());
        values.put(TaskDbSchema.UserTable.Cols.PASSWORD, user.getPassword());

        return values;
    }
}
