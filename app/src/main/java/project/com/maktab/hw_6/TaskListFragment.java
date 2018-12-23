package project.com.maktab.hw_6;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import project.com.maktab.hw_6.model.Task;
import project.com.maktab.hw_6.model.TaskRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private static final String ARGS_LIST_STATUS = "project.com.maktab.hw_6.args_list_status";
    private TaskAdapter mTaskAdapter;
    private boolean mListStatus;

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
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*true>> all list
        false >> doneList*/
        mListStatus = getArguments().getBoolean(ARGS_LIST_STATUS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        updateUI();
        return view;
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewImage;
        private Task mTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.item_title);
            mTextViewImage = itemView.findViewById(R.id.item_circle_view);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UUID id = mTask.getID();
                        Intent intent = CrudTaskActivity.newIntent(getActivity(), id);
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

        public void setTaskList(List<Task> taskList) {
            mTaskList = taskList;
        }

        public TaskAdapter(List<Task> tasks) {
            this.mTaskList = tasks;
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
            Task task = mTaskList.get(i);
            taskViewHolder.bind(task);

        }

        @Override
        public int getItemCount() {
            return mTaskList.size();
        }
    }


}
