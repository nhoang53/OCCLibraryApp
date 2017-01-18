package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test for Time period
 */
public class TimePeriodTest {
    private TimePeriod mTimePeriod;

    @Before
    public void setUp() throws Exception {
        mTimePeriod = new TimePeriod(null, null, false);
        mTimePeriod.setStartTime("7:00:00");
        mTimePeriod.setEndTime("8:00:00");
        mTimePeriod.setStatus(true);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getStartTime() throws Exception {
        Assert.assertEquals("7:00:00", mTimePeriod.getStartTime());
    }



    @Test
    public void getEndTime() throws Exception {
        Assert.assertEquals("8:00:00", mTimePeriod.getEndTime());
    }


    @Test
    public void getStatus() throws Exception {
        Assert.assertEquals(true, mTimePeriod.getStatus());
    }

}