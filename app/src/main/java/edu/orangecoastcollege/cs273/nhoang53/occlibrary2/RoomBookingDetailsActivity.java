package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Show room booking details
 */
public class RoomBookingDetailsActivity extends AppCompatActivity {
    DBHelper db;
    private RoomBooking roomBooking;
    private TextView detailsTextView;
    private List<Student> allStudentsList;
    private List<Room> allRoomsList;
    private LinearLayout mainLayout;
    String studentName;
    String roomName;

    /** Set up all elements for activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking_details);

        // Animation
        mainLayout = (LinearLayout) findViewById(R.id.activity_room_booking_details);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartOffset(2000);
        alphaAnimation.setFillAfter(true);
        mainLayout.startAnimation(alphaAnimation);

        db = new DBHelper(this);
        allStudentsList = db.getAllStudents();
        allRoomsList = db.getAllRooms();

        detailsTextView = (TextView) findViewById(R.id.detailsTextView);

        Intent intentFromPickTimePeriodDetails = getIntent();

        roomBooking = intentFromPickTimePeriodDetails.getExtras().getParcelable("RoomBooking");

        for(Student student : allStudentsList)
        {
            if(student.getId() == roomBooking.getStudentId())
            {
                studentName = student.getFirstName() + " " + student.getLastName();
                break;
            }
        }
        for(Room room : allRoomsList)
        {
            if (room.getId() == roomBooking.getRoomId())
            {
                roomName = room.getName();
                break;
            }
        }
        //float totalHours = roomBooking.getHoursUsed();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        Time startTime = Time.valueOf(roomBooking.getStartTime());

        Time endTime = new Time(startTime.getTime() + (long ) (roomBooking.getHoursUsed()* 60 * 60 * 1000 ));
        detailsTextView.setText(getString(R.string.details, studentName, roomBooking.getDate(), roomName, sdf.format(startTime) , sdf.format(endTime)));
    }

    public void returnToMainMenu(View view )
    {
        Intent returnToMainMenuIntent = new Intent(this, Main2Activity.class);
        startActivity(returnToMainMenuIntent);
        finish();
    }
}
