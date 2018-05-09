package com.tyler.inspirationintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

// Fragment class that displays the text of a note
public class InspirationNoteFragment extends Fragment {

    private static final String ARG_INSPIRATION_ID = "inspiration_id";

    private Inspiration mInspiration;
    private TextView mNoteTextView;

    public static InspirationNoteFragment newInstance(UUID inspirationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INSPIRATION_ID, inspirationId);

        InspirationNoteFragment fragment = new InspirationNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID inspirationId = (UUID) getArguments().getSerializable(ARG_INSPIRATION_ID);
        setHasOptionsMenu(true);
        mInspiration = InspirationLab.get(getActivity()).getInspiration(inspirationId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inspiration_note, container, false);


        mNoteTextView = (TextView) v.findViewById(R.id.inspiration_note);
        mNoteTextView.setText(mInspiration.getNote());

        return v;
    }

}

