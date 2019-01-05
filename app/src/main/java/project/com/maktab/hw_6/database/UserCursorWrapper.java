package project.com.maktab.hw_6.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import project.com.maktab.hw_6.model.user.User;

public class UserCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {

        String name = getString(getColumnIndex(TaskDbSchema.UserTable.Cols.USER_NAME));
        String password = getString(getColumnIndex(TaskDbSchema.UserTable.Cols.PASSWORD));
        User user = new User();
        user.setName(name);
        user.setPassword(password);

        return user;
    }
}
