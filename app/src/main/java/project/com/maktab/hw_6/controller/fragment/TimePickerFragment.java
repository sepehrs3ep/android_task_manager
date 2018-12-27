package project.com.maktab.hw_6.controller.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import project.com.maktab.hw_6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String TIME_ARGS = "time_args";


    private static final String TIME_EXTRA = "project.com.maktab.hw_6.controller.fragment.time_extra";
    private TimePicker mTimePicker;
    private Date mDate;


    public static String getTimeExtra() {
        return TIME_EXTRA;
    }

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(TIME_ARGS, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(TIME_ARGS);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker, null);
        mTimePicker = view.findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minutes);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.crime_time_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = getTimePickerTime(year, month, day);
                        sendResult(date);

                    }
                })
                .setView(view)
                .create();


    }

    private Date getTimePickerTime(int year, int month, int day) {
        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        return new GregorianCalendar(year, month, day, hour, minute).getTime();
    }

    private void sendResult(Date date) {
        Intent intent = new Intent();
        intent.putExtra(TIME_EXTRA, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
