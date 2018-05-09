package com.tyler.inspirationintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class InspirationNoteActivity extends SingleFragmentActivity {

    private static final String EXTRA_SURVEY_ID =
            "com.tyler.android.inspirationintent.survey_id";

    public static Intent newIntent(Context packageContext, UUID inspirationId) {
        Intent intent = new Intent(packageContext, InspirationNoteActivity.class);
        intent.putExtra(EXTRA_SURVEY_ID, inspirationId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {

        UUID inspirationId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_SURVEY_ID);
        return InspirationNoteFragment.newInstance(inspirationId);
    }
}
