package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Select an available period of selected room and date.
 *  Then go to pick a detail starting time and number of hours uses
 */
public class PickBookingTimeActivity extends AppCompatActivity {

    private static final String LIBRARY_OPENING_TIME = "7:00:00";
    private static final String LIBRARY_CLOSING_TIME = "20:00:00";
    private static final boolean BOOKED = true;
    private static final boolean NOT_BOOKED = false;

    private DBHelper db;
    private List<RoomBooking> allRoomBookingsList;
    private List<TimePeriod> allTimePeriodsList;
    private TimePeriodListAdapter periodListAdapter;
    private ListView periodListView;
    private TextView titleTextView;
    private TextView informTextView;
    private String date;
    private int room;

    /** Set up all elements for activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_booking_time);

        db = new DBHelper(this);
        allRoomBookingsList = db.getAllRoomBookings();

        Intent intentFromPickRoomActivity = getIntent();
        date = intentFromPickRoomActivity.getStringExtra("Date");
        room = intentFromPickRoomActivity.getIntExtra("Room", 0);

        allTimePeriodsList = getAllTimePeriod(date, room, allRoomBookingsList);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(getString(R.string.pick_period_title));

        int orientation = getResources().getConfiguration().orientation;

        // display the information along with the list view in landscape mode
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            informTextView = (TextView) findViewById(R.id.informTextView);
            informTextView.setText(getString(R.string.inform_text_view, date, room));
        }

        periodListView = (ListView) findViewById(R.id.periodListView);
        periodListAdapter = new TimePeriodListAdapter(this, R.layout.period_list_item, allTimePeriodsList);
        periodListView.setAdapter(periodListAdapter);
    }


    /** get all the list of periods and theirs status
     * @param date
     * @param room
     * @param allRoomBookingsList
     * @return
     */
    private ArrayList<TimePeriod> getAllTimePeriod(String date, int room, List<RoomBooking> allRoomBookingsList)
    {
        // filter the booking in specific date and room
        List<RoomBooking> filterRoomBookingsList = new ArrayList<>();
        for(RoomBooking roomBooking : allRoomBookingsList)
        {
            if(roomBooking.getDate().equals(date) && roomBooking.getRoomId() == room)
            {
                filterRoomBookingsList.add(roomBooking);
            }
        }

        // Sort roomBooking list by startTime
        Collections.sort(filterRoomBookingsList, new Comparator<RoomBooking>() {
            @Override
            public int compare(RoomBooking roomBooking, RoomBooking t1) {
                return roomBooking.getTime().compareTo(t1.getTime());
            }
        });

        // Make a list of time period.
        ArrayList<TimePeriod> timePeriods = new ArrayList<>();
        String startTime = LIBRARY_OPENING_TIME;
        String endTime;
        boolean isBooked = NOT_BOOKED;

        for(RoomBooking roomBooking : filterRoomBookingsList)
        {
            // Set end time for the gap
            endTime = roomBooking.getStartTime();

            // Create time period for the gap between 2 booking if exists
            if(!startTime.equals(endTime))
            {
                TimePeriod timePeriod = new TimePeriod(startTime, endTime, isBooked);
                timePeriods.add(timePeriod);
            }

            // Create time period for the booking
            if(isBooked == NOT_BOOKED) // double check
            {
                // Set start time
                startTime = endTime;

                // Set end time
                float duration = roomBooking.getHoursUsed();
                Time temp = Time.valueOf(endTime);
                endTime = new Time(temp.getTime() + (long)(duration*60*60*1000)).toString();
                if(endTime.charAt(0) == '0')
                    endTime = endTime.substring(1);
                // Set flag
                isBooked = BOOKED;

                // Add period
                TimePeriod timePeriod = new TimePeriod(startTime, endTime, isBooked);
                timePeriods.add(timePeriod);

                // Prepare for new room booking (reset)
                isBooked = NOT_BOOKED;
                startTime = endTime;
            }
        }

        // Check the gap between last booking and the time the library closes
        if(!startTime.equals(LIBRARY_CLOSING_TIME))
        {
            timePeriods.add(new TimePeriod(startTime, LIBRARY_CLOSING_TIME, NOT_BOOKED)) ;
        }
        return timePeriods;
    }

    /**  onClick() event for selected period item in list view
     * @param view
     */
    public void pickTimePeriodDetails(View view)
    {
        if(view instanceof LinearLayout) {
            LinearLayout selectedLinearLayout = (LinearLayout) view;
            TimePeriod selectedTimePeriod = (TimePeriod) selectedLinearLayout.getTag();

            Intent pickTimePeriodDetails = new Intent(this, PickTimePeriodDetails.class);

            pickTimePeriodDetails.putExtra("SelectedPeriod", selectedTimePeriod);
            pickTimePeriodDetails.putExtra("Room", room);
            pickTimePeriodDetails.putExtra("Date", date);

            startActivity(pickTimePeriodDetails);
        }
    }
}

