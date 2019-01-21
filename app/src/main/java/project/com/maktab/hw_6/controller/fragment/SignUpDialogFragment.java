package project.com.maktab.hw_6.controller.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.maktab.hw_6.DbBitmapUtility;
import project.com.maktab.hw_6.PictureUtils;
import project.com.maktab.hw_6.R;
import project.com.maktab.hw_6.controller.activity.MainViewPagerActivity;
import project.com.maktab.hw_6.model.user.User;
import project.com.maktab.hw_6.model.user.UserRepository;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpDialogFragment extends DialogFragment {
    public static final String ALREADY_SIGN_IN = "userSignedIn";
    public static final String SIGN_IN_USER_ID = "signInUserId";
    private static final int GET_FROM_GALLERY_REQ_CODE = 21;
    private EditText mUserNameEt;
    private TextInputEditText mUserPasswordEt;
    private EditText mUserEmailEt;
    private CircleImageView mCircleImageView;
    private Button mSignUpBtn;
    public static final String ID_ARGS = "id";
    public static final String IS_GUEST_ARGS = "isGuest";
    private long mUserId;
    private boolean mIsFromGuest;
    private TextInputLayout mPasswordLayout, mEmailLayout, mUsernameLayout;
    private User mUserGuest;
    private ImageButton mUploadPhotoIbtn;
    private SharedPreferences mSharedPreferences;
//    private Bitmap mBitmap;
    private User mCreateUser;

    public SignUpDialogFragment() {
        // Required empty public constructor
    }

    public static SignUpDialogFragment newInstance(long id, boolean isGuest) {

        Bundle args = new Bundle();
        args.putLong(ID_ARGS, id);
        args.putBoolean(IS_GUEST_ARGS, isGuest);
        SignUpDialogFragment fragment = new SignUpDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mCreateUser= new User();

        mUserId = getArguments().getLong(ID_ARGS);
        mIsFromGuest = getArguments().getBoolean(IS_GUEST_ARGS, false);
        if (mIsFromGuest)
            mUserGuest = UserRepository.getInstance(getActivity()).getUser(mUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_dialog, container, false);
        mUserNameEt = view.findViewById(R.id.sign_up_user_name_et);
        mUserPasswordEt = view.findViewById(R.id.sign_up_password_et);
        mUserEmailEt = view.findViewById(R.id.sign_up_email_et);
        mSignUpBtn = view.findViewById(R.id.sign_up_create_account_btn);
        mPasswordLayout = view.findViewById(R.id.sign_up_password_layout);
        mEmailLayout = view.findViewById(R.id.sign_up_email_layout);
        mUsernameLayout = view.findViewById(R.id.sign_up_user_name_layout);
        mUploadPhotoIbtn = view.findViewById(R.id.upload_image_btn);
        mCircleImageView = view.findViewById(R.id.sign_up_profile_image);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = mUserNameEt.getText().toString();
                String userPassword = mUserPasswordEt.getText().toString();
                String userEmail = mUserEmailEt.getText().toString();
                if (!validateName())
                    return;
                if (!validatePassword())
                    return;
                if (!validateEmail())
                    return;


                if (mIsFromGuest) {
                    provideGuestUser(userText, userPassword, userEmail);
                } else {
                    createNewUser(userText, userPassword, userEmail);
                }

            }
        });

        mUploadPhotoIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GET_FROM_GALLERY_REQ_CODE);
//                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY_REQ_CODE);
            }
        });



        return view;
    }

    private void createNewUser(String userText, String userPassword, String userEmail) {
        mCreateUser.setName(userText);
            mCreateUser.setPassword(userPassword);
        mCreateUser.setEmail(userEmail);
//        byte[] image = DbBitmapUtility.getBytes(mBitmap);
//        createUser.setImage(image);




        long id = UserRepository.getInstance(getActivity()).createUser(mCreateUser);
        if (id == -1)
            Toast.makeText(getActivity(), "this user name already exist", Toast.LENGTH_SHORT).show();
        else {
            writePref(id);
            sendIntent(id);
            dismiss();
        }
    }

    private void writePref(long id) {
        mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ALREADY_SIGN_IN,true);
        editor.putLong(SIGN_IN_USER_ID,id);
        editor.commit();
    }

    private void provideGuestUser(String userText, String userPassword, String userEmail) {
        mUserGuest.setName(userText);
        mUserGuest.setPassword(userPassword);
        mUserGuest.setEmail(userEmail);
        mUserGuest.setId(mUserId);
      /*  byte[] image = DbBitmapUtility.getBytes(mBitmap);
        mUserGuest.setImage(image);*/
        int result = UserRepository.getInstance(getActivity()).updateUser(mUserGuest);
        if (result == -1)
            Toast.makeText(getActivity(), "this user name already exist", Toast.LENGTH_SHORT).show();
        else {
            writePref(mUserId);
            dismiss();
            getActivity().finish();
            sendIntent(mUserId);
            LoginFragment.IS_GUEST = false;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendIntent(long id) {
        Intent sendIntent = MainViewPagerActivity.newIntent(getActivity(), id);
        startActivity(sendIntent);
    }

    private boolean validatePassword() {
        if (mUserPasswordEt.getText().toString().trim().isEmpty()) {
            mPasswordLayout.setError("should not be empty!");
            requestFocus(mUserPasswordEt);
            return false;

        } else if (mUserPasswordEt.getText().toString().trim().length() < 5) {
            mPasswordLayout.setError("password length should be more thant 5 characters");
            requestFocus(mUserPasswordEt);
            return false;
        } else {
            mPasswordLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (mUserNameEt.getText().toString().trim().isEmpty()) {
            mUsernameLayout.setError("should not be empty!");
            requestFocus(mUserNameEt);
            return false;
        } else {
            mUsernameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = mUserEmailEt.getText().toString().trim();

        if (email.isEmpty()) {
            mEmailLayout.setError("should not be empty!");
            requestFocus(mUserEmailEt);
            return false;
        } else if (!isValidEmail(email)) {
            mEmailLayout.setError("wrong email type found!");
            requestFocus(mUserEmailEt);
            return false;
        } else {
            mEmailLayout.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            String imagePath;
                Uri imageUri = data.getData();
                imagePath = imageUri.getPath();
                mCreateUser.setImage(imageUri.toString());

//            OutputStream outputStream = null;
            InputStream imageStream = null;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap userProfileBitmap;
                userProfileBitmap = PictureUtils.getScaledBitmap(imagePath,getActivity());
                mCircleImageView.setImageBitmap(selectedImage);
            try {
//                mPhotoFile.createNewFile();
//                outputStream = new FileOutputStream(mPhotoFile,false);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            mBitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);


            /*} catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }*/

        }else {
            Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY_REQ_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }*/
}
