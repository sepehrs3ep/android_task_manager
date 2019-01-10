package project.com.maktab.hw_6.controller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowUserFragment extends DialogFragment {
    private TextView mUserName;
    private TextView mUserEmail;
    private TextView mUserId;
    private TextView mUserDate;
    public static final String USER_ID_ARGS = "userIdArgs";
    private long mId;
    private User mUser;

    public static ShowUserFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong(USER_ID_ARGS, id);
        ShowUserFragment fragment = new ShowUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ShowUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getLong(USER_ID_ARGS);
        mUser = UserRepository.getInstance(getActivity()).getUser(mId);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_user, container, false);
        mUserName = view.findViewById(R.id.show_user_name_tv);
        mUserEmail = view.findViewById(R.id.show_user_email);
        mUserId = view.findViewById(R.id.show_user_id);
        mUserDate = view.findViewById(R.id.show_user_date);

        mUserName.setText(mUser.getName());
        mUserEmail.setText(mUser.getEmail());
        mUserId.setText(mUser.getUserUUID().toString());
        setDateTextView();


        return view;
    }

    private void setDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,E");
        String dateOutput = dateFormat.format(mUser.getUserDate());
        mUserDate.setText(dateOutput);
    }

}
