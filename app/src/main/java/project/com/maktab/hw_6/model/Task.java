package project.com.maktab.hw_6.model;

import java.util.UUID;

public class Task {
    private String mTitle;
    private String mDescription;
    private String mDate;
    private String mTime;
    private UUID mID;

    public Task(){
        mID = UUID.randomUUID();
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

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
