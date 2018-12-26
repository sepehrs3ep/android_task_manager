package project.com.maktab.hw_6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import project.com.maktab.hw_6.model.Task;
import project.com.maktab.hw_6.model.TaskRepository;

public class MainViewPagerActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mFloatingActionButton = findViewById(R.id.float_button_add);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                TaskRepository.getInstance().addTask(task);
                Intent intent = CrudTaskActivity.newIntent(MainViewPagerActivity.this, task.getID(), true);
                startActivity(intent);
            }
        });
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0) return TaskListFragment.getInstance(-1);
                if (i == 1) return TaskListFragment.getInstance(1);
                if (i == 2) return TaskListFragment.getInstance(0);
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) return "Undone List";
                if (position == 1) return "Done List";
                if (position == 2) return "All List";
                return "";
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
