package project.com.maktab.hw_6.model.user;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User  {

    public User(UUID id){
        this.mUserUUID = id;
        mUserDate = new Date();

    }
    public User(){
        this(UUID.randomUUID());
    }
    @Generated(hash = 823876703)
    public User(String mName, String mPassword, Long id, UUID mUserUUID,
            Date mUserDate, String mEmail, String mImage) {
        this.mName = mName;
        this.mPassword = mPassword;
        this.id = id;
        this.mUserUUID = mUserUUID;
        this.mUserDate = mUserDate;
        this.mEmail = mEmail;
        this.mImage = mImage;
    }


    @Unique
    private String mName;
    private String mPassword;
    @Id(autoincrement = true)
    private Long id;

    @Convert(converter = UuidConverter.class,columnType = String.class)
    private UUID mUserUUID;

    private Date mUserDate;
    private String mEmail;
    private String mImage;


    public String getPhotoName(){
        return "IMG_" + this.mUserUUID.toString() + ".jpg";
    }
    public String getMName() {
        return this.mName;
    }
    public void setMName(String mName) {
        this.mName = mName;
    }
    public String getMPassword() {
        return this.mPassword;
    }
    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UUID getMUserUUID() {
        return this.mUserUUID;
    }
    public void setMUserUUID(UUID mUserUUID) {
        this.mUserUUID = mUserUUID;
    }
    public Date getMUserDate() {
        return this.mUserDate;
    }
    public void setMUserDate(Date mUserDate) {
        this.mUserDate = mUserDate;
    }
    public String getMEmail() {
        return this.mEmail;
    }
    public void setMEmail(String mEmail) {
        this.mEmail = mEmail;
    }
    public String getMImage() {
        return this.mImage;
    }
    public void setMImage(String mImage) {
        this.mImage = mImage;
    }


    public static class UuidConverter implements PropertyConverter<UUID,String>{

        @Override
        public UUID convertToEntityProperty(String databaseValue) {
            return UUID.fromString(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(UUID entityProperty) {
            return entityProperty.toString();
        }
    }

}
