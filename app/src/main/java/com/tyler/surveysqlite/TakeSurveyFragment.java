package com.tyler.surveysqlite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class TakeSurveyFragment extends Fragment {
    private Survey mSurvey;

    private TextView mQuestionTextView;
    private Button mFirstResponseButton;
    private Button mSecondResponseButton;
    private TextView mFirstScoreTextView;
    private TextView mSecondScoreTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID surveyId = (UUID) getActivity().getIntent()
                .getSerializableExtra(TakeSurveyActivity.EXTRA_SURVEY_ID);
        mSurvey = SurveyLab.get(getActivity()).getSurvey(surveyId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_survey, container, false);

        final int score;


        mQuestionTextView = (TextView) view.findViewById(R.id.survey_question);
        mQuestionTextView.setText(mSurvey.getQuestion());

        mFirstScoreTextView = (TextView) view.findViewById(R.id.survey_first_score);
        mFirstScoreTextView.setText(String.valueOf(mSurvey.getFirst_score()));

        mSecondScoreTextView = (TextView) view.findViewById(R.id.survey_second_score);
        mSecondScoreTextView.setText(String.valueOf(mSurvey.getSecond_score()));

        mFirstResponseButton = (Button) view.findViewById(R.id.survey_first_response);
        mFirstResponseButton.setText(mSurvey.getFirst_response());
        mFirstResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "First Response +1", Toast.LENGTH_SHORT).show();
                mSurvey.setFirst_score(mSurvey.getFirst_score() + 1);
                mFirstScoreTextView.setText(String.valueOf(mSurvey.getFirst_score()));
                SurveyLab.get(getActivity()).updateSurvey(mSurvey);
            }
        });

        mSecondResponseButton = (Button) view.findViewById(R.id.survey_second_response);
        mSecondResponseButton.setText(mSurvey.getSecond_response());
        mSecondResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Second Response +1", Toast.LENGTH_SHORT).show();
                mSurvey.setSecond_score(mSurvey.getSecond_score() + 1);
                mSecondScoreTextView.setText(String.valueOf(mSurvey.getSecond_score()));
                SurveyLab.get(getActivity()).updateSurvey(mSurvey);
            }
        });

        return view;
    }



}
