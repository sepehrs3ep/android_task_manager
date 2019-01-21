package project.com.maktab.hw_6.controller.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.hw_6.DbBitmapUtility;
import project.com.maktab.hw_6.MyDialogCloseListener;
import project.com.maktab.hw_6.PictureUtils;
import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.CrudTaskFragment;
import project.com.maktab.hw_6.controller.fragment.LoginFragment;
import project.com.maktab.hw_6.controller.fragment.ShowUserFragment;
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
    private User mUser;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;


    private FragmentStatePagerAdapter mViewPagerAdapter;
    public static final String USER_ID_EXTRA = "project.com.maktab.hw_6.controller.activity.user_id_extra";

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, MainViewPagerActivity.class);
        intent.putExtra(USER_ID_EXTRA, userId);
        return intent;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_view_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.logout_menu_item:


                if (LoginFragment.IS_GUEST) {

                    guestExit();
                } else {
                    AlertDialog exitDialog = new AlertDialog.Builder(MainViewPagerActivity.this)
                            .setTitle("are you sure you want to log out of your account ? ")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                                    editor.putBoolean(SignUpDialogFragment.ALREADY_SIGN_IN, false);
                                    editor.commit();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .create();
                    exitDialog.show();

                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mUserId = getIntent().getLongExtra(USER_ID_EXTRA, 1);
        mUser = UserRepository.getInstance(MainViewPagerActivity.this).getUser(mUserId);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_toggle, R.string.close_toggle);
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationView = findViewById(R.id.nav_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.show_my_profile:
                        ShowUserFragment fragment = ShowUserFragment.newInstance(mUserId);
                        fragment.show(getSupportFragmentManager(), "showMyProfile");
                        return true;
                    case R.id.delete_account:
                        AlertDialog deleteDialog = new AlertDialog.Builder(MainViewPagerActivity.this)
                                .setTitle("Are you sure you want to delete your account?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UserRepository.getInstance(MainViewPagerActivity.this).deleteAccount(mUserId);
                                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                                        editor.putBoolean(SignUpDialogFragment.ALREADY_SIGN_IN, false);
                                        editor.commit();
                                        finishAffinity();
                                    }
                                })
                                .create();
                        deleteDialog.show();
                        return true;
                    default:
                        return true;

                }
            }
        });

        View headerViewName = mNavigationView.getHeaderView(0);
        /*TextView userTextView = headerViewName.findViewById(R.id.drawer_user_name);
        userTextView.setText("User Name :  " + mUser.getName());
        TextView emailTextView = headerViewName.findViewById(R.id.drawer_email);
        emailTextView.setText("User Email : " + mUser.getEmail());
        TextView userIdTextView = headerViewName.findViewById(R.id.drawer_uuid);
        userIdTextView.setText("User Id : " + mUser.getUserUUID().toString())*/
        ;
        if (!LoginFragment.IS_GUEST) {
            Bitmap selectedImage = null;
            String imagePath = mUser.getImage();
            Uri imageUri = Uri.parse(imagePath);
            CircleImageView photoImageView = headerViewName.findViewById(R.id.user_profile_image);
            try {
                selectedImage = PictureUtils.decodeUri(MainViewPagerActivity.this,imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


//        Bitmap bitmap = PictureUtils.getScaledBitmap(mUser.getImage(),MainViewPagerActivity.this);
            photoImageView.setImageBitmap(selectedImage);
       /* Bitmap bitmap = DbBitmapUtility.getImage(mUser.getImage());
        photoImageView.setImageBitmap(bitmap);*/
        }


        final FloatingActionButton floatingActionButton = findViewById(R.id.float_button_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Task task = new Task();
                task.setTaskType("undone");
                task.setUserID(mUserId);
                TaskRepository.getInstance(MainViewPagerActivity.this).addTask(task);*/
                /*
                Intent intent = CrudTaskActivity.newIntent(MainViewPagerActivity.this, task.getID(), true);
                startActivity(intent);*/

                CrudTaskFragment fragment = CrudTaskFragment.getInstance(null, true, mUserId);
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

        if (LoginFragment.IS_GUEST) {
            guestExit();
        } else
//            finishAffinity();
            finishAffinity();
    }

    private void guestExit() {
        AlertDialog dialog = new AlertDialog.Builder(MainViewPagerActivity.this)
                .setTitle("Don't want to create account ? your information will delete if you don't create account")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SignUpDialogFragment fragment = SignUpDialogFragment.newInstance(mUserId, true);
                        fragment.show(getSupportFragmentManager(), "geustUser");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserRepository.getInstance(MainViewPagerActivity.this).deleteAccount(mUserId);
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

  /*  public void onBackPressedd() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }*/
}