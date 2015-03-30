package com.android.smap.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.smap.R;
import com.android.smap.api.models.Contact;
import com.android.smap.ui.VelocAdapter;
import com.android.smap.ui.ViewQuery;
import com.google.inject.Inject;
import com.mjw.android.swipe.MultiChoiceSwipeListener.MultiSelectActionAdapter;
import com.mjw.android.swipe.SwipeListView;

public class ContactSelectionAdapter extends VelocAdapter implements
		MultiSelectActionAdapter {

	private List<Contact>	mModel;
	private SwipeListView	mListViewRef;

	@Inject
	public ContactSelectionAdapter(Context context,
			List<Contact> model,
			SwipeListView ref) {
		super(context);
		this.mModel = model;
		this.mListViewRef = ref;
	}

	@Override
	public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
		return inflator.inflate(R.layout.item_contact_select_slider, null,
				false);
	}

	@Override
	public void bindView(Context context, View view, ViewQuery query,
			int position) {

		// clean up choice selections when scrolling
		mListViewRef.recycle(view, position);

		query.find(R.id.txt_name).text(mModel.get((position)).getName());
		query.find(R.id.txt_number)
				.text("Ph: " + mModel.get((position)).getNumber());

	}

	@Override
	public int getCount() {
		return mModel.size();
	}

	@Override
	public Contact getItem(int position) {
		return mModel.get(position);
	}

	public void setModel(List<Contact> model) {
		this.mModel = model;
	}

	@Override
	public void action(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionAllSelected(int[] pos) {
		// TODO Auto-generated method stub

	}

}
