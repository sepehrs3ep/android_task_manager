package project.com.maktab.hw_6.controller.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

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
    private TextView mCreateAccountTv, mForgetPasswordTv;
    private Button mSignInBtn;
    public static boolean IS_GUEST = false;
    private FloatingActionButton mFloatGuestBtn;
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
        mFloatGuestBtn = view.findViewById(R.id.floatingGeustButton);
        mUserNameLayout = view.findViewById(R.id.login_username_layout);
        mPasswordLayout = view.findViewById(R.id.sign_in_password_layout);
        mCreateAccountTv = view.findViewById(R.id.create_account_tv);
        mForgetPasswordTv = view.findViewById(R.id.forget_password);


        createAccount();


        SpannableString spannableForgetPass = new SpannableString(getString(R.string.forget_password));
        ClickableSpan clickableSpanForgetPass = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                ForgetPasswordFragment fragment = ForgetPasswordFragment.newInstance(mUserNameEt.getText().toString());
                fragment.show(getFragmentManager(), "forgetPass");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableForgetPass.setSpan(clickableSpanForgetPass, 30, getString(R.string.forget_password).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mForgetPasswordTv.setText(spannableForgetPass);
        mForgetPasswordTv.setMovementMethod(LinkMovementMethod.getInstance());
        mForgetPasswordTv.setHighlightColor(Color.TRANSPARENT);
        mForgetPasswordTv.setVisibility(View.GONE);


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
                else if (id == -2) {
                    Toast.makeText(getActivity(), "wrong password!", Toast.LENGTH_SHORT).show();
                    mForgetPasswordTv.setVisibility(View.VISIBLE);
                } else
                    Toast.makeText(getActivity(), "can't find your account", Toast.LENGTH_SHORT).show();

            }
        });

        mFloatGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName("guest");
                IS_GUEST = true;
                user.setPassword("admin");
                long id = UserRepository.getInstance(getActivity()).createUser(user);
                sendIntent(id);
            }
        });

        return view;
    }


    private void createAccount() {
        SpannableString spannableCreate = new SpannableString(getString(R.string.create_account));
        ClickableSpan clickableSpanCreateAccount = new ClickableSpan() {
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
        spannableCreate.setSpan(clickableSpanCreateAccount, 24, getString(R.string.create_account).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCreateAccountTv.setText(spannableCreate);
        mCreateAccountTv.setMovementMethod(LinkMovementMethod.getInstance());
        mCreateAccountTv.setHighlightColor(Color.TRANSPARENT);
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
