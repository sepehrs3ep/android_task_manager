package project.com.maktab.hw_6.model.user;

import java.util.UUID;

public class User {
    private String mName;
    private String mPassword;
    private int mId;

    public int getId() {
        return mId;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }




}
