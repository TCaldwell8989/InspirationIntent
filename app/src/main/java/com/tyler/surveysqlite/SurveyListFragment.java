package com.tyler.surveysqlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class SurveyListFragment extends Fragment {

    private RecyclerView mSurveyRecyclerView;
    private SurveyAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_list, container, false);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_survey_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_survey:
                Survey survey = new Survey();
                SurveyLab.get(getActivity()).addSurvey(survey);
                Intent intent = SurveyActivity
                        .newIntent(getActivity(), survey.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        SurveyLab surveyLab = SurveyLab.get(getActivity());
        int surveyCount = surveyLab.getSurveys().size();
        String subtitle = getString(R.string.subtitle_format, surveyCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class SurveyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,  View.OnLongClickListener {

        private Survey mSurvey;
        private TextView mQuestionTextView;
        private ImageView mSurveyImageView;
        private File mPhotoFile;

        public SurveyHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_survey, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mQuestionTextView = (TextView) itemView.findViewById(R.id.survey_question);
            mSurveyImageView = (ImageView) itemView.findViewById(R.id.survey_picture);


        }

        public void bind(Survey survey) {
            mSurvey = survey;
            mPhotoFile = SurveyLab.get(getActivity()).getPhotoFile(survey);
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                mSurveyImageView.setImageResource(R.drawable.ic_photo_add);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitamp(
                        mPhotoFile.getPath(), getActivity());
                mSurveyImageView.setImageBitmap(bitmap);
            }
            mQuestionTextView.setText(mSurvey.getQuestion());

        }

        @Override
        public void onClick(View view) {
            Intent intent = SurveyActivity.newIntent(getActivity(), mSurvey.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            Intent intent = TakeSurveyActivity.newIntent(getActivity(), mSurvey.getId());
            startActivity(intent);
            return true;
        }

    }

    private class SurveyAdapter extends RecyclerView.Adapter<SurveyHolder> {

        private List<Survey> mSurveys;

        public SurveyAdapter(List<Survey> surveys) {

            mSurveys = surveys;
        }

        @Override
        public SurveyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new SurveyHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SurveyHolder holder, int position) {
            Survey survey = mSurveys.get(position);
            holder.bind(survey);
        }

        @Override
        public int getItemCount() {
            return mSurveys.size();
        }

        public void setSurveys(List<Survey> surveys) {
            mSurveys = surveys;
        }
    }

    private void updateUI() {
        SurveyLab surveyLab = SurveyLab.get(getActivity());
        List<Survey> surveys = surveyLab.getSurveys();

        if (mAdapter == null) {
            mAdapter = new SurveyAdapter(surveys);
            mSurveyRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSurveys(surveys);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }


}
