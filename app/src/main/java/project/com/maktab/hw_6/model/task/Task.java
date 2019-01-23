package project.com.maktab.hw_6.model.task;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import project.com.maktab.hw_6.model.user.User;

import org.greenrobot.greendao.DaoException;

import project.com.maktab.hw_6.model.user.UserDao;


@Entity
public class Task {

    public enum TaskType {
        UNDONE(0), DONE(1), ALL(2);

        int mIndex;

        public int getIndex() {
            return mIndex;
        }

        TaskType(int index) {
            this.mIndex = index;
        }
    }


    @Id(autoincrement = true)
    private Long id;

    private String mTitle;
    private String mDescription;
    private Date mDate;

    //    @Transient
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mID;

    @Convert(converter = TypeConverter.class,columnType = Integer.class)
    private TaskType mTaskType;

    @ToOne(joinProperty = "mUserID")
    private User mUser;
    private Long mUserID;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    @Generated(hash = 1377221062)
    private transient Long mUser__resolvedKey;

;


    public Task(UUID id) {
        this.mID = id;
        mDate = new Date();

    }

    public Task() {
        this(UUID.randomUUID());
    }

    @Generated(hash = 788600800)
    public Task(Long id, String mTitle, String mDescription, Date mDate, UUID mID,
            TaskType mTaskType, Long mUserID) {
        this.id = id;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDate = mDate;
        this.mID = mID;
        this.mTaskType = mTaskType;
        this.mUserID = mUserID;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMTitle() {
        return this.mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMDescription() {
        return this.mDescription;
    }

    public void setMDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getMDate() {
        return this.mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    public UUID getMID() {
        return this.mID;
    }

    public void setMID(UUID mID) {
        this.mID = mID;
    }

    public TaskType getMTaskType() {
        return this.mTaskType;
    }

    public void setMTaskType(TaskType mTaskType) {
        this.mTaskType = mTaskType;
    }

    public Long getMUserID() {
        return this.mUserID;
    }

    public void setMUserID(Long mUserID) {
        this.mUserID = mUserID;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1742446920)
    public User getMUser() {
        Long __key = this.mUserID;
        if (mUser__resolvedKey == null || !mUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User mUserNew = targetDao.load(__key);
            synchronized (this) {
                mUser = mUserNew;
                mUser__resolvedKey = __key;
            }
        }
        return mUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 195124107)
    public void setMUser(User mUser) {
        synchronized (this) {
            this.mUser = mUser;
            mUserID = mUser == null ? null : mUser.getId();
            mUser__resolvedKey = mUserID;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

    public static class UuidConverter implements PropertyConverter<UUID, String> {

        @Override
        public UUID convertToEntityProperty(String databaseValue) {
            return UUID.fromString(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(UUID entityProperty) {
            return entityProperty.toString();
        }
    }

    public static class TypeConverter implements PropertyConverter<TaskType, Integer> {


        @Override
        public TaskType convertToEntityProperty(Integer databaseValue) {
            for (TaskType type : TaskType.values()) {
                if (type.getIndex() == databaseValue)
                    return type;

            }


            return null;
        }

        @Override
        public Integer convertToDatabaseValue(TaskType entityProperty) {
            return entityProperty.getIndex();
        }
    }

}
