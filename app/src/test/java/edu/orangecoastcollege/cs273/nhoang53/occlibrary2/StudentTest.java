package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Joseph on 12/12/2016.
 */

public class StudentTest {
    private Student student;

    @Before
    public void setUp() throws Exception{
        student = new Student();
    }

    @After
    public void tearDown() throws Exception{

    }

    @Test
    public void getId() throws Exception{
        student.setId(1234567);
        assertEquals(1234567, student.getId());
    }

    @Test
    public void getLastName() throws Exception{
        student.setLastName("Test last name");
        assertEquals("Test last name", student.getLastName());
    }

    @Test
    public void getFirstName() throws Exception{
        student.setFirstName("Test first name");
        assertEquals("Test first name", student.getFirstName());
    }

    @Test
    public void getPassword() throws Exception{
        student.setPassword("Test password");
        assertEquals("Test password", student.getPassword());
    }
}
