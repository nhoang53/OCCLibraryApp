package edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.DBHelper;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.PickRoomActivity;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.R;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.Room;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.RoomBooking;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.ShakeDetector;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveRoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveRoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /////////
    private static final int FULL_OR_WEEKEND_DAYS = 0; // no time period available to book
    private static final int AVAILABLE = 1; // still available in some periods.
    private static final int EMPTY = 2; // no booking yet on this day
    private static final int LIBRARY_TOTAL_HOURS_OPEN = 13; // Is used to check room/ date is full booked
    private static final int IS_1_TO_10 = 0;
    private static final int IS_11_TO_20 = 1;
    private static final int IS_21_TO_30 = 2;
    private static final int TOTAL_AVAILABLE_DAYS = 30;

    private DBHelper db;
    private List<Room> allRoomsList;
    private List<RoomBooking> allRoomBookingsList;
    private int datesRange; // show 10 of 30 days on view: 0: 1-10, 1: 11-20, 2:21-30

    private GridLayout datesLayout;
    private ImageButton nextButton;
    private ImageButton previousButton;

    private ArrayList<Calendar> calendarsList;
    private TextView datesRangeTextView;

    // Reference to the SensorManager
    private SensorManager sensorManager;

    //Reference to the accelerometer
    private Sensor accelerometer;

    private ShakeDetector shakeDetector;

    public ReserveRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveRoomFragment newInstance(String param1, String param2) {
        ReserveRoomFragment fragment = new ReserveRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_reserve_room, container, false);

        View view = inflater.inflate(R.layout.fragment_reserve_room, container, false);
        db = new DBHelper(getContext());

        allRoomsList = db.getAllRooms();
        allRoomBookingsList = db.getAllRoomBookings();

        datesRange = IS_1_TO_10; // start showing first 10 days

        datesLayout = (GridLayout) view.findViewById(R.id.datesGridLayout);
        nextButton = (ImageButton) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNext10Dates(v);
            }
        });
        previousButton = (ImageButton) view.findViewById(R.id.previousButton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPrevious10Dates(v);
            }
        });
        datesRangeTextView = (TextView) view.findViewById(R.id.dateRangeTextView);

        // disable previous button
        previousButton.setVisibility(View.INVISIBLE);

        // get 30 days from today
        calendarsList = new ArrayList<>();
        for(int i = 0; i < TOTAL_AVAILABLE_DAYS; i++)
        {
            Calendar nCalendar = Calendar.getInstance();
            nCalendar.add(Calendar.DATE, i);
            calendarsList.add(nCalendar);
        }

        datesRangeTextView.setText(getString(R.string.dates_range, getDateStringDisplay(calendarsList.get(0)), getDateStringDisplay(calendarsList.get(calendarsList.size() - 1))));

        //
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                datesRange++;
                if(datesRange > IS_21_TO_30)
                {
                    datesRange = IS_1_TO_10;
                }
                // update views
                showDateOnTable( datesRange, calendarsList);
            }
        });

        // update views
        showDateOnTable( datesRange, calendarsList);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Start the
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }

    /** Get a string of date to display in room layout from input calendar object
     * @param calendar store a date needed to display
     * @return string of the date
     */
    private String getDateStringDisplay(Calendar calendar)
    {
        int curMonth = calendar.get(Calendar.MONTH) + 1; // need +1
        int curDate = calendar.get(Calendar.DATE);
        int curYear = calendar.get(Calendar.YEAR);
        String date = String.valueOf(curMonth) + "/"
                + String.valueOf(curDate) + "/"
                +String.valueOf(curYear);
        return date;
    }

    /** update view of pick date layout
     * @param range decide which days will be showed on screen
     * @param calendarsList list of 30 available days
     */
    private void showDateOnTable(int range, ArrayList<Calendar> calendarsList)
    {

        // loop: check each day available status & load data to views
        int llChildCount = datesLayout.getChildCount();
        for(int i = 0; i < llChildCount; i++)
        {
            View childView = datesLayout.getChildAt(i);
            if (childView instanceof LinearLayout) // check if that cell is a LinearLayout
            {
                LinearLayout childLinearLayout = (LinearLayout) childView;

                // set onClick listener
                childLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewRoomOfSelectedDate(view);
                    }
                });

                View grandchildView1 = childLinearLayout.getChildAt(0);
                View grandchildView2 = childLinearLayout.getChildAt(1);
                if (grandchildView1 instanceof TextView && grandchildView2 instanceof TextView) // check if that cell is a TextView
                {
                    TextView childDayOfWeekTextView = (TextView) grandchildView1;
                    TextView childDateTextView = (TextView) grandchildView2;
                    Calendar mCalendar = calendarsList.get(range * 10 + i);

                    int currentDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK); // get day of week

                    // get a string of date
                    int curMonth = mCalendar.get(Calendar.MONTH) + 1; // need +1
                    int curDate = mCalendar.get(Calendar.DATE);
                    int curYear = mCalendar.get(Calendar.YEAR);
                    String date = getString(R.string.get_date_from_calendar, String.valueOf(curMonth)
                            ,String.valueOf(curDate)
                            ,String.valueOf(curYear));


                    String currentDayString = ""; // store day of week
                    switch (currentDayOfWeek) {
                        case Calendar.SUNDAY:
                            // Current day is Sunday = 1
                            currentDayString = "SUN";
                            break;
                        case Calendar.MONDAY:
                            // Current day is Monday
                            currentDayString = "MON";
                            break;
                        case Calendar.TUESDAY:
                            // etc.
                            currentDayString = "TUE";
                            break;
                        case Calendar.WEDNESDAY:
                            // etc.
                            currentDayString = "WED";
                            break;
                        case Calendar.THURSDAY:
                            // etc.
                            currentDayString = "THUR";
                            break;
                        case Calendar.FRIDAY:
                            // etc.
                            currentDayString = "FRI";
                            break;
                        case Calendar.SATURDAY:
                            // = 7
                            currentDayString = "SAT";
                            break;
                        default:

                    }


                    childDayOfWeekTextView.setText(currentDayString);
                    childDateTextView.setText(curMonth + "/" + curDate);

                    if((currentDayString == "SUN"))
                        updateBackGroundColor(childLinearLayout, FULL_OR_WEEKEND_DAYS);
                    else
                        updateBackGroundColor(childLinearLayout, getDateStatus(date));

                    childLinearLayout.setTag(date);
                }

            }
        }
    }


    /** get available status of a date
     * @param date
     * @return
     */
    private int getDateStatus(String date)
    {
        ArrayList<RoomBooking> newList =  new ArrayList<>();

        // filter room bookings of input date
        for(RoomBooking roomBooking : allRoomBookingsList)
        {
            if(roomBooking.getDate().equals(date))
            {
                newList.add(roomBooking);
            }
        }
        if(!newList.isEmpty())
        {
            int roomAvailable = allRoomsList.size();
            for (Room room : allRoomsList)
            {
                int totalHoursUsed = 0;
                for (RoomBooking booking : newList)
                {
                    if(room.getId() == booking.getRoomId())
                    {
                        totalHoursUsed += booking.getHoursUsed();
                    }
                }

                if(totalHoursUsed == LIBRARY_TOTAL_HOURS_OPEN)
                {
                    roomAvailable--;
                }
            }

            if(roomAvailable != 0)
            {
                return AVAILABLE;
            }
            else
            {
                return FULL_OR_WEEKEND_DAYS;
            }
        }
        else
            return EMPTY;
    }

    /** update background color
     * @param linearLayout
     * @param status
     */
    public void updateBackGroundColor(LinearLayout linearLayout, int status)
    {
        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.empty_background);
        switch (status)
        {
            case EMPTY:

                break;
            case AVAILABLE:
                background = ContextCompat.getDrawable(getContext(), R.drawable.available_background);
                break;
            case FULL_OR_WEEKEND_DAYS:

                // don't allow to pick date that full booked or weekend days
                linearLayout.setClickable(false);

                background = ContextCompat.getDrawable(getContext(), R.drawable.full_background);
                break;
            default:
        }
        linearLayout.setBackground(background);
    }

    /** onClick method of nextButton: load next 10 days of 30 days
     * @param view
     */
    public void loadNext10Dates(View view)
    {
        if(view instanceof ImageButton)
        {
            datesRange++;
            showDateOnTable(datesRange, calendarsList);
            if(datesRange == IS_11_TO_20)
            {
                previousButton.setVisibility(View.VISIBLE);
            }

            if(datesRange == IS_21_TO_30)
            {
                nextButton.setVisibility(View.INVISIBLE);
            }
        }


    }

    /** onClick method of previousButton: load previous 10 days of 30 days
     * @param view
     */
    public void loadPrevious10Dates(View view)
    {
        if(view instanceof ImageButton)
        {
            datesRange--;
            showDateOnTable(datesRange, calendarsList);
            if(datesRange == IS_11_TO_20)
            {
                nextButton.setVisibility(View.VISIBLE);
            }

            if(datesRange == IS_1_TO_10)
            {
                previousButton.setVisibility(View.INVISIBLE);
            }
        }


    }

    /** onClick button for dateLayout
     * @param view
     */
    public void viewRoomOfSelectedDate(View view)
    {
        if(view instanceof LinearLayout)
        {
            LinearLayout selectedLinearLayout = (LinearLayout) view;
            String selectedDate = (String) selectedLinearLayout.getTag();

            Intent pickRoomIntent = new Intent(getContext(), PickRoomActivity.class);

            pickRoomIntent.putExtra("Date", selectedDate);
            startActivity(pickRoomIntent);
        }
    }
}
