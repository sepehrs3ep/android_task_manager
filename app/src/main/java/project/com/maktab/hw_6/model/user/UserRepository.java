package project.com.maktab.hw_6.model.user;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import project.com.maktab.hw_6.database.orm.App;
import project.com.maktab.hw_6.model.task.DaoSession;

public class UserRepository {

    private static UserRepository mInstance;
    private Context mContext;
    private UserDao mUserDao;
    private DaoSession mDaoSession;

    private UserRepository(Context context) {
        mDaoSession = App.getInstance().getDaoSession();
        mUserDao = mDaoSession.getUserDao();

    }

    public User getUser(Long id) {
        return mUserDao.load(id);

    }

    public void deleteAccount(Long id) {
        mUserDao.deleteByKey(id);

    }

    public long login(String userName, String password) {
        List<User> result =
                mUserDao.queryBuilder()
                        .where(UserDao.Properties.MName.eq(userName))
                        .where(UserDao.Properties.MPassword.eq(password))
                        .list();

        if (result.size() > 0)
            return result.get(0).getId();


        if (checkUserNameExist(userName))
            return -2;

        return -1;
    }

    private boolean checkUserNameExist(String userName) {
        List<User> result =
                mUserDao.queryBuilder()
                        .where(UserDao.Properties.MName.eq(userName))
                        .list();
        if (result.size() > 0)
            return true;
        return false;
    }

    public Long getUserId(String username) {
        List<User> result = mUserDao.queryBuilder()
                .where(UserDao.Properties.MName.eq(username))
                .list();

        if (result.size() > 0)
            return result.get(0).getId();

        return -1l;

    }

    public int updateUser(User user) {
        if (checkUserNameExist(user.getMName()))
            return -1;
        mUserDao.update(user);
        return 1;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long createUser(User user) {
        Long userId =mUserDao.insert(user);
        return userId;
    }


    public static UserRepository getInstance(Context context) {
        if (mInstance == null)
            mInstance = new UserRepository(context);

        return mInstance;
    }


}
