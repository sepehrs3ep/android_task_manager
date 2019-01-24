package project.com.maktab.hw_6.controller.fragment;


import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.model.task.Task;

import project.com.maktab.hw_6.model.task.TaskRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {
    private static final String SUBTITLE_STATUS = "subtitleStatus";
    private RecyclerView mRecyclerView;
    private ImageView mNoTaskImageView;
    private static final String ARGS_LIST_TYPE = "args_list_type";
    private static final String USER_ID_ARGS = "user_id_args";
    private TaskAdapter mTaskAdapter;
    private Task.TaskType mListType;
    private long mUserId;
    private OnDataPass mOnDataPass;
    private boolean mClickedShowSub = false;

    private void shareTask(Task task) {

        Intent reportIntent = new Intent(Intent.ACTION_SEND);
        reportIntent.setType("text/plain");
        reportIntent.putExtra(Intent.EXTRA_TEXT, getTaskReport(task));
        reportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_report_subject));

        startActivity(Intent.createChooser(reportIntent, getString(R.string.send_report)));
    }

    public TaskListFragment() {
        // Required empty public constructor
    }

    private String getTaskReport(Task mTask) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd,E,HH-mm");
        String outputDate = dateFormater.format(mTask.getMDate());

        String description = mTask.getMDescription().trim().length() <= 0 ? getString(R.string.no_description) :
                getString(R.string.have_description, mTask.getMDescription());

        String report = getString(R.string.share_task_format
                , mTask.getMTitle(), mTask.getMTaskType().toString(), outputDate, mTask.getMUser().getMName(), description);


        return report;
    }

    public static TaskListFragment getInstance(Task.TaskType listType, long userId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_LIST_TYPE, listType);
        args.putLong(USER_ID_ARGS, userId);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        updateSubtitle();
//        ((MainViewPagerActivity) getContext()).setAdapter(selected);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDataPass = (OnDataPass) context;
    }

    public void updateUI() {
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
        if (mListType.equals(Task.TaskType.ALL))
            list = TaskRepository.getInstance(getActivity()).getList(mUserId);
        if (mListType.equals(Task.TaskType.DONE))
            list = TaskRepository.getInstance(getActivity()).getDoneTaskList(mUserId);
        if (mListType.equals(Task.TaskType.UNDONE))
            list = TaskRepository.getInstance(getActivity()).getUnDoneTaskList(mUserId);
        return list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*true>> all list
        false >> doneList*/
        mListType = (Task.TaskType) getArguments().getSerializable(ARGS_LIST_TYPE);
        mUserId = getArguments().getLong(USER_ID_ARGS);
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

    public interface OnDataPass {
        public void onDataPass();
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
                        TaskRepository.getInstance(getActivity()).clearLists(mUserId);
                        mOnDataPass.onDataPass();
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
        private ImageButton mButtonShareTask;
        private TextView mTextViewImage;
        private ImageView mImageViewDone;
        private ImageView mImageViewUndone;
        private Task mTask;

        public TaskViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.item_title);
            mTextViewImage = itemView.findViewById(R.id.item_circle_view);
            mImageViewDone = itemView.findViewById(R.id.done_image_view);
            mButtonShareTask = itemView.findViewById(R.id.share_btn);
            mImageViewUndone = itemView.findViewById(R.id.undone_image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UUID id = mTask.getMID();
                    /*Intent intent = CrudTaskActivity.newIntent(getActivity(), id, false);
                    startActivity(intent);*/
                    CrudShowDialogFragment fragment = CrudShowDialogFragment.newInstance(id);
                    fragment.show(getFragmentManager(), "showCrud");
                }
            });
            mButtonShareTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareTask(mTask);
                }
            });
        }

        public void bind(Task task) {

            mTask = task;
            String taskTitleText = task.getMTitle() == null ? "@" : task.getMTitle();
            mTextViewTitle.setText(taskTitleText);
            mTextViewImage.setText(taskTitleText.charAt(0) + "");

            if (task.getMTaskType().equals(Task.TaskType.DONE))
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
            if (task != null || task.getMTitle() != null)
                taskViewHolder.bind(task);
            else TaskRepository.getInstance(getActivity()).removeTask(task);

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
                    if (item.getMTitle().toLowerCase().contains(text)) {
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



