package project.com.maktab.hw_6;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrudTaskActivity extends AppCompatActivity {
    private static final String ID_EXTRA = "project.com.maktab.hw_6.id_extra";
    private static final String HOME_STAUS_EXTRA = "project.com.maktab.hw_6.id.home_status_extra";
    private boolean mFromAddButton = false;


    public static Intent newIntent(Context context, UUID id, boolean status) {
        Intent intent = new Intent(context, CrudTaskActivity.class);
        intent.putExtra(ID_EXTRA, id);
        intent.putExtra(HOME_STAUS_EXTRA, status);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_task);
        mFromAddButton = false;
        mFromAddButton = getIntent().getBooleanExtra(HOME_STAUS_EXTRA, false);


        UUID id = (UUID) getIntent().getSerializableExtra(ID_EXTRA);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mFromAddButton) {
            if (fragmentManager.findFragmentById(R.id.crud_fragment_container) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.crud_fragment_container, CrudTaskFragment.getInstance(id, true))
                        .commit();
            }


        } else {
            if (fragmentManager.findFragmentById(R.id.crud_fragment_container) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.crud_fragment_container, CrudTaskFragment.getInstance(id, false))

                        .commit();
            }
        }
    }

}

