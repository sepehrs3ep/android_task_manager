package project.com.maktab.hw_6.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.activity.CrudTaskActivity;
import project.com.maktab.hw_6.model.task.Task;
import project.com.maktab.hw_6.model.task.TaskRepository;
import project.com.maktab.hw_6.model.task.TaskType;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {
    private static final String SUBTITLE_STATUS = "subtitleStatus";
    private RecyclerView mRecyclerView;
    private ImageView mNoTaskImageView;
    private static final String ARGS_LIST_TYPE = "args_list_type";
    private TaskAdapter mTaskAdapter;
    private int mListType;
    private boolean mClickedShowSub = false;


    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment getInstance(int listType) {
        Bundle args = new Bundle();
        args.putInt(ARGS_LIST_TYPE, listType);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        updateSubtitle();
    }

    private void updateUI() {
        List<Task> list = getTaskList();

        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(list);
            mRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.setTaskList(list);
            mTaskAdapter.notifyDataSetChanged();
//            updateSubtitle();
        }
        if (list == null || list.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoTaskImageView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoTaskImageView.setVisibility(View.GONE);
        }
    }

    @Nullable
    private List<Task> getTaskList() {
        List<Task> list = null;
        if (mListType==1)
            list = TaskRepository.getInstance(getActivity()).getList();
        if (mListType==0)
            list = TaskRepository.getInstance(getActivity()).getDoneTaskList();
        if (mListType==-1)
            list = TaskRepository.getInstance(getActivity()).getUnDoneTaskList();
        return list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*true>> all list
        false >> doneList*/
        mListType = getArguments().getInt(ARGS_LIST_TYPE,1);
        setHasOptionsMenu(true);

        if (savedInstanceState != null)
            mClickedShowSub = savedInstanceState.getBoolean(SUBTITLE_STATUS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mNoTaskImageView = view.findViewById(R.id.no_task_image);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        updateUI();
//        updateSubtitle();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_list_fragment, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_subtitle_tasks);
        subtitleItem.setTitle(mClickedShowSub ? R.string.hide_subtitle : R.string.menu_subtitle);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mTaskAdapter.filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mTaskAdapter.filter(s);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_list_menu:
                AlertDialogFragment fragment = AlertDialogFragment.getInstance("are you sure you want delete all your lists?");
                fragment.show(getFragmentManager(), "menu_tag");
                fragment.setOnYesNoClick(new AlertDialogFragment.OnYesNoClick() {
                    @Override
                    public void onYesClicked() {
                        TaskRepository.getInstance(getActivity()).clearLists();
                        updateUI();

                    }

                    @Override
                    public void onNoClicked() {

                    }
                });
                return true;
            case R.id.menu_subtitle_tasks:
                mClickedShowSub = !mClickedShowSub;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void updateSubtitle() {
        List<Task> list = getTaskList();
        int count = list.size();
        String subtitleText;
        if (count == 0)
            subtitleText = getString(R.string.zero_task);
        else
            subtitleText = getResources().getQuantityString(R.plurals.subtitle, count, count);
        if (!mClickedShowSub)
            subtitleText = null;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitleText);
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewImage;
        private ImageView mImageViewDone;
        private ImageView mImageViewUndone;
        private Task mTask;

        public TaskViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.item_title);
            mTextViewImage = itemView.findViewById(R.id.item_circle_view);
            mImageViewDone = itemView.findViewById(R.id.done_image_view);
            mImageViewUndone = itemView.findViewById(R.id.undone_image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UUID id = mTask.getID();
                    Intent intent = CrudTaskActivity.newIntent(getActivity(), id, false);
                    startActivity(intent);
                }
            });
        }

        public void bind(Task task) {

            mTask = task;
            String taskTitleText = task.getTitle() == null ? "@" : task.getTitle();
            mTextViewTitle.setText(taskTitleText);
            mTextViewImage.setText(taskTitleText.charAt(0) + "");

            if (task.getTaskType().equals("done"))
                mImageViewUndone.setVisibility(View.GONE);
            else mImageViewDone.setVisibility(View.GONE);


        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
        private List<Task> mTaskList;
        private List<Task> mSearchTaskList;

        public void setTaskList(List<Task> taskList) {
            mTaskList = taskList;
            mSearchTaskList.clear();
            mSearchTaskList.addAll(mTaskList);
        }

        public TaskAdapter(List<Task> tasks) {
            this.mTaskList = tasks;
            mSearchTaskList = new ArrayList<>();
            mSearchTaskList.clear();
            mSearchTaskList.addAll(mTaskList);
        }


        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.task_list_item, viewGroup, false);
            TaskViewHolder taskViewHolder = new TaskViewHolder(view);
            return taskViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
            Task task = mSearchTaskList.get(i);
            if (task != null || task.getTitle() != null)
                taskViewHolder.bind(task);
            else TaskRepository.getInstance(getActivity()).removeTask(task.getID());

        }


        @Override
        public int getItemCount() {
            return mSearchTaskList.size();
        }

        public void filter(String text) {
            mSearchTaskList.clear();
            if (text.isEmpty()) {
                mSearchTaskList.addAll(mTaskList);
            } else {
                text = text.toLowerCase();
                for (Task item : mTaskList) {
                    if (item.getTitle().toLowerCase().contains(text)) {
                        mSearchTaskList.add(item);
                    }
                }
            }
            notifyDataSetChanged();


        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_STATUS, mClickedShowSub);
    }
}


