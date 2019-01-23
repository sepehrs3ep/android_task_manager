/*
package project.com.maktab.hw_6.database.sql;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import project.com.maktab.hw_6.model.user.User;

public class UserCursorWrapper extends CursorWrapper {
    */
/**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     *//*

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {

        String name = getString(getColumnIndex(TaskDbSchema.UserTable.Cols.USER_NAME));
        String password = getString(getColumnIndex(TaskDbSchema.UserTable.Cols.PASSWORD));
        UUID uuid = UUID.fromString(getString(getColumnIndex(TaskDbSchema.UserTable.Cols.UUID)));
        Date date  = new Date(getLong(getColumnIndex(TaskDbSchema.UserTable.Cols.DATE)));
        String email = getString(getColumnIndex(TaskDbSchema.UserTable.Cols.EMAIL));
        long _id = getLong(getColumnIndex(TaskDbSchema.UserTable.Cols._ID));
        String imagePath = getString(getColumnIndex(TaskDbSchema.UserTable.Cols.IMAGE));
//        byte[] image = getBlob(getColumnIndex(TaskDbSchema.UserTable.Cols.IMAGE));

        User user = new User(uuid);
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setId(_id);
        user.setUserDate(date);
        user.setImage(imagePath);
//        user.setImage(image);


        return user;
    }
}
*/
