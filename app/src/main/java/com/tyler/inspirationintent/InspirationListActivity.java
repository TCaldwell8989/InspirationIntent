package com.tyler.inspirationintent;

import android.support.v4.app.Fragment;


//Start of App
public class InspirationListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new InspirationListFragment();
    }

}