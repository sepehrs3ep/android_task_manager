package project.com.maktab.hw_6;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrudTaskActivity extends AppCompatActivity {
    private static final String ID_EXTRA = "project.com.maktab.hw_6.id_extra";
    private static boolean mIsFirst = false;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CrudTaskActivity.class);
        mIsFirst = true;
        return intent;
    }

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = newIntent(context);
        intent.putExtra(ID_EXTRA, id);
        mIsFirst = false;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = UUID.randomUUID();
        setContentView(R.layout.activity_crud_task);
        if (!mIsFirst)
            id = (UUID) getIntent().getSerializableExtra(ID_EXTRA);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.crud_fragment_container) == null) {
            if (mIsFirst) {
                fragmentManager.beginTransaction()
                        .add(R.id.crud_fragment_container, CrudTaskFragment.getInstance())
                        .commit();
            } else {

                fragmentManager.beginTransaction()
                        .add(R.id.crud_fragment_container, CrudTaskFragment.getInstance(id))
                        .commit();
            }
        }
    }
}
