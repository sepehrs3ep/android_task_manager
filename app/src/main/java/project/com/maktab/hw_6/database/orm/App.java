package project.com.maktab.hw_6.database.orm;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import project.com.maktab.hw_6.model.task.DaoMaster;
import project.com.maktab.hw_6.model.task.DaoSession;


public class App extends Application {

    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
//        mDaoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this,"task_manager").getWritableDb()).newSession();
        MyDevOpenHelper myDevOpenHelper = new MyDevOpenHelper(this,"task_manager");
        Database database = myDevOpenHelper.getWritableDb();

        mDaoSession = new DaoMaster(database).newSession();


        mInstance = this;


    }
}
