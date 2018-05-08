package com.tyler.surveysqlite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SurveyFragment extends Fragment {

    private static final String ARG_SURVEY_ID = "survey_id";

    private static final int REQUEST_PHOTO = 0;

    private Survey mSurvey;
    private File mPhotoFile;
    private ImageButton mImageButton;
    private EditText mQuestionField;
    private EditText mFirstResponseField;
    private EditText mSecondResponseField;
    private ImageView mImageView;

    public static SurveyFragment newInstance(UUID surveyId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SURVEY_ID, surveyId);

        SurveyFragment fragment = new SurveyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID surveyId = (UUID) getArguments().getSerializable(ARG_SURVEY_ID);
        mSurvey = SurveyLab.get(getActivity()).getSurvey(surveyId);
        mPhotoFile = SurveyLab.get(getActivity()).getPhotoFile(mSurvey);
    }

    @Override
    public void onPause() {
        super.onPause();
        SurveyLab.get(getActivity()).updateSurvey(mSurvey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey, container, false);

        mImageButton = (ImageButton) v.findViewById(R.id.survey_image_button);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mImageButton.setEnabled(canTakePhoto);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.tyler.surveysqlite.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mQuestionField = (EditText) v.findViewById(R.id.survey_question);
        mQuestionField.setText(mSurvey.getQuestion());
        mQuestionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mSurvey.setQuestion(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mFirstResponseField = (EditText) v.findViewById(R.id.survey_first_response);
        mFirstResponseField.setText(mSurvey.getFirst_response());
        mFirstResponseField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mSurvey.setFirst_response(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSecondResponseField = (EditText) v.findViewById(R.id.survey_second_response);
        mSecondResponseField.setText(mSurvey.getSecond_response());
        mSecondResponseField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mSurvey.setSecond_response(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mImageView = (ImageView) v.findViewById(R.id.survey_full_pic);
        updatePhotoView();

        return v;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageView.setImageResource(R.drawable.ic_photo_add);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitamp(
                    mPhotoFile.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.tyler.surveysqlite.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }
}
