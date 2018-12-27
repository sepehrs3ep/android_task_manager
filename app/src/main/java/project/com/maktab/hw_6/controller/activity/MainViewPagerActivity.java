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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);
        Paper.init(MainViewPagerActivity.this);
        if (Paper.book().contains(PAPER_TASK_LIST)) {

            List<Task> list = Paper.book().read(PAPER_TASK_LIST);
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
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Paper.book().write(PAPER_TASK_LIST, TaskRepository.getInstance().getList());
    }
}
