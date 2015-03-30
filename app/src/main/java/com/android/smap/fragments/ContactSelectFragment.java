package com.android.smap.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.adapters.ContactSelectionAdapter;
import com.android.smap.api.models.Contact;
import com.android.smap.api.models.Distribution;
import com.android.smap.api.models.Survey;
import com.android.smap.di.DataManager;
import com.android.smap.ui.ViewQuery;
import com.google.inject.Inject;
import com.mjw.android.swipe.MultiChoiceSwipeListener;
import com.mjw.android.swipe.SwipeListView;

public class ContactSelectFragment extends BaseFragment {


    public static final String EXTRA_DISTRIBUTION_ID = ContactSelectFragment.class
															.getCanonicalName()
															+ "id";
	@Inject
	private DataManager				mDataManager;
	private List<Contact>			mModel;
	private Distribution            mDistribution;
	private ContactSelectionAdapter	mAdapter;
	private SwipeListView			mSwipeListView;
	private int						mDistributionId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		if (b != null) {
            mDistributionId = b.getInt(EXTRA_DISTRIBUTION_ID);
		}

	}

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {

		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.fragment_select_contacts,
				null);

		ViewQuery query = new ViewQuery(view);
		mSwipeListView = (SwipeListView) query.find(R.id.list_contacts).get();

		// get all necessary local data
		mDataManager = GatewayApp.getDependencyContainer().getDataManager();
		mModel = mDataManager.getContacts();
		mDistribution = mDataManager.getDistribution(mDistributionId);
		setupList();

		// query.find(R.id.txt_completed_progress).text(completedProgress);

		return view;
	}

	private void setupList() {

		mAdapter = new ContactSelectionAdapter(getActivity(), mModel,
				mSwipeListView);
		mSwipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		mSwipeListView
				.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

					@Override
					public void onItemCheckedStateChanged(ActionMode mode,
							int position,
							long id, boolean checked) {
						mode.setTitle(String.format("Add (%d) Contacts",
								mSwipeListView.getCountSelected()));
					}

					@Override
					public boolean onActionItemClicked(ActionMode mode,
							MenuItem item) {
						switch (item.getItemId()) {
						case R.id.action_add:
							addContactsToSurvey();
							mode.finish();
							getActivity().onBackPressed();
							return true;
						default:
							return false;
						}

					}

					@Override
					public boolean onCreateActionMode(ActionMode mode,
							Menu menu) {
						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.menu_add, menu);
						return true;
					}

					@Override
					public void onDestroyActionMode(ActionMode mode) {
						mSwipeListView.unselectedChoiceStates();
					}

					@Override
					public boolean onPrepareActionMode(ActionMode mode,
							Menu menu) {
						return false;
					}
				});

		mSwipeListView.setSwipeListViewListener(new MultiChoiceSwipeListener(
				mAdapter));
		mSwipeListView.setAdapter(mAdapter);

	}

	private void addContactsToSurvey() {

		List<Integer> selected = mSwipeListView.getPositionsSelected();
		List<Contact> contacts = new ArrayList<Contact>();
		for (Integer i : selected) {
			contacts.add(mModel.get(i));
		}
		mDataManager.addContactsToDistribution(contacts, mDistribution);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = false;
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().onBackPressed();
			break;
		}
		return handled;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(EXTRA_DISTRIBUTION_ID, mDistributionId);
	}

}
