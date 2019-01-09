package project.com.maktab.hw_6.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.LoginFragment;
import project.com.maktab.hw_6.controller.fragment.SignUpDialogFragment;

public class SingleFragmentActivity extends AppCompatActivity {
    public  Fragment createFragment(){
        LoginFragment fragment = LoginFragment.newInstance();
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        boolean status = preferences.getBoolean(SignUpDialogFragment.ALREADY_SIGN_IN,false);
        if(status){
            long id = preferences.getLong(SignUpDialogFragment.SIGN_IN_USER_ID,-1);
            Intent intent = MainViewPagerActivity.newIntent(this,id);
            startActivity(intent);

        }
        else {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.single_activity_fragment_container, createFragment())
                .commit();

        }

    }
}
