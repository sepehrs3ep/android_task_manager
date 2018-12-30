package project.com.maktab.hw_6.controller.activity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.fragment.TaskListFragment;
import project.com.maktab.hw_6.model.Task;
import project.com.maktab.hw_6.model.TaskRepository;

public class MainViewPagerActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatingActionButton;
    public static final String PAPER_TASK_LIST = "paper_task_list";
    TaskListFragment[] mListFragments=new TaskListFragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);
        Paper.init(MainViewPagerActivity.this);
        if (Paper.book().contains(PAPER_TASK_LIST)) {

            List<Task> list = Paper.book().read(PAPER_TASK_LIST);
            TaskRepository.getInstance().getList().clear();
            TaskRepository.getInstance().setTaskList(list);
        }

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
            private TaskListFragment mCurrentFragment;
            public TaskListFragment getCurrentFragment() {
                return mCurrentFragment;
            }
            @Override
            public Fragment getItem(int i) {
                TaskListFragment taskListFragment = null;
                if (i == 0){
                    taskListFragment = TaskListFragment.getInstance(-1);
                    mListFragments[0] = taskListFragment;
                }
                if (i == 1) {
                    taskListFragment = TaskListFragment.getInstance(1);
                    mListFragments[1] = taskListFragment;
                }
                if (i == 2) {
                    taskListFragment = TaskListFragment.getInstance(0);
                    mListFragments[2] = taskListFragment;
                }
                mCurrentFragment = taskListFragment;
                return taskListFragment;
            }

            @Override
            public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                if (getCurrentFragment() != object) {
                    mCurrentFragment = ((TaskListFragment) object);
                }
                super.setPrimaryItem(container, position, object);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) return "Undone";
                if (position == 1) return "Done";
                if (position == 2) return "All";
                return "";
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
            mListFragments[i].updateSubtitle();
//                getActionBar().setSubtitle("" + i );
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Paper.book().write(PAPER_TASK_LIST, TaskRepository.getInstance().getList());
    }

}
