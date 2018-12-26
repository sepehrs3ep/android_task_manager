package project.com.maktab.hw_6;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import project.com.maktab.hw_6.model.Task;
import project.com.maktab.hw_6.model.TaskRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ImageView mNoTaskImageView;
    private static final String ARGS_LIST_STATUS = "project.com.maktab.hw_6.args_list_status";
    private TaskAdapter mTaskAdapter;
    private boolean mListStatus;
    private int mpostion;


    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment getInstance(boolean listStatus) {
        Bundle args = new Bundle();
        args.putBoolean(ARGS_LIST_STATUS, listStatus);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Task> list;
        if (mListStatus)
            list = TaskRepository.getInstance().getAllList();
        else list = TaskRepository.getInstance().getDoneList();

        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(list);
            mRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.setTaskList(list);
            mTaskAdapter.notifyItemChanged(mpostion);
        }
        if (list == null || list.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoTaskImageView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoTaskImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*true>> all list
        false >> doneList*/
        setHasOptionsMenu(true);
        mListStatus = getArguments().getBoolean(ARGS_LIST_STATUS);

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
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_list_fragment, menu);

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


    private class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewImage;
        private Task mTask;

        public TaskViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.item_title);
            mTextViewImage = itemView.findViewById(R.id.item_circle_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UUID id = mTask.getID();
                    Intent intent = CrudTaskActivity.newIntent(getActivity(), id);
                    mpostion = getAdapterPosition();
                    startActivity(intent);
                }
            });
        }

        public void bind(Task task) {
            mTask = task;
            String imageText = "";
            String taskTitleText = task.getTitle();
            mTextViewTitle.setText(taskTitleText);
            imageText = taskTitleText.substring(0, 1);
            mTextViewImage.setText(imageText);
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
            taskViewHolder.bind(task);

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
}


