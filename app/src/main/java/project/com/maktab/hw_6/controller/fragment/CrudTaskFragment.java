package project.com.maktab.hw_6.controller.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.model.task.Task;
import project.com.maktab.hw_6.model.task.TaskRepository;
import project.com.maktab.hw_6.model.task.TaskType;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrudTaskFragment extends Fragment {
    private static final String ARGS_EXTRA_ID = "args_extra_id";
    private static final String ARGS_EXTRA_HAS_EXTRA = "args_extra_has_extra";
    private static final int DATE_REQ_CODE = 1;
    private static final String DATE_TAG = "date_tag";
    private static final String TIME_TAG = "time_tag";
    private static final int CALENDER_REQ_CODE = 2;
    private static final int TIME_REQ_CODE = -1;
    public static boolean IS_EMPTY = false;
    private EditText mEditTextTitle;
    private EditText mEditTextDesc;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private Button mCalenderBtn;
    private static boolean mFromFloatButton;
    private static Task mTask;
    public static boolean mStay;
    private static String mRawTextTitle = "";
    private String mTaskTitle = "";

    public static CrudTaskFragment getInstance(UUID id, boolean hasExtra) {
        CrudTaskFragment fragment = new CrudTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_EXTRA_ID, id);
        bundle.putBoolean(ARGS_EXTRA_HAS_EXTRA, hasExtra);
        fragment.setArguments(bundle);
        return fragment;
    }


    public CrudTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getArguments().getSerializable(ARGS_EXTRA_ID);
        mFromFloatButton = getArguments().getBoolean(ARGS_EXTRA_HAS_EXTRA);
        mTask = TaskRepository.getInstance(getActivity()).getTaskByID(id);
        mTaskTitle = mTask.getTitle();
        mRawTextTitle = mTaskTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coordinate_view, container, false);

        mEditTextTitle = view.findViewById(R.id.title_edit_text);
        mEditTextDesc = view.findViewById(R.id.desc_edit_text);
        LinearLayout layoutButtonSheet = view.findViewById(R.id.bottom_sheet);
        /*final Drawable colorDrawable = mEditTextDesc.getBackground();
        mEditTextTitle.setBackground(colorDrawable);*/
        mDateTextView = view.findViewById(R.id.date_text_view);
        mTimeTextView = view.findViewById(R.id.time_text_view);
        mCalenderBtn = view.findViewById(R.id.calender_button);
        Button buttonAddCrud = layoutButtonSheet.findViewById(R.id.crud_add);
        Button buttonEditCrud = layoutButtonSheet.findViewById(R.id.crud_edit);
        Button buttonDoneCrud = layoutButtonSheet.findViewById(R.id.crud_done);
        Button buttonDeleteCrud = layoutButtonSheet.findViewById(R.id.crud_delete);
        setDateTextView();
        setTimeTextView();
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setTitle(s.toString());
            }

            //check back button pressed for edit.
            @Override
            public void afterTextChanged(Editable s) {
                IS_EMPTY = s.length() == 0;
            }
        });
        mEditTextDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (mFromFloatButton) {
            buttonAddCrud.setVisibility(View.VISIBLE);
            buttonDoneCrud.setVisibility(View.GONE);
            buttonEditCrud.setVisibility(View.GONE);
            buttonDeleteCrud.setVisibility(View.GONE);

            buttonAddCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addTask();
                }
            });
        }
        /*
        check and separate from float button from check on that button is a boolean
        that come from main view pager activity.
         */
        if (!mFromFloatButton) {
            final Toast toast = Toast.makeText(getActivity(), R.string.toast_req_success, Toast.LENGTH_SHORT);

            buttonEditCrud.setVisibility(View.VISIBLE);
            buttonDeleteCrud.setVisibility(View.VISIBLE);
            buttonDoneCrud.setVisibility(View.VISIBLE);
            buttonAddCrud.setVisibility(View.GONE);
            mEditTextTitle.setText(mTask.getTitle());
            mEditTextDesc.setText(mTask.getDescription());


            buttonEditCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTask.getTitle();
                    if (title == null || title.equals("")) {
                        checkTitle();
                    } else {
                        mEditTextTitle.setBackgroundColor(Color.WHITE);
                        TaskRepository.getInstance(getActivity()).updateTask(mTask);
                        toast.show();
                        getActivity().finish();

                    }
                }
            });
            buttonDeleteCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogFragment deleteAlert = AlertDialogFragment.getInstance("Are you sure you want to delete this item?");
                    deleteAlert.show(getFragmentManager(), "Delete Alert");
                    deleteAlert.setOnYesNoClick(new AlertDialogFragment.OnYesNoClick() {
                        @Override
                        public void onYesClicked() {
                            TaskRepository.getInstance(getActivity()).removeTask(mTask.getID());
                            toast.show();
                            getActivity().finish();
                        }

                        @Override
                        public void onNoClicked() {

                        }
                    });


                }
            });

            if (mTask.getTaskType().equals(TaskType.DONE))
                buttonDoneCrud.setEnabled(false);

            buttonDoneCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTask.getTitle();
                    if (title == null || title.equals("")) {
                        checkTitle();
                    } else {
                        mTask.setTaskType(TaskType.DONE);
                        TaskRepository.getInstance(getActivity()).updateTask(mTask);
                        toast.show();
                        getActivity().finish();

                    }

                }
            });


        }
   /*     mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             *//*   TimePickerFragment fragment = TimePickerFragment.newInstance(mTask.getDate());
                fragment.setTargetFragment(CrudTaskFragment.this, TIME_REQ_CODE);
                fragment.show(getFragmentManager(), TIME_TAG);*//*
             DateDialogFragment fragment = new DateDialogFragment();
             fragment.show(getFragmentManager(),"tag");
            }
        });
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = DatePickerFragment.newInstance(mTask.getDate());
                fragment.setTargetFragment(CrudTaskFragment.this, DATE_REQ_CODE);
                fragment.show(getFragmentManager(), DATE_TAG);
            }
        });*/
        mCalenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment fragment = DateDialogFragment.newInstance(mTask.getDate());
                fragment.setTargetFragment(CrudTaskFragment.this, CALENDER_REQ_CODE);
                fragment.show(getFragmentManager(), "calenderTag");
            }
        });


        return view;
    }


    private void addTask() {
        String title = mEditTextTitle.getText().toString();
        if (title == null || title.equals("")) {
            Snackbar.make(getView(), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
            mEditTextTitle.setBackgroundColor(Color.RED);
        } else {
//                        mEditTextTitle.setBackgroundColor(descDrawble.getColor());
            mEditTextTitle.setBackgroundColor(Color.WHITE);
            String desc = mEditTextDesc.getText().toString();
            mTask.setTitle(title);
            mTask.setDescription(desc);
//            TaskRepository.getInstance().addTask(mTask);
            TaskRepository.getInstance(getActivity()).updateTask(mTask);
            Toast.makeText(getActivity(), R.string.toast_crud_success, Toast.LENGTH_SHORT).show();
            getActivity().finish();

        }
    }

    private void setTimeTextView() {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
        String timeOutput = timeFormat.format(mTask.getDate());
        mTimeTextView.setText(timeOutput);
    }

    private void setDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
        String dateOutput = dateFormat.format(mTask.getDate());
        mDateTextView.setText(dateOutput);
    }

    public void checkTitle() {
        Snackbar.make(getView(), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
        mEditTextTitle.setBackgroundColor(Color.RED);
        mTask.setTitle(mTaskTitle);
        mEditTextTitle.setText(mTask.getTitle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == CALENDER_REQ_CODE) {
            Date date = (Date) data.getSerializableExtra(DateDialogFragment.getCalenderExtra());
            mTask.setDate(date);
            setDateTextView();
            setTimeTextView();

        }
       /* if (requestCode == TIME_REQ_CODE) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.getTimeExtra());
            mTask.setDate(date);
            setTimeTextView();


        }*/
    }

    //check back pressed for add button
    public static boolean onBackPressed(final Context context) {
        String mTaskTitle = mTask.getTitle();
        final Activity activity = (Activity) context;
        if (mTaskTitle.length() > 0) {

            if (mFromFloatButton)
                checkForChangesBack(context, activity);
            else {
                if (mRawTextTitle.equals(mTaskTitle))
                    activity.finish();
                else checkForChangesBack(context, activity);

            }

        }

        if (mTaskTitle == null || mTaskTitle.equals("") || mTaskTitle.length() <= 0) {
            TaskRepository.getInstance(context).removeTask(mTask.getID());
            return true;
        }
        return false;


    }

    private static void checkForChangesBack(final Context context, final Activity activity) {
        mStay = true;
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("save your changes?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskRepository.getInstance(context).updateTask(mTask);
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mFromFloatButton)
                            TaskRepository.getInstance(context).removeTask(mTask.getID());
                        activity.finish();
                    }
                })
                .create();
        alertDialog.show();
    }


}


