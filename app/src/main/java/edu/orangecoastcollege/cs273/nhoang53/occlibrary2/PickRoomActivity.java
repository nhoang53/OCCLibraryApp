package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * get selected date from pickDateActivity to show all rooms with available status on this date
 * @author Long Truong
 */
public class PickRoomActivity extends AppCompatActivity {

    private static final int FULL_OR_WEEKEND_DAYS = 0; // no time period available to book
    private static final int AVAILABLE = 1; // still available in some periods.
    private static final int EMPTY = 2; // no booking on this day
    private static final int LIBRARY_TOTAL_HOURS_OPEN = 13; // Is used to check room/ date is full booked
    private static final int IS_1_TO_10 = 0;
    private static final int NUMBER_OF_ROOMS_ON_TABLE = 10;
    private String date;


    private DBHelper db;
    private List<Room> allRoomsList;

    private List<RoomBooking> allRoomBookingsList;
    private int roomsRange;
    private GridLayout roomsLayout;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private TextView pickRoomTitleTextView;

    /** Set up all elements for activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_room);

        db = new DBHelper(this);
        allRoomsList = db.getAllRooms();
        allRoomBookingsList = db.getAllRoomBookings();

        roomsRange = IS_1_TO_10;
        roomsLayout = (GridLayout) findViewById(R.id.roomsGridLayout);
        nextButton = (ImageButton) findViewById(R.id.pickRoom_nextButton);
        previousButton = (ImageButton) findViewById(R.id.pickRoom_previousButton);
        pickRoomTitleTextView = (TextView) findViewById(R.id.pickRoomTitleTextView);

        // Set up previous and next button
        previousButton.setVisibility(View.INVISIBLE);

        if(allRoomsList.size() < NUMBER_OF_ROOMS_ON_TABLE)
        {
            nextButton.setVisibility(View.INVISIBLE);
        }

        Intent intentFromPickDateActivity = getIntent();

        date = intentFromPickDateActivity.getStringExtra("Date");
        pickRoomTitleTextView.setText(getString(R.string.pick_room_title,date));

        showRoomOnTable(roomsRange, date);
    }

    /** show rooms available status on date
     * @param range use if there is more than 10 rooms available
     * @param date selected date
     */
    private void showRoomOnTable(int range, String date)
    {
        int llChildCount = roomsLayout.getChildCount();
        for(int i = 0; i < llChildCount; i++)
        {
            View childView = roomsLayout.getChildAt(i);
            if (childView instanceof LinearLayout) // check if that cell is a LinearLayout
            {
                LinearLayout childLinearLayout = (LinearLayout) childView;
                childLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewTimePeriod(view);
                    }
                });
                childLinearLayout.setVisibility(View.INVISIBLE);

                View grandchildView1 = childLinearLayout.getChildAt(0);
                View grandchildView2 = childLinearLayout.getChildAt(1);
                View grandchildView3 = childLinearLayout.getChildAt(2);

                if (grandchildView1 instanceof TextView && grandchildView2 instanceof TextView && grandchildView3 instanceof TextView) // check if that cell is a TextView
                {
                    if(allRoomsList.size() > range*NUMBER_OF_ROOMS_ON_TABLE + i)
                    {
                        childLinearLayout.setVisibility(View.VISIBLE);
                        TextView childRoomNameTextView = (TextView) grandchildView1;
                        TextView childRoomDetailsTextView = (TextView) grandchildView2;
                        TextView childRoomCapacityTextView = (TextView) grandchildView3;

                        Room room = allRoomsList.get(range*NUMBER_OF_ROOMS_ON_TABLE + i);

                        childRoomNameTextView.setText(room.getName());
                        childRoomDetailsTextView.setText(room.getDescription());
                        childRoomCapacityTextView.setText(getString(R.string.room_capacity, room.getCapacity()));

                        updateBackGroundColor(childLinearLayout, checkRoomStatus(room, date));
                        childLinearLayout.setTag(room);
                    }
                }
            }
        }
    }

    /** update background color for each room layout
     * @param linearLayout
     * @param status
     */
    public void updateBackGroundColor(LinearLayout linearLayout, int status)
    {
        Drawable background = ContextCompat.getDrawable(this, R.drawable.empty_background);
        switch (status)
        {
            case EMPTY:
                break;
            case AVAILABLE:
                background = ContextCompat.getDrawable(this, R.drawable.available_background);
                break;
            case FULL_OR_WEEKEND_DAYS:
                //disable full room
                linearLayout.setClickable(false);

                background = ContextCompat.getDrawable(this, R.drawable.full_background);
                break;
            default:
        }
        linearLayout.setBackground(background);
    }

    /** check room available status
     * @param room
     * @param date
     * @return
     */
    private int checkRoomStatus(Room room, String date)
    {
        float totalHoursUsed = 0;
        for(RoomBooking roomBooking : allRoomBookingsList)
        {
            if(roomBooking.getRoomId() == room.getId() && roomBooking.getDate().equals(date))
            {
                totalHoursUsed+= roomBooking.getHoursUsed();
            }
        }
        if(totalHoursUsed == LIBRARY_TOTAL_HOURS_OPEN)
            return FULL_OR_WEEKEND_DAYS;
        else if(totalHoursUsed == 0)
            return EMPTY;
        else
            return AVAILABLE;
    }

    /** onClick for nextButton
     * Load next 10 rooms
     * @param view
     */
    public void loadNext10Rooms(View view)
    {
        if(view instanceof ImageButton)
        {
            roomsRange++;
            showRoomOnTable(roomsRange, date);

            previousButton.setVisibility(View.VISIBLE);

            if(allRoomsList.size() <= roomsRange*NUMBER_OF_ROOMS_ON_TABLE)
            {
                nextButton.setVisibility(View.INVISIBLE);
            }
        }


    }

    /** Load previous 10 rooms
     * @param view
     */
    public void loadPrevious10Rooms(View view)
    {
        if(view instanceof ImageButton)
        {
            roomsRange--;
            showRoomOnTable(roomsRange, date);

            nextButton.setVisibility(View.VISIBLE);

            if(roomsRange == IS_1_TO_10)
            {
                //nextButton.setEnabled(false);
                previousButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    /** onClick() of room layout. Show the time period for the selected room and date.
     * @param view
     */
    public void viewTimePeriod(View view)
    {
        if(view instanceof LinearLayout)
        {
            LinearLayout selectedLinearLayout = (LinearLayout) view;
            Room selectedRoom = (Room) selectedLinearLayout.getTag();

            Intent pickTimeIntent = new Intent(this, PickBookingTimeActivity.class);

            pickTimeIntent.putExtra("Date", date);
            pickTimeIntent.putExtra("Room", selectedRoom.getId());
            startActivity(pickTimeIntent);
        }
    }
}
