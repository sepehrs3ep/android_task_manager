package project.com.maktab.hw_6.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.UUID;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.CrudTaskFragment;

public class CrudTaskActivity extends AppCompatActivity {
    private static final String ID_EXTRA = "project.com.maktab.hw_6.id_extra";
    private static final String HOME_STATUS_EXTRA = "project.com.maktab.hw_6.id.home_status_extra";
    private UUID mCurrentId;
    private boolean mFromAddButton;


    public static Intent newIntent(Context context, UUID id, boolean status) {
        Intent intent = new Intent(context
                , CrudTaskActivity.class);
        intent.putExtra(ID_EXTRA, id);
        intent.putExtra(HOME_STATUS_EXTRA, status);
        return intent;
    }

 /*   @Override
    public Fragment createFragment() {
        CrudTaskFragment fragment = CrudTaskFragment.getInstance(mCurrentId,mFromAddButton);
        return fragment;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        mFromAddButton = getIntent().getBooleanExtra(HOME_STATUS_EXTRA, false);
        mCurrentId = (UUID) getIntent().getSerializableExtra(ID_EXTRA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CrudTaskFragment.getInstance(mCurrentId, mFromAddButton))
                .commit();

    }

/*    @Override
    public void onBackPressed() {
        if (CrudTaskFragment.IS_EMPTY)
            Snackbar.make(findViewById(android.R.id.content), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
        else {
            if (CrudTaskFragment.onBackPressed(CrudTaskActivity.this))
                Toast.makeText(this, "no item added", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }*/
}

