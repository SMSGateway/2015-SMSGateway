package com.android.smap.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.api.models.Survey;
import com.android.smap.di.DataManager;
import com.android.smap.ui.VelocAdapter;
import com.android.smap.ui.ViewQuery;
import com.google.inject.Inject;
import com.mjw.android.swipe.MultiChoiceSwipeListener.MultiSelectActionAdapter;
import com.mjw.android.swipe.SwipeListView;

public class SurveyAdapter extends VelocAdapter 
		implements MultiSelectActionAdapter {

	private List<Survey>	mModel;
	private SwipeListView 	mListViewRef;
	private float			mProgressBarTotal;
	private DataManager		mDataManager;

	@Inject
	public SurveyAdapter(Context context, List<Survey> model, 
			SwipeListView ref) {
		super(context);
		this.mModel = model;
		this.mListViewRef = ref;
		mDataManager = GatewayApp.getDependencyContainer().getDataManager();
		mProgressBarTotal = getContext()
				.getResources().getDimension(R.dimen.survey_progress_width);
	}

	@Override
	public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
		return inflator.inflate(R.layout.item_survey_slider, null, false);

	}

	@Override
	public void bindView(Context context, View view, ViewQuery query,
			int position) {
		
		// clean up choice selections when scrolling
		mListViewRef.recycle(view, position);
		
		Survey survey = mModel.get((position));

        int distributionCount = survey.getMembersCount();
		String surveyName = survey.getName();

		query.find(R.id.txt_name).text(surveyName);
		query.find(R.id.txt_total_distribution).text(String.valueOf(distributionCount));
	}

	@Override
	public int getCount() {
		return mModel.size();
	}

	@Override
	public Survey getItem(int position) {
		return mModel.get(position);
	}

	public void setModel(List<Survey> model) {
		this.mModel = model;
		notifyDataSetChanged();
	}

	@Override
	public void action(int pos) {
		Survey survey = getItem(pos);
		mDataManager.deleteSurvey(survey);
		notifyDataSetChanged();
        mModel.remove(pos);
	}

	@Override
	public void actionAllSelected(int[] pos) {
		
		for (int i : pos) {
			this.action(i);
		}
		
	}
	
	

}
