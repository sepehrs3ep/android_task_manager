package project.com.maktab.hw_6.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import project.com.maktab.hw_6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private Button mSignUpBtn;
    private Button mSignInBtn;


    public static LoginFragment newInstance() {


        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mUserNameEt = view.findViewById(R.id.user_name_edit_text);
        mPasswordEt = view.findViewById(R.id.password_edit_text);
        mSignUpBtn = view.findViewById(R.id.sign_up_btn);
        mSignInBtn = view.findViewById(R.id.sign_in_btn);
        return view;
    }

}
