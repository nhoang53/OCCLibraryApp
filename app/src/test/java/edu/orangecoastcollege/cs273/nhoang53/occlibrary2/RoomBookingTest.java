package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit Test for Room Booking
 */
public class RoomBookingTest {
    private RoomBooking mRoomBooking;

    @Before
    public void setUp() throws Exception {
        mRoomBooking = new RoomBooking(0, 0, null, null, 1);
        mRoomBooking.setRoomId(111);
        mRoomBooking.setStudentId(222);
        mRoomBooking.setDate("12/11/2016");
        mRoomBooking.setStartTime("8:00:00");
        mRoomBooking.setHoursUsed(2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getRoomId() throws Exception {
        Assert.assertEquals(111, mRoomBooking.getRoomId());
    }



    @Test
    public void getStudentId() throws Exception {
        Assert.assertEquals(222, mRoomBooking.getStudentId());
    }


    @Test
    public void getDate() throws Exception {
        Assert.assertEquals("12/11/2016", mRoomBooking.getDate());
    }

    @Test
    public void getStartTime() throws Exception {
        Assert.assertEquals("8:00:00", mRoomBooking.getStartTime());
    }

    @Test
    public void getHourUsed() throws Exception {
        Assert.assertEquals(2, mRoomBooking.getHoursUsed());
    }
}