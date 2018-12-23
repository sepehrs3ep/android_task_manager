package project.com.maktab.hw_6;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

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
    private EditText mEditTextDate;
    private EditText mEditTextTime;

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
        mEditTextDate = view.findViewById(R.id.date_edit_text);
        mEditTextTime = view.findViewById(R.id.time_edit_text);
        if (!mHasExtra) {
            mButtonDoneCrud.setEnabled(false);
            mButtonDeleteCrud.setEnabled(false);
            mButtonEditCrud.setEnabled(false);
            mButtonAddCrud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mEditTextTitle.getText().toString();
                    String desc = mEditTextDesc.getText().toString();
                    String date = mEditTextDate.getText().toString();
                    String time = mEditTextTime.getText().toString();
                    TaskRepository.getInstance().addToAll(title, desc, date, time);
                    Toast.makeText(getActivity(), R.string.toast_crud_success, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });


        }


        return view;
    }

}
