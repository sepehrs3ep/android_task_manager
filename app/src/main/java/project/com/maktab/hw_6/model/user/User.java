package project.com.maktab.hw_6.model.user;

import java.util.Date;
import java.util.UUID;

public class User {
    private String mName;
    private String mPassword;
    private long mId;
    private UUID mUserUUID;
    private Date mUserDate;
    private String mEmail;
    private byte[] mImage;

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] image) {
        mImage = image;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setId(long id) {
        mId = id;
    }

    public User(UUID id){
        this.mUserUUID = id;
        mUserDate = new Date();

    }
    public User(){
        this(UUID.randomUUID());
    }

    public Date getUserDate() {
        return mUserDate;
    }

    public void setUserDate(Date userDate) {
        mUserDate = userDate;
    }

    public UUID getUserUUID() {
        return mUserUUID;
    }

    public long getId() {
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
