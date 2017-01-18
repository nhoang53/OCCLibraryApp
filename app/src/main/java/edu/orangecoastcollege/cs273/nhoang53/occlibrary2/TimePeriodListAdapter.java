package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper class to provide custom adapter for the <code>TimePeriod</code> list.
 */
public class TimePeriodListAdapter extends ArrayAdapter<TimePeriod> {

    private Context mContext;
    private List<TimePeriod> mTimePeriodsList = new ArrayList<>();
    private int mResourceId;

    /**
     * Creates a new <code>TimePeriodListAdapter</code> given a mContext, resource id and list of offerings.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param timePeriods The list of time period to display
     */
    public TimePeriodListAdapter(Context c, int rId, List<TimePeriod> timePeriods) {
        super(c, rId, timePeriods);
        mContext = c;
        mResourceId = rId;
        mTimePeriodsList = timePeriods;
    }

    /**
     * Gets the view associated with the layout.
     * @param pos The position of the Offering selected in the list.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final TimePeriod selectedTimePeriod = mTimePeriodsList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout periodLinearLayout = (LinearLayout) view.findViewById(R.id.periodLinearLayout);
        TextView fromTextView =
                (TextView) view.findViewById(R.id.fromTextView);
        TextView toTextView =
                (TextView) view.findViewById(R.id.toTextView);

        //TODO:  Make a reference to the offeringListCRNTextView and set the text accordingly.


        periodLinearLayout.setTag(selectedTimePeriod);
        DateFormat sdf = new SimpleDateFormat("hh:mm aa");
        Time startTime = Time.valueOf(selectedTimePeriod.getStartTime());
        Time endTime = Time.valueOf(selectedTimePeriod.getEndTime());
        fromTextView.setText(sdf.format(startTime));
        toTextView.setText(sdf.format(endTime));

        // Resize the layout
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);

        Time t1 = Time.valueOf(selectedTimePeriod.getStartTime());
        Time t2 = Time.valueOf(selectedTimePeriod.getEndTime());
        int duration = (int)(t2.getTime() - t1.getTime()) /(60*60*1000);
        params.height = 150* duration;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        periodLinearLayout.setLayoutParams(params);


        // Set color
        Drawable background = ContextCompat.getDrawable(mContext, R.drawable.empty_background);
        boolean status = selectedTimePeriod.getStatus();
        if (!status) {

            background = ContextCompat.getDrawable(mContext, R.drawable.empty_background);
        }
        else
        {
            periodLinearLayout.setClickable(false);
            background = ContextCompat.getDrawable(mContext, R.drawable.full_background);
        }


        periodLinearLayout.setBackground(background);


        return view;
    }
}
