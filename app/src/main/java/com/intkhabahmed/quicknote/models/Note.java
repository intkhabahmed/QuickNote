package com.intkhabahmed.quicknote.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.intkhabahmed.quicknote.utils.HashTagTypeConverter;

import java.util.List;

@Entity(tableName = "notes")
@TypeConverters(HashTagTypeConverter.class)
public class Note implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String title;
    private String description;
    @ColumnInfo(name = "date_created")
    private long dateCreated;
    @ColumnInfo(name = "hash_tag")
    private List<String> hashTags;

    @Ignore
    public Note() {
    }

    public Note(int _id, String title, String description, long dateCreated, List<String> hashTags) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.hashTags = hashTags;
    }


    protected Note(Parcel in) {
        _id = in.readInt();
        title = in.readString();
        description = in.readString();
        dateCreated = in.readLong();
        hashTags = in.createStringArrayList();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(_id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(dateCreated);
        dest.writeStringList(hashTags);
    }
}
