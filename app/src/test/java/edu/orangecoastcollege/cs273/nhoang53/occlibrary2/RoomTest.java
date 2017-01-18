package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit Test for Room
 */
public class RoomTest {
    private Room mRoom;

    @Before
    public void setUp() throws Exception {
        mRoom = new Room(null, null, 0);
        mRoom.setName("A111");
        mRoom.setDescription("Big room");
        mRoom.setCapacity(2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getStartTime() throws Exception {
        Assert.assertEquals("7:00:00", mRoom.getName());
    }



    @Test
    public void getEndTime() throws Exception {
        Assert.assertEquals("8:00:00", mRoom.getDescription());
    }


    @Test
    public void getStatus() throws Exception {
        Assert.assertEquals(true, mRoom.getCapacity());
    }
}