package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
//import android.app.FragmentManager;  // no good
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments.ContactUsFragment;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments.HomeFragment;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments.ReserveRoomFragment;
import edu.orangecoastcollege.cs273.nhoang53.occlibrary2.fragments.StudentProfileFragment;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String STUDENT_PREFS = "studentPrefs"; // save infor of student
    public static final String SPLASH_PREF = "splashPrefs"; // control ON - OFF splash screen
    private SharedPreferences studentSharedPrefs;
    private SharedPreferences splashSharedPrefs;
    private SharedPreferences settingPrefs;
    private SharedPreferences.Editor editorSplash;
    private SharedPreferences.Editor editorStudent;

    private boolean preferencesChanged = true;

    private TextView mNotLoginTextView;
    private ImageView bannerImageView;
    int bannerChange;
    int studentId;
    private DBHelper db;
    private Student student;
    private Handler mHandler;

    /**
     *  onCreate generates the layout when its View is created.
     *  Check the login status on SharedPreferences
     * @param savedInstanceState any saved state to restore in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///
        studentSharedPrefs = getSharedPreferences(STUDENT_PREFS, 0);
        editorStudent = studentSharedPrefs.edit();
        studentId = studentSharedPrefs.getInt("studentId", 0); // if not, id = 0

        // go to SplashActivity
        splashSharedPrefs = getSharedPreferences(SPLASH_PREF, 0);
        editorSplash = splashSharedPrefs.edit();
        if(splashSharedPrefs.getInt("splash", 0) == 0)
        {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        else if(splashSharedPrefs.getInt("splash", 0) == 1)
        {
            editorSplash.putInt("splash", 0); // put it back to 0, so it will splash next time
            editorSplash.apply();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), "Home").commit();
        }
        else if(splashSharedPrefs.getInt("splash", 0) == -1)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), "Home").commit();
        }

        /*PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(preferenceChangeListener);*/

        /*mNotLoginTextView = (TextView) findViewById(R.id.notLoginTextView);
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        bannerChange = 1;*/

        /*// Restore preferences
        studentSharedPrefs = getSharedPreferences(STUDENT_PREFS, 0);
        editorStudent = studentSharedPrefs.edit();
        studentId = studentSharedPrefs.getInt("studentId", 0); // if not, id = 0
        if(studentId == 0)
        {
            mNotLoginTextView.setText(R.string.not_login);
            mNotLoginTextView.setEnabled(true);
        }
        else
        {
            db = new DBHelper(this);
            student = db.getStudent(studentId);
            mNotLoginTextView.setText("Have a nice day "
                    + student.getFirstName() + " "
                    + student.getLastName());
            mNotLoginTextView.setTextSize(18);
        }*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame);

            if(currentFragment.getTag().equals("Home"))
                super.onBackPressed();
            else {
                fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), "Home").commit();
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, new HomeFragment(), "Home").commit();
        }
        else if (id == R.id.nav_studentProfile) {
            if(studentId != 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new StudentProfileFragment(), "Student Profile").commit();
            }
            else
                startActivity(new Intent(this, LoginActivity.class));

        }
        else if (id == R.id.nav_reserveRoom) {
            if(studentId != 0) {
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        Fragment fragment = new ReserveRoomFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.frame, fragment, "Reserve Room");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };
                // If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You must login to use this feature.");
                builder.setCancelable(false);

                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.cancel();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

        }
        else if (id == R.id.nav_borrowBook) {
            if(studentId != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This function coming soon");
                builder.setCancelable(false);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You must login to use this feature.");
                builder.setCancelable(false);

                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.cancel();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

        }
        else if (id == R.id.nav_setting) {
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);

        }
        else if (id == R.id.nav_contact_us) {
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    // update the main content by replacing fragments
                    Fragment fragment = new ContactUsFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "Contact Us");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };
            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }

        }
        else if(id == R.id.nav_about){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("About page coming soon");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            //builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * start the application
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * call the login activity
     * @param view
     */
    /*public void login(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }*/

    /**
     * It will call the login activity if student haven't login yet.
     * Otherwise, it will call student profile activity
     * @param view
     */
    /*public void studentProfile(View view)
    {
        if(studentId == 0)
        {
            login(view);
        }
        else {
            Intent intent = new Intent(this, StudentProfileActivity.class);
            startActivity(intent);
        }
    }*/

    /**
     * stop the application
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     *  onResume is call when user go back to MainActivity
     *  It will turn on or off the SplashActivity
     *  Register the SharedPreference listener
     */
    @Override
    protected void onResume()
    {
        // This work on Portrait
        settingPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean switchPrefs = settingPrefs.getBoolean("splashPrefs", true);
        if(!switchPrefs) //if not true
        {
            editorSplash.putInt("splash", -1);
            editorSplash.apply();
        }
        else
        {
            if(splashSharedPrefs.getInt("splash", 0) == -1) {
                editorSplash.putInt("splash", 0);
                editorSplash.apply();
            }
        }
        super.onResume();
        // this work on Landscape
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * unregister SharePreferences listener
     */
    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Show the Menu option setting
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Control the Options Menu setting. It will change menu title to Login or Logout
     * It also remove setting option on landscape orientated.
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            menu.findItem(R.id.action_settings).setVisible(false);

        if(studentId == 0)
        {
            menu.findItem(R.id.action_log).setTitle(R.string.login);
        }
        else
            menu.findItem(R.id.action_log).setTitle(R.string.logout);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Depend on what option user click. It will go to setting activity or login activity
     * or logout.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                return true;
            case R.id.action_log:
                if(studentId != 0) {
                    editorStudent.clear();
                    editorStudent.apply();

                    editorSplash.putInt("splash", 1); // put it 1, so it will not splash
                    editorSplash.apply();

                    Toast.makeText(this, "You successfully logout", Toast.LENGTH_SHORT).show();

                    this.finish();
                    startActivity(getIntent()); // refresh itself
                    return true;
                }
                else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    // Preferences doesn't work
    /*private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));

                    if(key.equals(SPLASH_PREF))
                    {
                        Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));

                        if(splashSharedPrefs.getInt("splash", 0) == 0)
                        {
                            editorSplash.putInt("splash", -1);
                            editorSplash.apply();
                            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
                        }
                        else if(splashSharedPrefs.getInt("splash", 0) == -1)
                        {
                            editorSplash.putInt("splash", 0);
                            editorSplash.apply();
                            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
                        }

                    }
                }
            };*/

    /**
     * Start ContactActivity by use of an Intent (no data passed)
     * @param view
     */
    /*public void contactUs(View view)
    {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }*/

    /**
     * Pop-up Alert dialog because this function haven't set up yet.
     * @param view
     */
    /*public void borrowBook(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    }*/

    /**
     * Change image of the banner, then shake the image left and right.
     * @param view
     */
    /*public void toggleShakeAnim(View view)
    {
        AssetManager am = this.getAssets();
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

        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_anim);
        bannerImageView.startAnimation(shake);
    }*/

    /**
     /** onClick method for Reserve Room button
     * also check if user is not login yet or already have a upcoming room booking.
     * Long Truong
     */
    /*public void reserveRoom(View view)
    {
        if(studentId == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

            Intent intent = new Intent(this, PickDateActivity.class);
            startActivity(intent);

        }
    }*/

    /**
     * Listener to handle changes in setting of the app's shared preferences (preferences.xml)
     * If the switch preference are changed, it will change the SharedPreferences to turn on or off splashActivity
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        settingPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean switchPrefs = settingPrefs.getBoolean("splashPrefs", true);
        if(!switchPrefs) //if not true
        {
            editorSplash.putInt("splash", -1);
            editorSplash.apply();
        }
        else
        {
            if(splashSharedPrefs.getInt("splash", 0) == -1) {
                editorSplash.putInt("splash", 0);
                editorSplash.apply();
            }
        }
        /*if(key.equals(SPLASH_PREF))
        {
            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));

            if(splashSharedPrefs.getInt("splash", 0) == 0)
            {
                editorSplash.putInt("splash", -1);
                editorSplash.apply();
                Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
            }
            else if(splashSharedPrefs.getInt("splash", 0) == -1)
            {
                editorSplash.putInt("splash", 0);
                editorSplash.apply();
                Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
            }

        }*/
    }

}
