package project.com.maktab.hw_6.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.UUID;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.CrudTaskFragment;

public class CrudTaskActivity extends AppCompatActivity {
    private static final String ID_EXTRA = "project.com.maktab.hw_6.id_extra";
    private static final String HOME_STAUS_EXTRA = "project.com.maktab.hw_6.id.home_status_extra";


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
        boolean fromAddButton = getIntent().getBooleanExtra(HOME_STAUS_EXTRA, false);

        UUID currentId = (UUID) getIntent().getSerializableExtra(ID_EXTRA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.crud_fragment_container, CrudTaskFragment.getInstance(currentId, fromAddButton))
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (CrudTaskFragment.IS_EMPTY)
            Snackbar.make(findViewById(android.R.id.content), R.string.title_warning, Snackbar.LENGTH_SHORT).show();
        else {
            if (CrudTaskFragment.onBackPressed())
                Toast.makeText(this, "no item added", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }
}

