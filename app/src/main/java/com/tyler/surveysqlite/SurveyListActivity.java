package com.tyler.surveysqlite;

import android.support.v4.app.Fragment;

public class SurveyListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SurveyListFragment();
    }

}