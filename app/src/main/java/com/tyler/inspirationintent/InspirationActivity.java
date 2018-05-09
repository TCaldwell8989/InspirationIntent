package com.tyler.inspirationintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


public class InspirationActivity extends SingleFragmentActivity {

    private static final String EXTRA_INSPIRATION_ID =
            "com.tyler.android.inspirationintent.inspiration_id";

    public static Intent newIntent(Context packageContext, UUID surveyId) {
        Intent intent = new Intent(packageContext, InspirationActivity.class);
        intent.putExtra(EXTRA_INSPIRATION_ID, surveyId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {

        UUID surveyId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_INSPIRATION_ID);
        return InspirationFragment.newInstance(surveyId);
    }
}
