package project.com.maktab.hw_6.controller.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;


import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.activity.LoginActivity;
import project.com.maktab.hw_6.controller.activity.MainViewPagerActivity;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpDialogFragment extends DialogFragment {
    private EditText mUserName;
    private TextInputEditText mUserPassword;
    private EditText mUserEmail;
    private User mUser;
    private Button mSignUpBtn;
    public static final String ID_ARGS = "id";
    public static final String IS_GEUST_ARGS = "isGeust";
    private long mUserId;
    private boolean mIsFromGeust;
    private User mGeustUser;

    public SignUpDialogFragment() {
        // Required empty public constructor
    }

    public static SignUpDialogFragment newInstance(long id, boolean isGeust) {

        Bundle args = new Bundle();
        args.putLong(ID_ARGS, id);
        args.putBoolean(IS_GEUST_ARGS, isGeust);
        SignUpDialogFragment fragment = new SignUpDialogFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new User();
        mUserId = getArguments().getLong(ID_ARGS);
        mIsFromGeust = getArguments().getBoolean(IS_GEUST_ARGS, false);
        if(mIsFromGeust)
        mGeustUser = UserRepository.getInstance(getActivity()).getUser(mUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_dialog, container, false);
        mUserName = view.findViewById(R.id.sign_up_user_name_et);
        mUserPassword = view.findViewById(R.id.sign_up_password_et);
        mUserEmail = view.findViewById(R.id.sign_up_email_et);
        mSignUpBtn = view.findViewById(R.id.sign_up_create_account_btn);

        if (mIsFromGeust) {
            mUserName.setText(mGeustUser.getName());
            mUserPassword.setText(mGeustUser.getPassword());
        }

        mUserName.addTextChangedListener(new TextWatcher() {
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
        mUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setEmail(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mUserPassword.addTextChangedListener(new TextWatcher() {
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
                if (mIsFromGeust) {
                    String userText = mUserName.getText().toString();
                    String geustPassword = mUserPassword.getText().toString();
                    String gesutEmail = mUserEmail.getText().toString();
                    mGeustUser.setName(userText);
                    mGeustUser.setPassword(geustPassword);
                    mGeustUser.setEmail(gesutEmail);
                    mGeustUser.setId(mUserId);

                    UserRepository.getInstance(getActivity()).updateUser(mGeustUser);
//                    sendIntent(mUserId);
                    dismiss();
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                } else {

                    long id = UserRepository.getInstance(getActivity()).createUser(mUser);
                    if (id == -1)
                        Toast.makeText(getActivity(), "this user name already exist", Toast.LENGTH_SHORT).show();
                    else {

                        sendIntent(id);
                        dismiss();
                    }
                }

            }
        });


        return view;
    }

    private void sendIntent(long id) {
        Intent sendIntent = MainViewPagerActivity.newIntent(getActivity(), id);
        startActivity(sendIntent);
    }

}
