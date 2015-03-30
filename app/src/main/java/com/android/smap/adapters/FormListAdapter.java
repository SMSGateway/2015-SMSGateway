package com.android.smap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.smap.R;
import com.android.smap.api.models.FormList;
import com.android.smap.api.models.FormList.Form;
import com.android.smap.ui.VelocAdapter;
import com.android.smap.ui.ViewQuery;
import com.google.inject.Inject;

public class FormListAdapter extends VelocAdapter {

	private FormList	mModel;

	@Inject
	public FormListAdapter(Context context, FormList model) {
		super(context);
		this.mModel = model;
	}

	@Override
	public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
		return inflator.inflate(R.layout.item_survey, null, false);

	}

	@Override
	public void bindView(Context context, View view, ViewQuery query,
			int position) {

		Form form = mModel.getForms().get((position));
		query.find(R.id.txt_name).text(form.getName());

	}

	@Override
	public int getCount() {
		if (mModel == null || mModel.getForms() == null) {
			return 0;
		}
		return mModel.getForms().size();
	}

	@Override
	public Object getItem(int position) {
		if (mModel == null || mModel.getForms() == null) {
			return null;
		}
		return mModel.getForms().get(position);
	}

	public void setModel(FormList model) {
		this.mModel = model;
		notifyDataSetChanged();
	}

}
