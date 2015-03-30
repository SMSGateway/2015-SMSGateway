package com.android.smap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;


import com.android.smap.R;
import com.android.smap.api.models.Distribution;
import com.android.smap.ui.VelocAdapter;
import com.android.smap.ui.ViewQuery;
import com.google.inject.Inject;

import java.util.List;

public class DistributionAdapter extends VelocAdapter {

	private List<Distribution>	mModel;
    private float	            mProgressBarTotal;

	@Inject
	public DistributionAdapter(Context context, List<Distribution> model) {
		super(context);
		this.mModel = model;
        mProgressBarTotal = getContext()
                .getResources().getDimension(R.dimen.survey_progress_width);
	}

	@Override
	public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
		return inflator.inflate(R.layout.item_distribution, null, false);
	}

	@Override
	public void bindView(Context context, View view, ViewQuery query,
			int position) {

        Distribution distribution = mModel.get((position));

		String distributionName = distribution.getName();

		int totalDialogue = distribution.getMembersCount();
        int completedDialogue = distribution.getCompletedCount();
        float completionPercent = distribution.getCompletionPercentage();

        query.find(R.id.txt_name).text(distributionName);

		// String formatting
		String template = getContext().getResources().getString(
				R.string.template_quotient);

        String progress = String.format(template, completedDialogue, totalDialogue);
        query.find(R.id.txt_member_progress).text(String.valueOf(progress));

        //Setting progress bar
        View progressBar = query.find(R.id.view_progress).get();
        LayoutParams params = progressBar.getLayoutParams();
        params.width = (int) (mProgressBarTotal * completionPercent/100);

        if(params.width == 0)
        params.width = (int) (mProgressBarTotal * 0.01); //Shows some green
        progressBar.setLayoutParams(params);

	}

	@Override
	public int getCount() {
		return mModel.size();
	}

	@Override
	public Distribution getItem(int position) {

        if(mModel != null){
            return mModel.get(position);
        }
        return null;
	}

	public void setModel(List<Distribution> model) {
		this.mModel = model;
		notifyDataSetChanged();
	}

}
