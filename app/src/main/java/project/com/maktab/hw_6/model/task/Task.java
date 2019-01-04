package project.com.maktab.hw_6.model.task;

import java.util.Date;
import java.util.UUID;

public class Task {

    private String mTitle;
    private String mDescription;
    private Date mDate;
    private UUID mID;
    private String mTaskType;
    private int mUserID;

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    public String getTaskType() {
        return mTaskType;
    }

    public void setTaskType(String taskType) {
        this.mTaskType = taskType;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    public Task(UUID id) {
        this.mID = id;
        mDate = new Date();

    }

    public Task() {
        this(UUID.randomUUID());
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
