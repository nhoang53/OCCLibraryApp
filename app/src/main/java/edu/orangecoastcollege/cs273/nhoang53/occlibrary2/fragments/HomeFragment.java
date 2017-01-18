package edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.DBHelper;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.LoginActivity;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.Main2Activity;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.R;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.RoomBooking;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.Student;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ////////////
    private SharedPreferences studentSharedPrefs;
    private TextView mNotLoginTextView;
    private ImageView bannerImageView;
    private LinearLayout activityMainLinearLayout;
    int bannerChange;
    int studentId;
    private DBHelper db;
    private Student student;
    private Handler mHandler;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        activityMainLinearLayout = (LinearLayout) view.findViewById(R.id.activity_main);
        //
        recursiveLoopChildren(activityMainLinearLayout);

        mHandler = new Handler();


        mNotLoginTextView = (TextView) view.findViewById(R.id.notLoginTextView);
        //mNotLoginTextView.setOnClickListener(this);

        bannerImageView = (ImageView) view.findViewById(R.id.bannerImageView);
        //bannerImageView.setOnClickListener(this);
        bannerChange = 1;



        // Restore preferences
        studentSharedPrefs = getContext().getSharedPreferences(Main2Activity.STUDENT_PREFS, 0);
        studentId = studentSharedPrefs.getInt("studentId", 0); // if not, id = 0
        if(studentId == 0)
        {
            mNotLoginTextView.setText(R.string.not_login);
            mNotLoginTextView.setEnabled(true);
        }
        else
        {
            db = new DBHelper(getContext());
            student = db.getStudent(studentId);
            mNotLoginTextView.setText("Have a nice day "
                    + student.getFirstName() + " "
                    + student.getLastName());
            mNotLoginTextView.setTextSize(18);
        }

        return view;
    }

    public void recursiveLoopChildren(LinearLayout parent)
    {
        for (int i = 0; i < parent.getChildCount(); i++)
        {
            final View child = parent.getChildAt(i);
            if (child instanceof LinearLayout)
            {
                recursiveLoopChildren((LinearLayout) child);
            }
            else if(child instanceof TextView) {
                ((TextView) child).setOnClickListener(this);
            }
            else if(child instanceof ImageView)
                ((ImageView) child).setOnClickListener(this);
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.notLoginTextView:
                login(v);
                break;

            case R.id.studentProfileImageView:
            case R.id.studentProfileTextView:
                studentProfile(v);
                break;

            case R.id.reserveRoomImageView:
            case R.id.reserveRoomTextView:
                reserveRoom(v);
                break;

            case R.id.bannerImageView:
                toggleShakeAnim(v);
                break;

            case R.id.contactImageView:
            case R.id.contactTextView:
                contactUs(v);
                break;

            case R.id.borrowBookImageView:
            case R.id.borrowBookTextView:
                borrowBook(v);
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
     * call the login activity
     * @param view
     */
    public void login(View view)
    {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * It will call the login activity if student haven't login yet.
     * Otherwise, it will call student profile activity
     * @param view
     */
    public void studentProfile(View view)
    {
        if(studentId == 0)
        {
            login(view);
        }
        else
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new StudentProfileFragment(), "Student Profile").commit();
        }
    }

    /**
     * Start ContactActivity by use of an Intent (no data passed)
     * @param view
     */
    public void contactUs(View view)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, new ContactUsFragment(), "Contact Us").commit();
    }

    /**
     * Pop-up Alert dialog because this function haven't set up yet.
     * @param view
     */
    public void borrowBook(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This function coming soon");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Change image of the banner, then shake the image left and right.
     * @param view
     */
    public void toggleShakeAnim(View view)
    {
        AssetManager am = getActivity().getAssets();
        bannerChange %= 3;
        if(bannerChange == 1)
        {
            try {
                InputStream stream = am.open("occ_library_banner_2.jpg");
                Drawable drawable = Drawable.createFromStream(stream, "occ_library_banner_2.jpg");
                bannerImageView.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(bannerChange == 2)
        {
            try {
                InputStream stream = am.open("occ_library_banner_3.jpg");
                Drawable drawable = Drawable.createFromStream(stream, "occ_library_banner_3.jpg");
                bannerImageView.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                InputStream stream = am.open("occ_library_banner.png");
                Drawable drawable = Drawable.createFromStream(stream, "occ_library_banner.png");
                bannerImageView.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bannerChange++;

        Animation shake = AnimationUtils.loadAnimation(getActivity(),R.anim.shake_anim);
        bannerImageView.startAnimation(shake);
    }

    /**
     /** onClick method for Reserve Room button
     * also check if user is not login yet or already have a upcoming room booking.
     * Long Truong
     */
    public void reserveRoom(View view)
    {
        if(studentId == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.not_login_click_button_error));
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }
        else {
            List<RoomBooking> allRoomBookings= db.getAllRoomBookings();

            // Check if student already had a upcoming room booking
            for(RoomBooking roomBooking : allRoomBookings)
            {
                Date date = Calendar.getInstance().getTime();
                if((date.compareTo(new Date(roomBooking.getDate())) < 0) && roomBooking.getStudentId() == studentId)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.already_reserve_room));
                    builder.setCancelable(false);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                    return;
                }

            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new ReserveRoomFragment(), "Reserve Room").commit();
        }
    }

}
