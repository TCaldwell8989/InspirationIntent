package com.tyler.surveysqlite;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class SurveyActivity extends SingleFragmentActivity {

    private static final String EXTRA_SURVEY_ID =
            "com.tyler.android.surveysqlite.survey_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, SurveyActivity.class);
        intent.putExtra(EXTRA_SURVEY_ID, crimeId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {

        UUID surveyId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_SURVEY_ID);
        return SurveyFragment.newInstance(surveyId);
    }
}
