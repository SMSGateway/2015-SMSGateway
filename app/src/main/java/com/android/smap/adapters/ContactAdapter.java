package com.android.smap.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.smap.api.models.Contact;
import com.android.smap.R;
import com.google.inject.Inject;

public class ContactAdapter extends ArrayAdapter<Contact> {
	
	private Activity activity;
	private List<Contact> items;
	private int row;
	private Contact objContact;

	@Inject
	public ContactAdapter(Activity Act, int Row, List<Contact> Item) {
		super(Act, Row, Item);
		this.activity = Act;
		this.row = Row;
		this.items = Item;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View view = convertView;
		ViewHolder holder;

		if(view == null){
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);
			holder = new ViewHolder();
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		if ((items == null) || ((position + 1) > items.size()))
			return view;
		
		objContact = items.get(position);
		holder.contactname = (TextView) view.findViewById(R.id.contactname);
		holder.contactPhoneNo = (TextView) view.findViewById(R.id.contactphone);
		
		if(holder.contactname != null && null != objContact.getName() && objContact.getName().trim().length() > 0){
			holder.contactname.setText(Html.fromHtml(objContact.getName()));
		}
		if(holder.contactPhoneNo != null && null != objContact.getNumber() && objContact.getNumber().trim().length() > 0){
			holder.contactPhoneNo.setText(objContact.getNumber());
		}
		
		return view;
	}
	
	public class ViewHolder {
		public TextView contactname, contactPhoneNo;
	}
}


//contactAdapter.java