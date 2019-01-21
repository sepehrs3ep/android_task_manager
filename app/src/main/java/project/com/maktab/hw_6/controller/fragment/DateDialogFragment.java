package project.com.maktab.hw_6.controller.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import project.com.maktab.hw_6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateDialogFragment extends DialogFragment {
    private TabLayout mTabLayout;
    private Date mDate;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private static final String MULTI_DIALOG_ARGS = "multi_dialog_args";
    private Button mOkBtn;
    private Button mCancelBtn;
    private static final String CALENDER_EXTRA = "project.com.maktab.hw_6.controller.fragment.calenderExtra";

    public static String getCalenderExtra() {
        return CALENDER_EXTRA;
    }

    public DateDialogFragment() {
        // Required empty public constructor
    }

    public static DateDialogFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(MULTI_DIALOG_ARGS, date);
        DateDialogFragment fragment = new DateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(MULTI_DIALOG_ARGS);
    }
    private String getFormatTime() {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
        String timeOutput = timeFormat.format(mDate);
        return timeOutput;
    }

    private String getFormatDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
        String dateOutput = dateFormat.format(mDate);
        return dateOutput;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_dialog, container, false);
        mTabLayout = view.findViewById(R.id.multi_tabLayout);
        mDatePicker = view.findViewById(R.id.date_multi_picker);
        mTimePicker = view.findViewById(R.id.time_multi_picker);
        mOkBtn = view.findViewById(R.id.date_time_ok_btn);
        mCancelBtn = view.findViewById(R.id.date_time_cancel_btn);
        mTimePicker.setIs24HourView(true);
        mDatePicker.setVisibility(View.VISIBLE);
        mTabLayout.getTabAt(0).setText(getFormatDate());
        mTabLayout.getTabAt(1).setText(getFormatTime());
        mTimePicker.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR);
        final int minutes = calendar.get(Calendar.MINUTE);
        mDatePicker.init(year, month, day, null);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setText(getFormatDate());
                        mDatePicker.setVisibility(View.VISIBLE);
                        mTimePicker.setVisibility(View.GONE);
                        mDatePicker.init(year, month, day, null);
                        break;
                    case 1:
                        tab.setText(getFormatTime());
                        mDatePicker.setVisibility(View.GONE);
                        mTimePicker.setVisibility(View.VISIBLE);
                        mTimePicker.setHour(hour);
                        mTimePicker.setMinute(minutes);
                        break;
                    default:
                        mDatePicker.setVisibility(View.GONE);
                        mTimePicker.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                int hour = mTimePicker.getHour();
                int minutes = mTimePicker.getMinute();
                Date date = new GregorianCalendar(year, month, day, hour, minutes).getTime();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(CALENDER_EXTRA, date);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);
                dismiss();
            }
        });


        return view;
    }

}
