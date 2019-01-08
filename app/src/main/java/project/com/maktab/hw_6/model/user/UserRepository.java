package project.com.maktab.hw_6.model.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        User user1;
        String search_query = " select * from " + TaskDbSchema.TaskTable.NAME +
                " where " +
                " cast(" + TaskDbSchema.UserTable.Cols._ID + " as text) = ? ";
        String[] args = new String[]{
                String.valueOf(id)
        };
        Cursor cursorWrapper = mDatabase.rawQuery(search_query, args);

        try {
            if (cursorWrapper.getCount() <= 0) return null;

            cursorWrapper.moveToFirst();

            String name = cursorWrapper.getString(cursorWrapper.getColumnIndex(TaskDbSchema.UserTable.Cols.USER_NAME));
            String password = cursorWrapper.getString(cursorWrapper.getColumnIndex(TaskDbSchema.UserTable.Cols.PASSWORD));
            UUID uuid = UUID.fromString(cursorWrapper.getString(cursorWrapper.getColumnIndex(TaskDbSchema.UserTable.Cols.UUID)));
            user1 = new User(uuid);
            user1.setName(name);
            user1.setPassword(password);


        } finally {
            cursorWrapper.close();

        }

        return user1;
    }
    public void deleteAccount(long id){
        String search_query = " delete from " + TaskDbSchema.TaskTable.NAME +
                " where " +
                " cast(" + TaskDbSchema.UserTable.Cols._ID + " as text) = " + String.valueOf(id);

        mDatabase.execSQL(search_query);

    }
    public long login(User user) {
        String whereClause = TaskDbSchema.UserTable.Cols.USER_NAME + " = ? AND " +
                TaskDbSchema.UserTable.Cols.PASSWORD + " = ? ";
        String[] whereArgs = new String[]{
                user.getName().toLowerCase(),
                user.getPassword()
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
//                long id = cursor.getLong(cursor.getColumnIndex(TaskDbSchema.UserTable.Cols._ID));
                return cursor.getUser().getId();

            }
        } finally {

            cursor.close();
        }

        return -1;
    }
    public void updateUser(User user){
        String whereClause = TaskDbSchema.UserTable.Cols.UUID + " = ? ";
        String[] args = new String[]{
                String.valueOf(user.getUserUUID())
        };
        ContentValues values = getContentValues(user);
        mDatabase.update(TaskDbSchema.UserTable.NAME,values,whereClause,args);


    }
    public long createUser(User user) {
        String whereClause = TaskDbSchema.UserTable.Cols.USER_NAME + " = ? ";
        String[] args = new String[]{
                user.getName().toLowerCase()
        };


        UserCursorWrapper cursor = userQuery(whereClause, args, null);
        try {

            if (cursor.getCount() > 0)
                return -1;
        } finally {
            cursor.close();
        }


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
