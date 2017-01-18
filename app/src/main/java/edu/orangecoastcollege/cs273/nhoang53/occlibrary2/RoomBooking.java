package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/** Room Booking database
 * Store Room Booking information
 * Created by Long Truong on 11/27/2016.
 */

public class RoomBooking implements Parcelable{

    private int mId;
    private int mRoomId;
    private int mStudentId;
    private String mDate; // mmddyyyy
    private String mStartTime;
    private float mHoursUsed;

    public RoomBooking(){}

    /** Constructor
     * @param mId
     * @param mRoomId
     * @param mStudentId
     * @param mDate
     * @param mStartTime
     * @param mHoursUsed
     */
    public RoomBooking(int mId, int mRoomId, int mStudentId, String mDate, String mStartTime, float mHoursUsed) {
        this.mId = mId;
        this.mRoomId = mRoomId;
        this.mStudentId = mStudentId;
        this.mDate = mDate;
        this.mStartTime = mStartTime;
        this.mHoursUsed = mHoursUsed;
    }

    /** Constructor without primary key id
     * @param mRoomId
     * @param mStudentId
     * @param mDate
     * @param mStartTime
     * @param mHoursUsed
     */
    public RoomBooking(int mRoomId, int mStudentId, String mDate, String mStartTime, float mHoursUsed) {
        this.mRoomId = mRoomId;
        this.mStudentId = mStudentId;
        this.mDate = mDate;
        this.mStartTime = mStartTime;
        this.mHoursUsed = mHoursUsed;
    }

    /** Constructor for parcel intent
     * @param in
     */
    protected RoomBooking(Parcel in) {
        mId = in.readInt();
        mRoomId = in.readInt();
        mStudentId = in.readInt();
        mDate = in.readString();
        mStartTime = in.readString();
        mHoursUsed = in.readFloat();
    }

    public static final Creator<RoomBooking> CREATOR = new Creator<RoomBooking>() {
        @Override
        public RoomBooking createFromParcel(Parcel in) {
            return new RoomBooking(in);
        }

        @Override
        public RoomBooking[] newArray(int size) {
            return new RoomBooking[size];
        }
    };

    /**
     * @return
     */
    public int getId() {

        return mId;
    }

    /**
     * @return
     */
    public int getRoomId() {
        return mRoomId;
    }

    /**
     * @param mRoomId
     */
    public void setRoomId(int mRoomId) {
        this.mRoomId = mRoomId;
    }

    /**
     * @return
     */
    public int getStudentId() {
        return mStudentId;
    }

    /**
     * @param mStudentId
     */
    public void setStudentId(int mStudentId) {
        this.mStudentId = mStudentId;
    }

    /**
     * @return
     */
    public String getDate() {
        return mDate;
    }

    /**
     * @param mDate
     */
    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    /**
     * @return
     */
    public String getStartTime() {
        return mStartTime;
    }

    /**
     * @param mStartTime
     */
    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    /**
     * @return
     */
    public float getHoursUsed() {
        return mHoursUsed;
    }

    /**
     * @return
     */
    public void setHoursUsed(int mHoursUsed) {
        this.mHoursUsed = mHoursUsed;
    }

    /**
     * @return
     */
    public Date getTime()
    {
        Date date;
        String sDate = getDate();
        String sTime = getStartTime();
        int month = Integer.parseInt(sDate.substring(0,sDate.indexOf('/')));
        int day = Integer.parseInt(sDate.substring(sDate.indexOf('/')+ 1,sDate.lastIndexOf('/')));

        int year = Integer.parseInt(sDate.substring(sDate.lastIndexOf('/') + 1,sDate.length()));
        int hour = Integer.parseInt(sTime.substring(0,sTime.indexOf(':')));
        int minute = Integer.parseInt(sTime.substring(sTime.indexOf(':') + 1,sTime.lastIndexOf(':')));

        date = new Date(year, month, day, hour, minute);
        return date;
    }

    // Use for sending intent by Parcel
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeInt(mRoomId);
        parcel.writeInt(mStudentId);
        parcel.writeString(mDate);
        parcel.writeString(mStartTime);
        parcel.writeFloat(mHoursUsed);
    }
}
