package project.com.maktab.hw_6;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private static final String ARGS_EXTRA_ID = "project.com.maktab.hw_6.args_extra_id";
    private static boolean mHasExtra = false;
    private Button mButtonAddCrud;
    private Button mButtonEditCrud;
    private Button mButtonDeleteCrud;
    private Button mButtonDoneCrud;
    private EditText mEditTextTitle;
    private EditText mEditTextDesc;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private Task mTask;

    public static CrudTaskFragment getInstance(UUID id) {
        mHasExtra = true;
        CrudTaskFragment fragment = new CrudTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_EXTRA_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CrudTaskFragment getInstance() {
        mHasExtra = false;
        return new CrudTaskFragment();
    }

    public CrudTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mHasExtra) {
            UUID id = (UUID) getArguments().getSerializable(ARGS_EXTRA_ID);
            mTask = TaskRepository.getInstance().getTaskByID(id);

        }
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
        mTextViewDate = view.findViewById(R.id.date_text_view);
        mTextViewTime = view.findViewById(R.id.time_text_view);
        if (!mHasExtra) {
            mButtonDoneCrud.setEnabled(false);
            mButtonDeleteCrud.setEnabled(false);
            mButtonEditCrud.setEnabled(false);
            mButtonAddCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mEditTextTitle.getText().toString();
                    String desc = mEditTextDesc.getText().toString();
                    TaskRepository.getInstance().addToAll(title, desc);
                    Toast.makeText(getActivity(), R.string.toast_crud_success, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }
        if (mHasExtra) {
            final Toast toast = Toast.makeText(getActivity(),R.string.toast_req_success,Toast.LENGTH_SHORT);
            mButtonEditCrud.setEnabled(true);
            mButtonDeleteCrud.setEnabled(true);
            mButtonDoneCrud.setEnabled(true);
            mButtonAddCrud.setEnabled(false);
            mEditTextTitle.setText(mTask.getTitle());
            mEditTextDesc.setText(mTask.getDescription());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
            String dateOutput = dateFormat.format(mTask.getDate());
            SimpleDateFormat timeFormat = new SimpleDateFormat("h-m-a");
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
                    toast.show();
                    getActivity().finish();
                }
            });
            mButtonDeleteCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskRepository.getInstance().removeFromAll(mTask.getID());
                    toast.show();
                    getActivity().finish();
                }
            });
            mButtonDoneCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskRepository.getInstance().addToDone(mTask);
                    toast.show();
                    getActivity().finish();
                }
            });


        }


        return view;
    }

}
