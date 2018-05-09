package com.tyler.inspirationintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.UUID;

// Fragment class that either displays a empty form for user input
// or a pre-populated form based on an Inspiration Object that can be edited
public class InspirationFragment extends Fragment {

    private static final String ARG_INSPIRATION_ID = "inspiration_id";

    private static final int REQUEST_PHOTO = 0;

    private Inspiration mInspiration;
    private File mPhotoFile;
    private ImageButton mImageButton;
    private EditText mNoteField;
    private ImageView mImageView;

    // Method that handles retreiving a new fragment with an Inspiration object
    public static InspirationFragment newInstance(UUID inspirationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INSPIRATION_ID, inspirationId);

        InspirationFragment fragment = new InspirationFragment();
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
        View v = inflater.inflate(R.layout.fragment_inspiration, container, false);

        mImageButton = (ImageButton) v.findViewById(R.id.inspiration_image_button);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mImageButton.setEnabled(canTakePhoto);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.tyler.inspirationintent.fileprovider",
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

        mNoteField = (EditText) v.findViewById(R.id.edit_inspiration_note);
        mNoteField.setText(mInspiration.getNote());
        mNoteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInspiration.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImageView = (ImageView) v.findViewById(R.id.survey_full_pic);
        updatePhotoView();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_inspiration, menu);

    }

    // Deleting an Inspiration from the database in the Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_survey:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            InspirationLab.get(getActivity()).deleteInspiration(mInspiration);
                            Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), InspirationListActivity.class);
                            startActivity(intent);
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.are_you_sure)
                        .setPositiveButton(getString(android.R.string.ok), dialogClickListener)
                        .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("HELP", "NOTE: " + mInspiration.getNote());
        InspirationLab.get(getActivity()).updateInspiration(mInspiration);

    }

    private void updatePhotoView() {
        if (mPhotoFile != null && mPhotoFile.exists()) {
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
                    "com.tyler.inspirationintent.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }
}
