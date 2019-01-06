package project.com.maktab.hw_6.controller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.UUID;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.model.task.Task;
import project.com.maktab.hw_6.model.task.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrudShowDialogFragment extends DialogFragment {
    private Task mTask;
    private TextView mTaskTitle;
    private TextView mTaskDesc;
    private TextView mTaskDate;
    private TextView mTaskTime;
    private static final String ID_SHOW_ARGS = "idShowArgs";
    private Button mNavigateEditBtn;
    private UUID mId;

    public static CrudShowDialogFragment newInstance(UUID id) {

        Bundle args = new Bundle();
        args.putSerializable(ID_SHOW_ARGS, id);
        CrudShowDialogFragment fragment = new CrudShowDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrudShowDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = (UUID) getArguments().getSerializable(ID_SHOW_ARGS);
        mTask = TaskRepository.getInstance(getActivity()).getTaskByID(mId);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crud_show_dialog, container, false);
        mTaskTitle = view.findViewById(R.id.title_show_crud_dialog);
        mTaskDesc = view.findViewById(R.id.desc_show_crud_dialog);
        mTaskDate = view.findViewById(R.id.date_show_crud_dialog);
        mTaskTime = view.findViewById(R.id.time_show_crud_dialog);
        mNavigateEditBtn = view.findViewById(R.id.edit_show_curd_btn);
        mTaskTitle.setText(mTask.getTitle());
        mTaskDesc.setText(mTask.getDescription());
        setDateTextView();
        setTimeTextView();

        mNavigateEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrudTaskFragment fragment = CrudTaskFragment.getInstance(mId, false);
                dismiss();
                fragment.show(getFragmentManager(), "goto edit");
            }
        });
        return view;
    }

    private void setDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
        String dateOutput = dateFormat.format(mTask.getDate());
        mTaskDate.setText(dateOutput);
    }

    private void setTimeTextView() {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm");
        String timeOutput = timeFormat.format(mTask.getDate());
        mTaskTime.setText(timeOutput);
    }

}
