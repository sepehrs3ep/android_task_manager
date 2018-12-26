package project.com.maktab.hw_6;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.UUID;

import project.com.maktab.hw_6.model.Task;
import project.com.maktab.hw_6.model.TaskRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrudTaskFragment extends Fragment {
    private static final String ARGS_EXTRA_ID = "args_extra_id";
    private static final String ARGS_EXTRA_HAS_EXTRA = "args_extra_has_extra";
    private Button mButtonAddCrud;
    private Button mButtonEditCrud;
    private Button mButtonDeleteCrud;
    private Button mButtonDoneCrud;
    private EditText mEditTextTitle;
    private EditText mEditTextDesc;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private boolean mFromFloatButton;
    private Task mTask;

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
        mTask = TaskRepository.getInstance().getTaskByID(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crud_task, container, false);
        mButtonAddCrud = view.findViewById(R.id.crud_add);
        mButtonDeleteCrud = view.findViewById(R.id.crud_delete);
        mButtonDoneCrud = view.findViewById(R.id.crud_done);
        mButtonEditCrud = view.findViewById(R.id.crud_edit);
        mEditTextTitle = view.findViewById(R.id.title_edit_text);
        mEditTextDesc = view.findViewById(R.id.desc_edit_text);
        final Drawable descDrawble = mEditTextDesc.getBackground();
//        mEditTextTitle.setBackgroundColor(descDrawble.getColor());
        mEditTextTitle.setBackground(descDrawble);
        mTextViewDate = view.findViewById(R.id.date_text_view);
        mTextViewTime = view.findViewById(R.id.time_text_view);
        if (mFromFloatButton) {
            mButtonAddCrud.setEnabled(true);
            mButtonDoneCrud.setEnabled(false);
            mButtonDeleteCrud.setEnabled(false);
            mButtonEditCrud.setEnabled(false);
            mButtonAddCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mEditTextTitle.getText().toString();
                    if (title == null || title.equals("")) {
                        Snackbar.make(getView(), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
                        mEditTextTitle.setBackgroundColor(Color.RED);
                    } else {
//                        mEditTextTitle.setBackgroundColor(descDrawble.getColor());
                        mEditTextTitle.setBackground(descDrawble);
                        String desc = mEditTextDesc.getText().toString();
                        mTask.setTitle(title);
                        mTask.setDescription(desc);
                        mTask.setTaskType(-1);
                        Toast.makeText(getActivity(), R.string.toast_crud_success, Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    }
                }
            });
        }
        if (!mFromFloatButton) {
            final Toast toast = Toast.makeText(getActivity(), R.string.toast_req_success, Toast.LENGTH_SHORT);
            mButtonEditCrud.setEnabled(true);
            mButtonDeleteCrud.setEnabled(true);
            mButtonDoneCrud.setEnabled(true);
            mButtonAddCrud.setEnabled(false);
            mEditTextTitle.setText(mTask.getTitle());
            mEditTextDesc.setText(mTask.getDescription());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
            String dateOutput = dateFormat.format(mTask.getDate());
            final SimpleDateFormat timeFormat = new SimpleDateFormat("h-m-a");
            String timeOutput = timeFormat.format(mTask.getTime());
            mTextViewDate.setText(dateOutput);
            mTextViewTime.setText(timeOutput);
            mEditTextTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mTask.setTitle(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

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
            mButtonEditCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTask.getTitle();
                    if (title == null || title.equals("")) {
                        checkTitle();
                    } else {
                        mEditTextTitle.setBackground(descDrawble);
                        toast.show();
                        getActivity().finish();

                    }
                }
            });
            mButtonDeleteCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogFragment deleteAlert = AlertDialogFragment.getInstance("Are you sure you want to delete this item?");
                    deleteAlert.show(getFragmentManager(), "Delete Alert");
                    deleteAlert.setOnYesNoClick(new AlertDialogFragment.OnYesNoClick() {
                        @Override
                        public void onYesClicked() {
                            TaskRepository.getInstance().removeTask(mTask.getID());
                            toast.show();
                            getActivity().finish();
                        }

                        @Override
                        public void onNoClicked() {

                        }
                    });


                }
            });
       /* if (mIsFromDoneList) {
            mButtonDoneCrud.setEnabled(false);
        }*/
            mButtonDoneCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mTask.getTitle();
                    if (title == null || title.equals("")) {
                        checkTitle();
                    } else {
                        mTask.setTaskType(1);
                        toast.show();
                        getActivity().finish();

                    }

                }
            });


        }
        return view;
    }

    private void checkTitle() {
        Snackbar.make(getView(), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
        mEditTextTitle.setBackgroundColor(Color.RED);
        mEditTextTitle.setText(mTask.getTitle());
    }

}
