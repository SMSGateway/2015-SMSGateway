package com.android.smap.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.android.smap.R;
import com.android.smap.fragments.BaseFragment;

public class FragmentContainerActivity extends BaseActivity {

    private static final String KEY_ARGUMENTS = "KEY_ARGUMENTS";
    private static final String KEY_FRAGMENT_NAME = "KEY_FRAGMENT_NAME";
    private static final String KEY_TITLE_ID = "KEY_TITLE_ID";
    private static final String KEY_TITLE = "KEY_TITLE";


    public static class Builder {

        private final Intent mIntent;


        public Builder(Context context, Class<?> cls) {
            mIntent = new Intent(context, FragmentContainerActivity.class);
            mIntent.putExtra(KEY_FRAGMENT_NAME, cls.getName());
        }

        public Builder title(int titleId) {
            mIntent.putExtra(KEY_TITLE_ID, titleId);
            return this;
        }

        public Builder title(String title) {
            mIntent.putExtra(KEY_TITLE, title);
            return this;
        }

        public Builder arguments(Bundle args) {
            mIntent.putExtra(KEY_ARGUMENTS, args);
            return this;
        }

        public Intent build() {
            return mIntent;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        setTitle();
        setFragment();

    }

    private void setTitle() {
        String title = "";

        int titleId = getIntent().getIntExtra(KEY_TITLE_ID, 0);

        if (titleId != 0) {
            title = getResources().getString(titleId);
        } else {
            title = getIntent().getStringExtra(KEY_TITLE);
        }

        if (!TextUtils.isEmpty(title)) {
            getActionBar().setTitle(title);
        }
    }

    private void setFragment() {

        String fragmentName = getIntent().getStringExtra(KEY_FRAGMENT_NAME);
        BaseFragment fragment = (BaseFragment) Fragment.instantiate(this, fragmentName);
        fragment.setArguments(getIntent().getBundleExtra(KEY_ARGUMENTS));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment);
        ft.commit();
    }


}
