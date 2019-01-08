package project.com.maktab.hw_6.controller.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import project.com.maktab.hw_6.MyDialogCloseListener;
import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.CrudTaskFragment;
import project.com.maktab.hw_6.controller.fragment.LoginFragment;
import project.com.maktab.hw_6.controller.fragment.SignUpDialogFragment;
import project.com.maktab.hw_6.controller.fragment.TaskListFragment;
import project.com.maktab.hw_6.model.task.Task;
import project.com.maktab.hw_6.model.task.TaskRepository;
import project.com.maktab.hw_6.model.task.TaskType;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

public class MainViewPagerActivity extends AppCompatActivity implements MyDialogCloseListener {
    static TaskListFragment[] mListFragments = new TaskListFragment[3];
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private long mUserId;
    int mposition;

    private FragmentStatePagerAdapter mViewPagerAdapter;
    public static final String USER_ID_EXTRA = "project.com.maktab.hw_6.controller.activity.user_id_extra";

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, MainViewPagerActivity.class);
        intent.putExtra(USER_ID_EXTRA, userId);
        return intent;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mUserId = getIntent().getLongExtra(USER_ID_EXTRA, 1);

        FloatingActionButton floatingActionButton = findViewById(R.id.float_button_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setTaskType("undone");
                task.setUserID(mUserId);
                TaskRepository.getInstance(MainViewPagerActivity.this).addTask(task);
                /*
                Intent intent = CrudTaskActivity.newIntent(MainViewPagerActivity.this, task.getID(), true);
                startActivity(intent);*/

                CrudTaskFragment fragment = CrudTaskFragment.getInstance(task.getID(), true);
                fragment.show(getSupportFragmentManager(), "add Task");

            }
        });

        mViewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

/*        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
              *//*  if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    mListFragments[i].updateSubtitle();*//*
//              mViewPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                mViewPagerAdapter.notifyDataSetChanged();
            }
        });*/
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        mViewPagerAdapter.notifyDataSetChanged();

    }

    public class viewPagerAdapter extends FragmentStatePagerAdapter {

        public viewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            TaskListFragment taskListFragment = null;
            if (i == 0) {
                taskListFragment = TaskListFragment.getInstance(TaskType.UNDONE, mUserId);
                mListFragments[0] = taskListFragment;
            }
            if (i == 1) {
                taskListFragment = TaskListFragment.getInstance(TaskType.DONE, mUserId);
                mListFragments[1] = taskListFragment;
            }
            if (i == 2) {
                taskListFragment = TaskListFragment.getInstance(TaskType.ALL, mUserId);
                mListFragments[2] = taskListFragment;
            }
            return taskListFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return getString(R.string.undone_task);
            if (position == 1) return getString(R.string.done_task);
            if (position == 2) return getString(R.string.all_task);
            return "";
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
           /* TaskListFragment f = (TaskListFragment ) object;
            if (f != null) {
                f.update();
            }
            return super.getItemPosition(object);*/
        }
    }


    @Override
    public void onBackPressed() {
//        User user = UserRepository.getInstance(MainViewPagerActivity.this).getUser(mUserId);
        if(LoginFragment.IS_GEUST){
            AlertDialog dialog = new AlertDialog.Builder(MainViewPagerActivity.this)
                    .setTitle("Don't want to create account ? your information will delete if you don't create account")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignUpDialogFragment fragment  = SignUpDialogFragment.newInstance(mUserId,true);
                            fragment.show(getSupportFragmentManager(),"geustUser");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserRepository.getInstance(MainViewPagerActivity.this).deleteAccount(mUserId);
                        }
                    })
                    .create();
            dialog.show();




        }else
        super.onBackPressed();



    }
}