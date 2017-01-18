package edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.DBHelper;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.LoginActivity;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.Main2Activity;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.R;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.Room;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.RoomBooking;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.Student;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentProfileFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //
    private LinearLayout activity_student_profile;
    private TextView idTextView;
    private TextView lastNameTextView;
    private TextView firstNameTextView;
    private TextView roomReservingTextView;
    private TextView bookBorrowTextView;
    private TextView alertTextView;
    private TextView oldPasswordWrongTextView;
    private TextView confirmPasswordWrongTextView;

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confrimPasswordEditText;
    private Button roomReservingCancelButton;
    private Button resetButton;
    private Button savePasswordButton;

    private SharedPreferences prefs;
    private edu.orangecoastcollege.cs273.nhoang53.occlibrary2.DBHelper db;
    private Student student;
    private Room room;
    private RoomBooking roomBooking;
    private String roomReserving;
    private int studentId;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentProfileFragment newInstance(String param1, String param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
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
        //return inflater.inflate(R.layout.fragment_student_profile2, container, false);

        View view = inflater.inflate(R.layout.fragment_student_profile2, container, false);

        room = new Room();
        roomBooking = null;

        activity_student_profile = (LinearLayout) view.findViewById(R.id.activity_student_profile);
        activity_student_profile.requestFocus();

        idTextView = (TextView) view.findViewById(R.id.idTextView);
        lastNameTextView = (TextView) view.findViewById(R.id.lastNameTextView);
        firstNameTextView = (TextView) view.findViewById(R.id.firstNameTextView);
        roomReservingTextView = (TextView) view.findViewById(R.id.roomReservingTextView);
        bookBorrowTextView = (TextView) view.findViewById(R.id.bookBorrowTextView);
        alertTextView = (TextView) view.findViewById(R.id.alertTextView);
        oldPasswordWrongTextView = (TextView) view.findViewById(R.id.oldPasswordWrongTextView);
        confirmPasswordWrongTextView = (TextView) view.findViewById(R.id.confirmPasswordWrongTextView);

        oldPasswordEditText = (EditText) view.findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText) view.findViewById(R.id.newPasswordEditText);
        confrimPasswordEditText = (EditText) view.findViewById(R.id.confirmPasswordEditText);

        roomReservingCancelButton = (Button) view.findViewById(R.id.roomReservingCancelButton);
        roomReservingCancelButton.setOnClickListener(this);

        resetButton = (Button) view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);

        savePasswordButton = (Button) view.findViewById(R.id.savePasswordButton);
        savePasswordButton.setOnClickListener(this);

        db = new DBHelper(getContext());
        // get student information
        prefs = this.getActivity().getSharedPreferences(Main2Activity.STUDENT_PREFS, 0); // getSharedPreferences work on Context, not work on Fragment

        prefs = getActivity().getSharedPreferences(Main2Activity.STUDENT_PREFS, 0);
        studentId = prefs.getInt("studentId", 0);

        if(studentId != 0)
        {
            student = db.getStudent(studentId);
            idTextView.setText(String.valueOf(student.getId()));
            lastNameTextView.setText(student.getLastName());
            firstNameTextView.setText(student.getFirstName());

            // get room name
            List<RoomBooking> listRoomBookings = db.getAllRoomBookings();

            Date date= Calendar.getInstance().getTime();

            for(RoomBooking booking : listRoomBookings)
            {
                if(booking.getStudentId() == studentId && (date.compareTo(new Date(booking.getDate())) < 0))
                {
                    roomBooking = booking;
                }
            }
            //roomBooking = db.getRoomBooking(studentId);
            if(roomBooking != null)
            {
                room = db.getRoom(roomBooking.getRoomId());
                if (room != null) {
                    roomReservingTextView.setText(room.getName());
                    roomReservingTextView.setTag(roomBooking);
                }
                else
                    roomReservingTextView.setText("0");
            }
            else {
                roomReservingTextView.setText("0");
                roomReservingCancelButton.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roomReservingCancelButton:
                roomReservingCancel(v);
                break;
            case R.id.resetButton:
                reset(v);
                break;
            case R.id.savePasswordButton:
                changePassword(v);
                break;
        }
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

    /**
     * It will let student cancel room that he/she reserved with Dialog confirm.
     * @param view
     */
    public void roomReservingCancel(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to cancel?");
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Toast.makeText(getActivity(), "Deleted room " + roomReservingTextView.getText(), Toast.LENGTH_LONG).show();

                RoomBooking roomBooking = (RoomBooking) roomReservingTextView.getTag();
                db.deleteRoomBooking(roomBooking.getId());

                // reload fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new StudentProfileFragment(), "Student Profile").commit();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * clear all field at change password section
     * @param view
     */
    public void reset (View view)
    {
        oldPasswordEditText.setText("");
        newPasswordEditText.setText("");
        confrimPasswordEditText.setText("");
    }

    /**
     * Let student change their default password
     * @param view
     */
    public void changePassword(View view)
    {
        if(student.getPassword().equals(oldPasswordEditText.getText().toString()))
        {
            if(newPasswordEditText.getText().toString().equals(confrimPasswordEditText.getText().toString()))
            {
                db.changePassword(Integer.parseInt(idTextView.getText().toString()),
                        newPasswordEditText.getText().toString());

                // reload fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new StudentProfileFragment(), "Student Profile").commit();

                Toast.makeText(getActivity(), "Password changed", Toast.LENGTH_LONG).show();
            }
            else
            {
                alertTextView.setText(R.string.password_not_match);
                confirmPasswordWrongTextView.setText("*");

                Toast.makeText(getActivity(), "New password do not match", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            alertTextView.setText(R.string.password_wrong);
            oldPasswordWrongTextView.setText("*");

            Toast.makeText(getActivity(), "Old password do not match", Toast.LENGTH_SHORT).show();
        }
    }

}
