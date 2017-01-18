package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.os.Parcel;
import android.os.Parcelable;

/** Store information for each period of time
 * Created by Long Truong on 11/29/2016.
 */
public class TimePeriod implements Parcelable{
    private String startTime;
    private String endTime;
    private boolean status;

    /** Constructor
     * @param startTime
     * @param endTime
     * @param status
     */
    public TimePeriod(String startTime, String endTime, boolean status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    /** Parcel Constructor
     * @param in
     */
    protected TimePeriod(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        status = in.readByte() != 0;
    }

    public static final Creator<TimePeriod> CREATOR = new Creator<TimePeriod>() {
        @Override
        public TimePeriod createFromParcel(Parcel in) {
            return new TimePeriod(in);
        }

        @Override
        public TimePeriod[] newArray(int size) {
            return new TimePeriod[size];
        }
    };

    /** get start time
     * @return
     */
    public String getStartTime() {
        return startTime;
    }

    /** set start time
     * @param startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /** get end time
     * @return
     */
    public String getEndTime() {
        return endTime;
    }

    /** set end time
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /** get status
     * @return
     */
    public boolean getStatus() {
        return status;
    }

    /** set status
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    // for Parcel intent
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeByte((byte) (status ? 1 : 0));
    }
}
