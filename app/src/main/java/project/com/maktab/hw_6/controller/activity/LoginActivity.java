package project.com.maktab.hw_6.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import project.com.maktab.hw_6.controller.fragment.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        LoginFragment fragment = LoginFragment.newInstance();
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
