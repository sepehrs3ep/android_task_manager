package project.com.maktab.hw_6.controller.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends DialogFragment {
    public static final String USER_NAME_ARGS = "userName";
    private String mUserName;
    private Button mSubmitBtn;
    private EditText mEditEmailEt;
    private long mUserId;
    private User mUser;



    public static ForgetPasswordFragment newInstance(String username) {

        Bundle args = new Bundle();
        args.putString(USER_NAME_ARGS,username);
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
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
        mUserName = getArguments().getString(USER_NAME_ARGS);
        mUserId = UserRepository.getInstance(getActivity()).getUserId(mUserName);
        mUser = UserRepository.getInstance(getActivity()).getUser(mUserId);
    }

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forget_password, container, false);
        mSubmitBtn = view.findViewById(R.id.confirm_submit_btn);
        mEditEmailEt = view.findViewById(R.id.confirm_email_et);
        mEditEmailEt.setText(mUser.getEmail());

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalEmail = mEditEmailEt.getText().toString();
                sendEmail(finalEmail);
            }
        });









        return view;
    }
    private void sendEmail(String email) {
        BackgroundMail.newBuilder(getActivity())
                .withUsername("todo.list.s3ep@gmail.com")
                .withPassword("todo1234")
                .withMailto(email)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(R.string.email_subject)
                .withBody(getString(R.string.email_body)+ mUser.getPassword())
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), R.string.success_send_email, Toast.LENGTH_SHORT).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(getActivity(), R.string.failed_send_email, Toast.LENGTH_SHORT).show();
                    }
                })
                .send();
    }
}
