package project.com.maktab.hw_6.controller.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.paperdb.Paper;
import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.TaskListFragment;
import project.com.maktab.hw_6.model.Task;
import project.com.maktab.hw_6.model.TaskRepository;
import project.com.maktab.hw_6.model.TaskType;

public class MainViewPagerActivity extends AppCompatActivity {
    TaskListFragment[] mListFragments = new TaskListFragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        FloatingActionButton floatingActionButton = findViewById(R.id.float_button_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setTaskType(TaskType.UNDONE);
                TaskRepository.getInstance().addTask(task);
                Intent intent = CrudTaskActivity.newIntent(MainViewPagerActivity.this, task.getID(), true);
                startActivity(intent);
            }
        });
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                TaskListFragment taskListFragment = null;
                if (i == 0) {
                    taskListFragment = TaskListFragment.getInstance(TaskType.UNDONE);
                    mListFragments[0] = taskListFragment;
                }
                if (i == 1) {
                    taskListFragment = TaskListFragment.getInstance(TaskType.DONE);
                    mListFragments[1] = taskListFragment;
                }
                if (i == 2) {
                    taskListFragment = TaskListFragment.getInstance(TaskType.ALL);
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
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    mListFragments[i].updateSubtitle();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }
}
