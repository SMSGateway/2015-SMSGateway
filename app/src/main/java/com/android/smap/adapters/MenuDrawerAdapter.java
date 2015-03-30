package com.android.smap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.smap.R;
import com.android.smap.models.MenuDrawerItem;
import com.android.smap.ui.ArrayVelocAdapter;
import com.android.smap.ui.ViewQuery;

public class MenuDrawerAdapter extends ArrayVelocAdapter<MenuDrawerItem> {

	private static final MenuDrawerItem	MENUITEM_SURVEYS	= new MenuDrawerItem(
																	"Surveys",
																	R.drawable.ic_action_survey);
	private static final MenuDrawerItem	MENUITEM_CONTACTS	= new MenuDrawerItem(
																	"Contacts",
																	R.drawable.ic_action_contacts);
	private static final MenuDrawerItem	MENUITEM_SERVERS	= new MenuDrawerItem(
																	"Servers",
																	R.drawable.ic_action_server);
	private static final MenuDrawerItem	MENUITEM_SETTINGS	= new MenuDrawerItem(
																	"Settings",
																	R.drawable.ic_action_settings);
	private static final MenuDrawerItem	MENUITEM_INFO		= new MenuDrawerItem(
																	"Info",
																	R.drawable.ic_action_info);

	public MenuDrawerAdapter(Context context) {
		super(context);
		add(MENUITEM_SURVEYS);
		add(MENUITEM_CONTACTS);
		add(MENUITEM_SERVERS);
		add(MENUITEM_SETTINGS);
		add(MENUITEM_INFO);

	}

	@Override
	public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
		return inflator.inflate(R.layout.item_menudrawer, null, false);
	}

	@Override
	public void bindView(Context context, View view, ViewQuery query,
			int position) {
		query.find(R.id.txt_title).text(getItem(position).getName());
		query.find(R.id.ic_menu_item).image(getItem(position).getId());
	}
}
