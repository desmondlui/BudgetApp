package com.example.des45.budgetapp;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.lang.String;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Record implements Parcelable {

    private int id; //record the ID
    private boolean recordType;
    private String type;
    private String category;
    private double amount;
    private String contents;
    private boolean draft;//check is user use the quick function
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;

    public int getId() {
        return id;
    }

    public boolean getRecordType() {
        return recordType;
    }
    public String getRecordTypeString()
    {
        if(recordType)
        {
            return "Expenses";
        }
        else
            return "Income";
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getContents() {
        return contents;
    }

    public boolean getDraft() {
        return draft;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecordType(boolean recordType) {
        this.recordType = recordType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setdraft(boolean draft) {
        this.draft = draft;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Record(int id, boolean recordType, String type, String category, double amount, String contents, boolean draft, int year, int month, int day, int hours, int minutes) {
        this.id = id;
        this.recordType = recordType;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.contents = contents;
        this.draft = draft;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
    }

    public Record(int id)
    {
        this.id = id;
    }
    public Record()
    {

    }
    public Record(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        public Record[] newArray(int size) {

            return new Record[size];
        }

    };

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.recordType = (Boolean) in.readValue(null);
        this.type = in.readString();
        this.category = in.readString();
        this.amount = in.readDouble();
        this.contents = in.readString();
        this.draft = (Boolean) in.readValue(null);
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.hours = in.readInt();
        this.minutes = in.readInt();

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(recordType);
        dest.writeString(type);
        dest.writeString(category);
        dest.writeDouble(amount);
        dest.writeString(contents);
        dest.writeValue(draft);
        dest.writeInt(year);
        dest.writeInt(day);
        dest.writeInt(hours);
        dest.writeInt(minutes);
    }
}

