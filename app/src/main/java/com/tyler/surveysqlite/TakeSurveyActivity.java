package com.tyler.surveysqlite;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class TakeSurveyActivity extends SingleFragmentActivity {

    public static final String EXTRA_SURVEY_ID =
            "com.tyler.android.surveysqlite.survey_id";

    public static Intent newIntent(Context packageContext, UUID surveyId) {
        Intent intent = new Intent(packageContext, TakeSurveyActivity.class);
        intent.putExtra(EXTRA_SURVEY_ID, surveyId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new TakeSurveyFragment();
    }
}
