package com.android.smap.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.activities.FragmentContainerActivity;
import com.android.smap.adapters.DialogueAdapter;
import com.android.smap.api.models.Contact;
import com.android.smap.api.models.Dialogue;
import com.android.smap.api.models.Distribution;
import com.android.smap.api.services.SmsDialogueHandler;
import com.android.smap.di.DataManager;
import com.android.smap.models.SmapTextMessage;
import com.android.smap.sms.GatewayService;
import com.android.smap.sms.GatewayService.LocalBinder;
import com.android.smap.ui.ViewQuery;
import com.android.smap.utils.MWAnimUtil;
import com.google.inject.Inject;
import com.mjw.android.swipe.DistributionDetailsSwipeListener;
import com.mjw.android.swipe.MultiChoiceSwipeListener;
import com.mjw.android.swipe.SwipeListView;

import org.smap.DialogueHandler;
import org.smap.surveyConverser.SurveyConverser;

import java.util.List;


public class DistributionDetailFragment extends BaseFragment implements
           OnClickListener{

	public static final String		EXTRA_DISTRIBUTION_ID	= DistributionDetailFragment.class
															.getCanonicalName()
															+ "id";
    public GatewayService mService;
    public boolean mBound = false;

	@Inject
	private DataManager				mDataManager;
	private Distribution			mModel;
	private DialogueAdapter         mAdapter;
	private int						mDistributionId;
	private SwipeListView			mSwipeListView;
	private View					mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		if (b != null) {
			mDistributionId = (int) b.getLong(EXTRA_DISTRIBUTION_ID);
		}
		// get all necessary local data
		mDataManager = GatewayApp.getDependencyContainer().getDataManager();
		mModel = mDataManager.getDistribution(mDistributionId);
	}

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {

		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.fragment_distribution_detail,
				null);

		ViewQuery query = new ViewQuery(view);
		mSwipeListView = (SwipeListView) query.find(R.id.list_contacts).get();

		setupContactsList();

        TextView textView = (TextView) view.findViewById(R.id.txt_distribution_name);
        textView.setText(mModel.getName());


        int completed = mModel.getCompletedCount();
		int total = mModel.getMembersCount();


        String template = getActivity().getResources().getString(
				R.string.template_quotient);
		String completedProgress = String.format(template, completed, total);
		query.find(R.id.txt_completed_progress).text(completedProgress);
        query.find(R.id.btn_submit).onClick(this).get();


		// grow the progress bar out
		mProgressBar = query.find(R.id.view_progress).get();

		return view;
	}

    @Override
    public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this.getActivity(), GatewayService.class);
        this.getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

	@Override
	public void onResume() {
		super.onResume();

		mModel = mDataManager.getDistribution(mDistributionId);
        // TODO get this from distribution
		mAdapter.setModel(mModel.getDialogues());

		if (mModel != null) {

            float percent = mModel.getCompletionPercentage();
			MWAnimUtil.growRight(mProgressBar, percent);

		}
	}

	private void setupContactsList() {
		mAdapter = new DialogueAdapter(getActivity(), mModel.getDialogues(),
				mSwipeListView);
		mSwipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		mSwipeListView
				.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

					@Override
					public void onItemCheckedStateChanged(ActionMode mode,
							int position,
							long id, boolean checked) {
						mode.setTitle("Remove ("
								+ mSwipeListView.getCountSelected()
								+ ")");
					}

					@Override
					public boolean onActionItemClicked(ActionMode mode,
							MenuItem item) {
						switch (item.getItemId()) {
						case R.id.menu_delete:

							mSwipeListView.dismissSelected();
							mode.finish();
							return true;
						default:
							return false;
						}

					}

					@Override
					public boolean onCreateActionMode(ActionMode mode,
							Menu menu) {
						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.menu_choice_items, menu);
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

		mSwipeListView.setSwipeListViewListener(new DistributionDetailsSwipeListener(
				mAdapter));
		mSwipeListView.setAdapter(mAdapter);

	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.menu_add, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = false;
		switch (item.getItemId()) {
		case android.R.id.home: // Actionbar home/up icon
			getActivity().onBackPressed();
			break;
		case R.id.action_add: // Actionbar home/up icon
			Bundle b = new Bundle();
            b.putInt(ContactSelectFragment.EXTRA_DISTRIBUTION_ID, mDistributionId);
            startActivity(new FragmentContainerActivity.Builder(getActivity(), ContactSelectFragment.class)
                    .arguments(b)
                    .title(R.string.ab_select_contacts).build());

			break;
		}
		return handled;

	}

    @Override
    public void onClick(View arg0) {
        // each dialogue in distribution
        List<Dialogue> dialogues = mModel.getDialogues();

        for (Dialogue dialogue : dialogues) {
            Contact contact = dialogue.getContact();
            contact.setActiveDialogue(dialogue);
            DialogueHandler handler = new SmsDialogueHandler(dialogue, mService);
            SurveyConverser.beginDialogue(handler);
        }
    }


    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(EXTRA_DISTRIBUTION_ID, mDistributionId);
	}


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to GatewayService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
