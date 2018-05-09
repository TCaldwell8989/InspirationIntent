package com.tyler.inspirationintent;

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

// Fragment class that displays the image of an Inspiration
public class InspirationPhotoFragment extends Fragment {

    private static final String ARG_INSPIRATION_ID = "inspiration_id";

    private static final int REQUEST_PHOTO = 0;

    private Inspiration mInspiration;
    private File mPhotoFile;
    private ImageView mPhotoView;

    public static InspirationPhotoFragment newInstance(UUID inspirationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INSPIRATION_ID, inspirationId);

        InspirationPhotoFragment fragment = new InspirationPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID inspirationId = (UUID) getArguments().getSerializable(ARG_INSPIRATION_ID);
        setHasOptionsMenu(true);
        mInspiration = InspirationLab.get(getActivity()).getInspiration(inspirationId);
        mPhotoFile = InspirationLab.get(getActivity()).getPhotoFile(mInspiration);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inspiration_photo, container, false);


        mPhotoView = (ImageView) v.findViewById(R.id.survey_full_pic);
        updatePhotoView();

        return v;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageResource(R.drawable.ic_photo_add);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitamp(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
