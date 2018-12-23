package project.com.maktab.hw_6.model;

import java.util.Date;
import java.util.UUID;

public class Task {
    private String mTitle;
    private String mDescription;
    private Date mDate;
    private Date mTime;
    private UUID mID;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }


    public Task(){
        mID = UUID.randomUUID();
        mDate = new Date();
        mTime = new Date();
    }
    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

}
