package project.com.maktab.hw_6.model.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import project.com.maktab.hw_6.database.TaskBaseHelper;
import project.com.maktab.hw_6.database.TaskDbSchema;
import project.com.maktab.hw_6.database.UserCursorWrapper;

public class UserRepository {
    private static UserRepository mInstance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private UserRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    public User getUser(long id) {
        User user;
        String search_query = " select * from " + TaskDbSchema.UserTable.NAME +
                " where " +
                " cast ( " + TaskDbSchema.UserTable.Cols._ID + " as text ) = ? ";
        String[] args = new String[]{
                String.valueOf(id)
        };
//        String whereClause = TaskDbSchema.UserTable.Cols.UUID + " = ? ";
        Cursor cursor = mDatabase.rawQuery(search_query,args);
        UserCursorWrapper cursorWrapper = new UserCursorWrapper(cursor);
//        Cursor cursorWrapper = mDatabase.rawQuery(search_query, args);

        try {
            if (cursorWrapper.getCount() <= 0) return null;

            cursorWrapper.moveToFirst();

            /*String name = cursorWrapper.getString(cursorWrapper.getColumnIndex(TaskDbSchema.UserTable.Cols.USER_NAME));
            String password = cursorWrapper.getString(cursorWrapper.getColumnIndex(TaskDbSchema.UserTable.Cols.PASSWORD));
            UUID uuid = UUID.fromString(cursorWrapper.getString(cursorWrapper.getColumnIndex(TaskDbSchema.UserTable.Cols.UUID)));
            user = new User(uuid);
            user.setName(name);
            user.setPassword(password);*/
            user = cursorWrapper.getUser();


        } finally {
            cursorWrapper.close();

        }

        return user;
    }
    public void deleteAccount(long id){
        String userSearch_query = " delete from " + TaskDbSchema.UserTable.NAME +
                " where " +
                " cast(" + TaskDbSchema.UserTable.Cols._ID + " as text) = " + String.valueOf(id);
        String taskSearchQuery =  " delete from " + TaskDbSchema.TaskTable.NAME +
                " where " +
                " cast(" + TaskDbSchema.UserTable.Cols._ID + " as text) = " + String.valueOf(id);

        mDatabase.execSQL(userSearch_query);
        mDatabase.execSQL(taskSearchQuery);

    }
    public long login(String userName,String password) {
        if(checkUserNameExist(userName))return -2;


        String whereClause = TaskDbSchema.UserTable.Cols.USER_NAME + " = ? AND " +
                TaskDbSchema.UserTable.Cols.PASSWORD + " = ? ";
        String[] whereArgs = new String[]{
                userName.toLowerCase(),
                password
        };
        String[] cols = new String[]{
                TaskDbSchema.UserTable.Cols._ID
        };
        /*Cursor cursor = mDatabase.query(TaskDbSchema.UserTable.NAME, cols,
                whereClause, whereArgs, null, null, null);*/
        UserCursorWrapper cursor = userQuery(whereClause, whereArgs, cols);
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
    public int updateUser(User user){
        if(checkUserNameExist(user.getName()))return -1;


        String whereClause = TaskDbSchema.UserTable.Cols.UUID + " = ? ";
        String[] args = new String[]{
                user.getUserUUID().toString()
        };
        ContentValues values = getContentValues(user);
        mDatabase.update(TaskDbSchema.UserTable.NAME,values,whereClause,args);
        return 1;

    }
    public long createUser(User user) {
        if (checkUserNameExist(user.getName())) return -1;


        ContentValues values = getContentValues(user);
        long id = mDatabase.insert(TaskDbSchema.UserTable.NAME, null, values);
        return id;
    }

    private boolean checkUserNameExist(String userName) {
        String whereClause = TaskDbSchema.UserTable.Cols.USER_NAME + " = ? ";
        String[] args = new String[]{
                userName.toLowerCase()
        };


        UserCursorWrapper cursor = userQuery(whereClause, args, null);
        try {

            if (cursor.getCount() > 0)
                return true;
        } finally {
            cursor.close();
        }
        return false;
    }

    public static UserRepository getInstance(Context context) {
        if (mInstance == null)
            mInstance = new UserRepository(context);

        return mInstance;
    }

    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.UserTable.Cols.USER_NAME, user.getName().toLowerCase());
        values.put(TaskDbSchema.UserTable.Cols.PASSWORD, user.getPassword());
        values.put(TaskDbSchema.UserTable.Cols.EMAIL, user.getEmail());
        values.put(TaskDbSchema.UserTable.Cols.DATE, user.getUserDate().getTime());
        values.put(TaskDbSchema.UserTable.Cols.UUID, user.getUserUUID().toString());

        return values;
    }

    private UserCursorWrapper userQuery(String whereClause, String[] args, String[] cols) {
        Cursor cursor = mDatabase.query(TaskDbSchema.UserTable.NAME, cols,
                whereClause, args, null, null, null);
        return new UserCursorWrapper(cursor);
    }


}
