package project.com.maktab.hw_6.model.task;

import java.util.Date;
import java.util.UUID;

public class Task {

    private String mTitle;
    private String mDescription;
    private Date mDate;
    private UUID mID;
//    private int taskType;
    private int mTaskType;


    public int getTaskType() {
        return mTaskType;
    }

    public void setTaskType(int taskType) {
        this.mTaskType = taskType;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }



    public Task(){
        mID = UUID.randomUUID();
        mDate = new Date();
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