package project.com.maktab.hw_6.controller.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertDialogFragment extends DialogFragment {
    private static final String ARGS_DATA = "args_date";
    private OnYesNoClick mOnYesNoClick;
    private String mTitleReceived;

    public void setOnYesNoClick(OnYesNoClick onYesNoClick) {
        mOnYesNoClick = onYesNoClick;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleReceived = getArguments().getString(ARGS_DATA);
    }

    public static AlertDialogFragment getInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARGS_DATA, title);
        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(mTitleReceived)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnYesNoClick != null)
                            mOnYesNoClick.onNoClicked();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOnYesNoClick.onYesClicked();
                    }
                });
        Dialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;


    }

    public interface OnYesNoClick {
        void onYesClicked();

        void onNoClicked();
    }
}
