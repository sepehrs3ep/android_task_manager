package project.com.maktab.hw_6.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import project.com.maktab.hw_6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpDialogFragment extends Fragment {


    public SignUpDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_dialog, container, false);
    }

}
