package project.com.maktab.hw_6.controller.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.activity.MainViewPagerActivity;
import project.com.maktab.hw_6.controller.activity.SingleFragmentActivity;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private TextView mCreateAccountTv;
    private Button mSignInBtn;
    public static boolean IS_GEUST = false;
    private FloatingActionButton mFloatGeustBtn;
    private String mUserName = "";
    private String mPassword = "";
    private TextInputLayout mUserNameLayout, mPasswordLayout;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mUserNameEt = view.findViewById(R.id.login_user_name_et);
        mPasswordEt = view.findViewById(R.id.login_password_et);
        mSignInBtn = view.findViewById(R.id.login_sign_in_btn);
        mFloatGeustBtn = view.findViewById(R.id.floatingGeustButton);
        mUserNameLayout = view.findViewById(R.id.login_username_layout);
        mPasswordLayout = view.findViewById(R.id.sign_in_password_layout);
        mCreateAccountTv = view.findViewById(R.id.create_account_tv);


        SpannableString ss = new SpannableString(getString(R.string.create_account));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                SignUpDialogFragment fragment = SignUpDialogFragment.newInstance(0, false);
                fragment.show(getFragmentManager(), "signUp");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 24, getString(R.string.create_account).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCreateAccountTv.setText(ss);
        mCreateAccountTv.setMovementMethod(LinkMovementMethod.getInstance());
        mCreateAccountTv.setHighlightColor(Color.TRANSPARENT);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mUserNameEt.getText().toString();
                mPassword = mPasswordEt.getText().toString();
                if (!validateName())
                    return;

                if (!validatePassword())
                    return;


                long id = UserRepository.getInstance(getActivity()).login(mUserName, mPassword);
                if (id > 0)
                    sendIntent(id);
                else
                    Toast.makeText(getActivity(), "can't find your account", Toast.LENGTH_SHORT).show();
            }
        });

        mFloatGeustBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName("guest");
                IS_GEUST = true;
                user.setPassword("admin");
                long id = UserRepository.getInstance(getActivity()).createUser(user);
                sendIntent(id);
            }
        });

        return view;
    }

    private void sendIntent(long id) {
        Intent sendIntent = MainViewPagerActivity.newIntent(getActivity(), id);
        startActivity(sendIntent);
    }

    private boolean validatePassword() {
        if (mPasswordEt.getText().toString().trim().isEmpty()) {
            mPasswordLayout.setError("should not be empty!");
            requestFocus(mPasswordEt);
            return false;
        } else {
            mPasswordLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateName() {
        if (mUserNameEt.getText().toString().trim().isEmpty()) {
            mUserNameLayout.setError("should not be empty!");
            requestFocus(mUserNameEt);
            return false;
        } else {
            mUserNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
