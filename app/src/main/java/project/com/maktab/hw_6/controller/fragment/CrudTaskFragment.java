package project.com.maktab.hw_6.controller.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.hw_6.MyDialogCloseListener;
import project.com.maktab.hw_6.PictureUtils;
import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.model.task.Task;
import project.com.maktab.hw_6.model.task.TaskRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrudTaskFragment extends DialogFragment {
    private static final String ARGS_EXTRA_ID = "args_extra_id";
    private static final String ARGS_EXTRA_HAS_EXTRA = "args_extra_has_extra";
    private static final int DATE_REQ_CODE = 1;
    private static final String DATE_TAG = "date_tag";
    private static final String TIME_TAG = "time_tag";
    private static final int CALENDER_REQ_CODE = 2;
    private static final int TIME_REQ_CODE = -1;
    private static final int REQ_PHOTOS = 24;
    public static boolean IS_EMPTY = false;
    private EditText mEditTextTitle;
    private EditText mEditTextDesc;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private Long mUserId;
    public static final String USER_ID_ARGS = "userId";
    private Button mCalenderBtn;
    private CircleImageView mTaskCircleImageView;
    private static boolean mFromFloatButton;
    private static Task mTask;
    public static boolean mStay;
    private File mPhotoFile;
    private ImageButton mUploadTaskImageBtn;
    private static String mRawTextTitle = "";
    private String mTaskTitle = "";

    public static CrudTaskFragment getInstance(UUID id, boolean hasExtra, Long userId) {
        CrudTaskFragment fragment = new CrudTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_EXTRA_ID, id);
        bundle.putBoolean(ARGS_EXTRA_HAS_EXTRA, hasExtra);
        bundle.putLong(USER_ID_ARGS, userId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public CrudTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener) activity).handleDialogClose(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID id = (UUID) getArguments().getSerializable(ARGS_EXTRA_ID);
        mFromFloatButton = getArguments().getBoolean(ARGS_EXTRA_HAS_EXTRA);
        if (mFromFloatButton) {
            mUserId = getArguments().getLong(USER_ID_ARGS);
            mTask = new Task();
        } else
            mTask = TaskRepository.getInstance(getActivity()).getTaskByID(id);


        mTaskTitle = mTask.getMTitle();
        mRawTextTitle = mTaskTitle;
    }

    private void SaveImage(Bitmap finalBitmap) {

        File file = getFileForCamera();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getFileForCamera() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        return file;
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
        mTaskCircleImageView = view.findViewById(R.id.task_circle_image_view);
        mUploadTaskImageBtn = view.findViewById(R.id.upload_task_image);
        mCalenderBtn = view.findViewById(R.id.calender_button);
        Button buttonAddCrud = layoutButtonSheet.findViewById(R.id.crud_add);
        Button buttonEditCrud = layoutButtonSheet.findViewById(R.id.crud_edit);
        Button buttonDoneCrud = layoutButtonSheet.findViewById(R.id.crud_done);
        Button buttonDeleteCrud = layoutButtonSheet.findViewById(R.id.crud_delete);
        setDateTextView();
        setTimeTextView();

        if (mTask.getMTaskImageUri() != null)
            updateTaskPhoto(Uri.parse(mTask.getMTaskImageUri()));

        mUploadTaskImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                try {
                    mPhotoFile = getFileForCamera();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
                    return;
                }

                Uri uri = Uri.fromFile(mPhotoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);


                startActivityForResult(captureIntent, REQ_PHOTOS);
            }
        });
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setMTitle(s.toString());
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
                mTask.setMDescription(s.toString());
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
            mEditTextTitle.setText(mTask.getMTitle());
            mEditTextDesc.setText(mTask.getMDescription());


            buttonEditCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTask.getMTitle();
                    if (title == null || title.equals("")) {
                        checkTitle();
                    } else {
                        mEditTextTitle.setBackgroundColor(Color.WHITE);
                        TaskRepository.getInstance(getActivity()).updateTask(mTask);
                        toast.show();
                        dismiss();

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
                            TaskRepository.getInstance(getActivity()).removeTask(mTask);
                            toast.show();
                            dismiss();
                        }

                        @Override
                        public void onNoClicked() {

                        }
                    });


                }
            });

            if (mTask.getMTaskType().equals(Task.TaskType.DONE))
                buttonDoneCrud.setEnabled(false);

            buttonDoneCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTask.getMTitle();
                    if (title == null || title.equals("")) {
                        checkTitle();
                    } else {
                        mTask.setMTaskType(Task.TaskType.DONE);
                        TaskRepository.getInstance(getActivity()).updateTask(mTask);
                        toast.show();
                        dismiss();

                    }

                }
            });


        }
   /*     mTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
             *//*   TimePickerFragment fragment = TimePickerFragment.newInstance(mTask.getDate());
                fragment.setTargetFragment(CrudTaskFragment.this, TIME_REQ_CODE);
                fragment.show(getFragmentManager(), TIME_TAG);*//*
             DateDialogFragment fragment = new DateDialogFragment();
             fragment.show(getFragmentManager(),"tag");
=======
                TimePickerFragment fragment = TimePickerFragment.newInstance(mTask.getDate());
                fragment.setTargetFragment(CrudTaskFragment.this, TIME_REQ_CODE);
                fragment.show(getFragmentManager(), TIME_TAG);
>>>>>>> parent of 9d1af47... + multi date and time picker on dialog created.
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
                DateDialogFragment fragment = DateDialogFragment.newInstance(mTask.getMDate());
                fragment.setTargetFragment(CrudTaskFragment.this, CALENDER_REQ_CODE);
                fragment.show(getFragmentManager(), "calenderTag");
            }
        });


        return view;
    }


    private void addTask() {
        mTask.setMTaskType(Task.TaskType.UNDONE);
        mTask.setMUserID(mUserId);
        TaskRepository.getInstance(getActivity()).addTask(mTask);
        String title = mEditTextTitle.getText().toString();
        if (title == null || title.equals("")) {
            Snackbar.make(getView(), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
            mEditTextTitle.setBackgroundColor(Color.RED);
        } else {
//                        mEditTextTitle.setBackgroundColor(descDrawble.getColor());
            mEditTextTitle.setBackgroundColor(Color.WHITE);
            String desc = mEditTextDesc.getText().toString();
            mTask.setMTitle(title);
            mTask.setMDescription(desc);
//            TaskRepository.getInstance().addTask(mTask);
            TaskRepository.getInstance(getActivity()).updateTask(mTask);
            Toast.makeText(getActivity(), R.string.toast_crud_success, Toast.LENGTH_SHORT).show();
            dismiss();

        }
    }

    private void setTimeTextView() {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
        String timeOutput = timeFormat.format(mTask.getMDate());
        mTimeTextView.setText(timeOutput);
    }

    private void setDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
        String dateOutput = dateFormat.format(mTask.getMDate());
        mDateTextView.setText(dateOutput);
    }

    public void checkTitle() {
        Snackbar.make(getView(), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
        mEditTextTitle.setBackgroundColor(Color.RED);
        mTask.setMTitle(mTaskTitle);
        mEditTextTitle.setText(mTask.getMTitle());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == CALENDER_REQ_CODE) {
            Date date = (Date) data.getSerializableExtra(DateDialogFragment.getCalenderExtra());
            mTask.setMDate(date);
            setDateTextView();
            setTimeTextView();
        }
        if (requestCode == REQ_PHOTOS) {
            Uri uri = Uri.fromFile(mPhotoFile);
            mTask.setMTaskImageUri(uri.toString());


            updateTaskPhoto(uri);
//            updatePhotoView();


        }
    }

    private void updateTaskPhoto(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = PictureUtils.decodeUri(getActivity(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mTaskCircleImageView.setImageBitmap(bitmap);
    }
       /* if (requestCode == TIME_REQ_CODE) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.getTimeExtra());
            mTask.setDate(date);
            setTimeTextView();


        }*/

    /*    //check back pressed for add button
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
        }*/
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mTaskCircleImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(),
                    getActivity());

            mTaskCircleImageView.setImageBitmap(bitmap);
        }
    }

    public File createTempFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }


}


