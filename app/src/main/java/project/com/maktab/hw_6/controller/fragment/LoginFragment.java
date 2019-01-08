package project.com.maktab.hw_6.controller.fragment;


import android.content.Intent;
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
import android.widget.Toast;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.activity.MainViewPagerActivity;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private Button mSignUpBtn;
    private Button mSignInBtn;
    private User mUser;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mUserNameEt = view.findViewById(R.id.user_name_edit_text);
        mPasswordEt = view.findViewById(R.id.password_edit_text);
        mSignUpBtn = view.findViewById(R.id.sign_up_btn);
        mSignInBtn = view.findViewById(R.id.sign_in_btn);

        mUserNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = UserRepository.getInstance(getActivity()).createUser(mUser);
                sendIntent(id);
            }
        });
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = UserRepository.getInstance(getActivity()).login(mUser);
                if (id > 0)
                    sendIntent(id);
                else
                    Toast.makeText(getActivity(), "can't find your account", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void sendIntent(long id) {
        Intent sendIntent = MainViewPagerActivity.newIntent(getActivity(), id);
        startActivity(sendIntent);
    }

}
