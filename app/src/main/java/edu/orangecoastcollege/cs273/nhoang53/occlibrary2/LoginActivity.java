package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordEditText;
    private EditText mStudentIdEditText;
    private Button mLogInButton;
    private Button mResetButton;
    private TextView mLogInStatusTextView;
    private TextView mStudentIdNullTextView;
    private TextView mPasswordNullTextView;

    private DBHelper db;
    private ArrayList<Student> allStudentList;

    //public static final String STUDENT_PREFS = "studentPrefs";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SharedPreferences splashPrefs;
    private SharedPreferences.Editor editorSplash;

    /**
     * Import data of student, room and rookBooking from CSV file
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // database operation
        //deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importStudentFromCSV("students.csv");
        allStudentList = db.getAllStudents();
        //Log.i("\nOCC Library.", allStudentList.toString());

        db.importRoomsFromCSV("rooms.csv");
        db.importRoomBookingsFromCSV("roomBookings.csv");

        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mStudentIdEditText = (EditText) findViewById(R.id.studentIdEditText);
        mLogInButton = (Button) findViewById(R.id.logInButton);
        mResetButton = (Button) findViewById(R.id.resetButton);
        mLogInStatusTextView = (TextView) findViewById(R.id.logInStatusTextView);
        mStudentIdNullTextView = (TextView) findViewById(R.id.studentIdNullTextView);
        mPasswordNullTextView = (TextView) findViewById(R.id.passwordNullTextView);

        mStudentIdNullTextView.setEnabled(false);
        mPasswordNullTextView.setEnabled(false);

        // prefs
        //prefs = getSharedPreferences(STUDENT_PREFS, 0);
        prefs = getSharedPreferences(Main2Activity.STUDENT_PREFS, 0);

        if(prefs.getInt("studentId", 0) != 0)
        {
            //go to main page
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Handle login action. It will check if user input correct username and password,
     * it will create SharedPreferences name "studentId", and then auto go to MainActivity
     * @param view
     */
    public void logIn(View view)
    {
        if(!mStudentIdEditText.getText().toString().equals("") && !mPasswordEditText.getText().toString().equals("")) {
            for (int i = 0; i < allStudentList.size(); i++) {
                if (allStudentList.get(i).getId() == Integer.parseInt(mStudentIdEditText.getText().toString())
                        && allStudentList.get(i).getPassword().equals(mPasswordEditText.getText().toString()))
                {
                    hideKeyboard(getApplicationContext(), view);
                    mLogInStatusTextView.setText(R.string.login_success);
                    mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginSuccess));
                    //store data in SharedPreference
                    editor = prefs.edit(); // edit
                    editor.putInt("studentId",allStudentList.get(i).getId());
                    editor.apply();

                    // prevent it go to welcome splash page
                    splashPrefs = getSharedPreferences(Main2Activity.SPLASH_PREF, 0);
                    editorSplash = splashPrefs.edit();
                    editorSplash.putInt("splash", 1); // stop welcome splash page
                    editorSplash.apply();

                    //go to main page
                    Intent intent = new Intent(this, Main2Activity.class);
                    this.finish();
                    startActivity(intent);

                    i = allStudentList.size(); // stop the loop
                }
                else {
                    mLogInStatusTextView.setText(R.string.login_fail);
                    mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginFail));
                    //mStudentIdNullTextView.requestFocus();
                    hideKeyboard(getApplicationContext(), view);
                }
            }
        }
        else {
            if (mStudentIdEditText.getText().toString().equals(""))
            {
                mStudentIdNullTextView.setText("*");
                mLogInStatusTextView.setText(R.string.student_id_null);
                mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginFail));
            }
            else if(mPasswordEditText.getText().toString().equals(""))
            {
                mPasswordNullTextView.setText("*");
                mLogInStatusTextView.setText(R.string.password_null);
                mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginFail));
            }
        }
    }

    /**
     * Clear all field input
     * @param view
     */
    public void reset(View view)
    {
        mStudentIdEditText.setText("");
        mPasswordEditText.setText("");
        mLogInStatusTextView.setText("");
    }

    /**
     * Hide the keyboard after user Click login button
     * @param context
     * @param view
     */
    public void hideKeyboard(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
