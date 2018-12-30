package project.com.maktab.hw_6.controller.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import project.com.maktab.hw_6.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String ARGS_DATE_PICKER = "args_date_picker";
    private static final String DATE_PICKER_EXTRA = "project.com.maktab.hw_6.date_picker_extra";

    public static String getDatePickerExtra() {
        return DATE_PICKER_EXTRA;
    }

    private Date mDate;
    private DatePicker mDatePicker;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Date date) {


        Bundle args = new Bundle();
        args.putSerializable(ARGS_DATE_PICKER, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARGS_DATE_PICKER);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker, null);
        mDatePicker = view.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, null);


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.task_date_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = getDatePickerDate();
                        sendResult(date);

                    }
                })
                .setView(view)
                .create();
    }

    private Date getDatePickerDate() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
         return new GregorianCalendar(year, month, day).getTime();
    }

    private void sendResult(Date date) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(DATE_PICKER_EXTRA, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);
    }
}
