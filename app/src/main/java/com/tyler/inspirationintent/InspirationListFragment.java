package com.tyler.inspirationintent;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.tyler.inspirationintent.database.InspirationDbSchema.InspirationTable;

import java.io.File;
import java.util.List;

//Reads in data from database and pictures from storage and displays them in a RecyclerView
public class InspirationListFragment extends Fragment {

    private static final int DISPLAY_REQUEST_CODE = 0;


    private RecyclerView mSurveyRecyclerView;
    private SurveyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspiration_list, container, false);
        mSurveyRecyclerView = (RecyclerView) view
                .findViewById(R.id.survey_recycler_view);
        mSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Menu with two items to search and add a new inspiration
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_inspiration_list, menu);

        //https://stackoverflow.com/questions/35802924/android-searchview-setonquerytextlistener-not-working
        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("HELP", "search: " + newText);
                updateUI(newText);
                return false;
            }
        });

    }

    // Search the database for hashtags and text
    private void updateUI(String search) {

        String search_hashtag = "#" + search.replace(" ", "");
        search_hashtag = "%" + search_hashtag + "%";
        search = "%" + search.trim() + "%";
        Log.d("HELP", "Hashtag: " + search_hashtag);
        String where = InspirationTable.Cols.NOTE + " LIKE ?";
        String[] whereArg = { search };
        String[] whereHashtagArg = { search_hashtag };
        List<Inspiration> inspirations = InspirationLab.get(getActivity())
                .getInspirations(where, whereArg);
        inspirations.addAll(InspirationLab.get(getActivity()).getInspirations(where, whereHashtagArg));
        Log.d("HELP", "List: " + inspirations + "Search: " + search + " ");
        mAdapter.setInspirations(inspirations);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_survey:
                Inspiration inspiration = new Inspiration();
                InspirationLab.get(getActivity()).addInspiration(inspiration);
                Intent intent = InspirationActivity
                        .newIntent(getActivity(), inspiration.getId());
                startActivityForResult(intent, DISPLAY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Add click listeners to each widget in the listview to display them on their own
    private class SurveyHolder extends RecyclerView.ViewHolder {

        private Inspiration mInspiration;
        private TextView mNoteTextView;
        private ImageView mInspirationPhoto;
        private File mPhotoFile;

        public SurveyHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_inspiration, parent, false));

            mNoteTextView = (TextView) itemView.findViewById(R.id.inspiration_note);
            mNoteTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = InspirationNoteActivity.newIntent(getActivity(), mInspiration.getId());
                    startActivity(intent);
                }
            });
            mNoteTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = InspirationActivity.newIntent(getActivity(), mInspiration.getId());
                    startActivity(intent);
                    return true;
                }
            });


            mInspirationPhoto = (ImageView) itemView.findViewById(R.id.inspiration_picture);
            mInspirationPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = InspirationPhotoActivity.newIntent(getActivity(), mInspiration.getId());
                    startActivity(intent);
                }
            });
            mInspirationPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = InspirationActivity.newIntent(getActivity(), mInspiration.getId());
                    startActivity(intent);
                    return true;
                }
            });

        }

        //Populate listviews with data
        public void bind(Inspiration inspiration) {
            mInspiration = inspiration;
            mPhotoFile = InspirationLab.get(getActivity()).getPhotoFile(inspiration);
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mInspirationPhoto.setImageResource(R.drawable.ic_photo_add);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitamp(
                        mPhotoFile.getPath(), getActivity());
                mInspirationPhoto.setImageBitmap(bitmap);
            }
            if (mInspiration.getNote() == null) {
                mNoteTextView.setText(R.string.hold_to_edit);
            } else if (mInspiration.getNote().length() == 0) {
                mNoteTextView.setText(R.string.hold_to_edit);
            } else {
                mNoteTextView.setText(mInspiration.getNote());
            }
        }

    }

    private class SurveyAdapter extends RecyclerView.Adapter<SurveyHolder> {

        private List<Inspiration> mInspirations;

        public SurveyAdapter(List<Inspiration> inspirations) {

            mInspirations = inspirations;
        }

        @Override
        public SurveyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new SurveyHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SurveyHolder holder, int position) {
            Inspiration inspiration = mInspirations.get(position);
            holder.bind(inspiration);
        }

        @Override
        public int getItemCount() {
            return mInspirations.size();
        }

        public void setInspirations(List<Inspiration> inspirations) {
            mInspirations = inspirations;
        }
    }

    //Call to repopulate ListView with all data in database
    private void updateUI() {
        InspirationLab inspirationLab = InspirationLab.get(getActivity());
        List<Inspiration> inspirations = inspirationLab.getInspirations();

        if (mAdapter == null) {
            mAdapter = new SurveyAdapter(inspirations);
            mSurveyRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setInspirations(inspirations);
            mAdapter.notifyDataSetChanged();
        }

    }


}
