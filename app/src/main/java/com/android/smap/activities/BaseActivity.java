package com.android.smap.activities;

import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.smap.R;

public abstract class BaseActivity extends FragmentActivity {

	public void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	protected boolean isOrientationForced() {
		return true;
	}



	// for maintaining a fragment stack that all need to know about the result
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		List<Fragment> fragmentList = getSupportFragmentManager()
				.getFragments();
		for (Fragment fragment : fragmentList) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

}
