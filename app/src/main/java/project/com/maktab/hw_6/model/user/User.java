package project.com.maktab.hw_6.model.user;

import java.util.UUID;

public class User {
    private UUID mId;
    private String mName;
    private String mPassword;

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

    public User() {
        this(UUID.randomUUID());
    }

    public User(UUID id) {
        mId = id;
    }


}
