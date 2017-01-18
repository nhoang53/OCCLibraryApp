package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

/** Store room details
 * Created by Long Truong on 11/27/2016.
 */
public class Room {
    private int mId;
    private String mName;
    private String mDescription;
    private int mCapacity;

    /** Constructor
     * @param mId
     * @param mName
     * @param mDescription
     * @param mCapacity
     */
    public Room(int mId, String mName, String mDescription, int mCapacity) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mCapacity = mCapacity;
    }

    public Room(){}

    /** Constructor
     * @param mName
     * @param mDescription
     * @param mCapacity
     */
    public Room(String mName, String mDescription, int mCapacity) {
        this(-1, mName, mDescription, mCapacity);
    }

    /** get ID
     * @return
     */
    public int getId() {
        return mId;
    }

    /** get Name
     * @return
     */
    public String getName() {
        return mName;
    }

    /** set Name
     * @param mName
     */
    public void setName(String mName) {
        this.mName = mName;
    }

    /** get description
     * @return
     */
    public String getDescription() {
        return mDescription;
    }

    /** set description
     * @param mDescription
     */
    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    /** get capacity
     * @return
     */
    public int getCapacity() {
        return mCapacity;
    }

    /** set capacity
     * @param mCapacity
     */
    public void setCapacity(int mCapacity) {
        this.mCapacity = mCapacity;
    }
}
