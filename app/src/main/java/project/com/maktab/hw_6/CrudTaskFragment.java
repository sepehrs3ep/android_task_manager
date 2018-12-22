package project.com.maktab.hw_6;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrudTaskFragment extends Fragment {
private static final String ARGS_EXTRA_ID = "project.com.maktab.hw_6.args_extra_id" ;
private static boolean hasExtra = false;

    public static CrudTaskFragment getInstance(UUID id) {
        hasExtra = true;
        CrudTaskFragment fragment = new CrudTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_EXTRA_ID,id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CrudTaskFragment getInstance() {
        hasExtra = false;
        return new CrudTaskFragment();
    }
    public CrudTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crud_task, container, false);
    }

}
