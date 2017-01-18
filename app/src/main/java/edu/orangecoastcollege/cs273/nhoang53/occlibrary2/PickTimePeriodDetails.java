package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** pick a detail starting time and number of hours uses
 *  and submit the room booking, then go to booking details
 *  to review all booking information
 *
 */
public class PickTimePeriodDetails extends AppCompatActivity {

    private final double MINIMUM_HOURS = 0.5;
    private TextView selectedRoomTextView;
    private TextView selectedDateTextView;
    private TextView seekBarTextView;
    private Spinner pickTimeSpinner;
    private SeekBar hoursSeekBar;

    private String date;
    private int room;
    private DBHelper db;
    private SharedPreferences prefs; // store studentID from login activity

    /** Set up all elements for activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time_period_details);

        db = new DBHelper(this);

        selectedDateTextView = (TextView) findViewById(R.id.selectedDateTextView);
        selectedRoomTextView = (TextView) findViewById(R.id.selectedRoomTextView);
        seekBarTextView = (TextView) findViewById(R.id.seekBarTextView);
        hoursSeekBar = (SeekBar) findViewById(R.id.hoursSeekBar);

        Intent intentFromPickBookingTimeActivity = getIntent();
        room = intentFromPickBookingTimeActivity.getIntExtra("Room", 0);
        date = intentFromPickBookingTimeActivity.getStringExtra("Date");
        TimePeriod timePeriod = intentFromPickBookingTimeActivity.getExtras().getParcelable("SelectedPeriod");

        // Update TextView
        selectedRoomTextView.setText(getString(R.string.selected_room_text_view, db.getRoom(room).getName()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date ddate = sdf.parse(date);
            selectedDateTextView.setText(getString(R.string.selected_date_text_view, sdf.format(ddate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Spinner
        pickTimeSpinner = (Spinner) findViewById(R.id.pickTimeSpinner);
        addItemsOnSpinner(pickTimeSpinner, timePeriod);

        // SeekView
        hoursSeekBar.setOnSeekBarChangeListener(hoursChangeListener);
        hoursSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    }

    /** add start time options into spinner
     * @param spinner
     * @param timePeriod
     */
    private void addItemsOnSpinner(Spinner spinner, TimePeriod timePeriod) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        double duration = MINIMUM_HOURS;
        List<String> list = new ArrayList<>();

        Time tStartTime = Time.valueOf(timePeriod.getStartTime());
        Time tEndTime = Time.valueOf(timePeriod.getEndTime());

        // add new start time after every 30' until 30' before ending time of period
        while (tStartTime.getTime() < (tEndTime.getTime() ))
        {
            list.add(sdf.format(tStartTime));
            tStartTime = new Time(tStartTime.getTime() + (long)(duration*60*60*1000));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * seek bar listener
     */
    private SeekBar.OnSeekBarChangeListener hoursChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int stepSize = 30;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int totalMinutes = progress + stepSize;
            int hour = totalMinutes / 60;
            int minute = totalMinutes % 60;

            // update seek bar text view
            seekBarTextView.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));

            progress = ((int)Math.round(progress/stepSize))*stepSize;
            seekBar.setProgress(progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /** add booking to database and go to roomBookingDetailsActivity to show booking information
     * @param view
     * @throws ParseException
     */
    public void showRoomBookingDetails(View view) throws ParseException {
        // Get login studentID
        prefs = getSharedPreferences(Main2Activity.STUDENT_PREFS, 0);
        int studentId = prefs.getInt("studentId", 0);

        float hoursUsed = (float) (hoursSeekBar.getProgress() + 30) / 60;
        String startTime = (String) pickTimeSpinner.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        Date tTime = sdf.parse(startTime);
        sdf = new SimpleDateFormat("HH:mm:ss");
        String sTime = sdf.format(tTime);
        RoomBooking roomBooking = new RoomBooking(room, studentId, date, sTime, hoursUsed);

        // Store to database
        db = new DBHelper(this);
        db.addRoomBooking(roomBooking);

        Intent roomBookingDetailsIntent = new Intent(this, RoomBookingDetailsActivity.class);

        roomBookingDetailsIntent.putExtra("RoomBooking", roomBooking);

        startActivity(roomBookingDetailsIntent);
    }
}
