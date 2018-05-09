package com.tyler.inspirationintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class InspirationPhotoActivity extends SingleFragmentActivity {

    private static final String EXTRA_INSPIRATION_ID =
            "com.tyler.android.inspirationintent.inspiration_id";

    public static Intent newIntent(Context packageContext, UUID inspirationId) {
        Intent intent = new Intent(packageContext, InspirationPhotoActivity.class);
        intent.putExtra(EXTRA_INSPIRATION_ID, inspirationId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {

        UUID inspirationId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_INSPIRATION_ID);
        return InspirationPhotoFragment.newInstance(inspirationId);
    }
}
