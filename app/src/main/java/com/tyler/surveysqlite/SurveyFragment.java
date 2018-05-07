package com.tyler.surveysqlite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

public class SurveyFragment extends Fragment {

    private static final String ARG_SURVEY_ID = "survey_id";

    private Survey mSurvey;
    private EditText mQuestionField;
    private EditText mFirstResponseField;
    private EditText mSecondResponseField;

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

        mQuestionField = (EditText) v.findViewById(R.id.survey_question);
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

        return v;
    }
}
