package com.android.smap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.activities.FragmentContainerActivity.Builder;
import com.android.smap.adapters.DistributionAdapter;
import com.android.smap.api.models.Distribution;
import com.android.smap.api.models.Survey;
import com.android.smap.di.DataManager;
import com.android.smap.ui.ViewQuery;
import com.google.inject.Inject;

public class SurveyDistributionsFragment extends BaseFragment implements
		OnItemClickListener {

    public static final String		EXTRA_SURVEY_ID	= DistributionDetailFragment.class
            .getCanonicalName()
            + "id";

	private Survey             mSurvey;

    @Inject
	private DataManager		mDataManager;



    // TODO - Create Distribution Adapter
    private DistributionAdapter mAdapter;

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.fragment_distributions,
				null);
        mDataManager = GatewayApp.getDependencyContainer().getDataManager();
        Bundle b = getArguments();
        if (b != null) {
            mSurvey = mDataManager.getSurvey(b.getLong(EXTRA_SURVEY_ID));
        }
        TextView textView = (TextView) view.findViewById(R.id.txt_survey_name);
        textView.setText(mSurvey.getName());
		ListView listView = (ListView) view.findViewById(R.id.list_distributions);
		//mDataManager = GatewayApp.getDependencyContainer().getDataManager();
        mAdapter = new DistributionAdapter(getActivity(), mSurvey.getDistributions());
		listView.setOnItemClickListener(this);
		listView.setAdapter(mAdapter);

        return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.setModel(mSurvey.getDistributions());
	}

	@Override
	public void onItemClick(AdapterView<?> av, View parent, int pos, long viewId) {
		Distribution distribution = mAdapter.getItem(pos);
		Bundle b = new Bundle();
		b.putLong(DistributionDetailFragment.EXTRA_DISTRIBUTION_ID, distribution.getId());

        startActivity(new Builder(getActivity(), DistributionDetailFragment.class)
				.arguments(b).title(R.string.ab_distribution_details).build());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.menu_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = true;
		switch (item.getItemId()) {
		case android.R.id.home: // Actionbar home/up icon
			getActivity().onBackPressed();
			break;
		case R.id.action_add:

            Bundle b = new Bundle();
            b.putLong(EXTRA_SURVEY_ID, mSurvey.getId());
            startActivity(new Builder(getActivity(), SurveyDistributionCreateFragment.class)
                    .arguments(b).title(R.string.ab_create_distribution).build());
			break;
		}
		return handled;
	}

}
