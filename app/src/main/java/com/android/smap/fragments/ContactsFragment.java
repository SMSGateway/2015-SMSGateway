package com.android.smap.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.adapters.ContactAdapter;
import com.android.smap.api.models.Contact;
import com.android.smap.controllers.ContactImportController;
import com.android.smap.di.DataManager;
import com.google.inject.Inject;

import java.util.List;

public class ContactsFragment extends BaseFragment implements OnItemClickListener {


    @Inject
    private DataManager mDataManager;
    private ContactAdapter mAdapter;
    private List<Contact> list;

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_contact,
                null);
        mDataManager = GatewayApp.getDependencyContainer().getDataManager();
        ListView listView = (ListView) view.findViewById(R.id.list_contacts);
        listView.setOnItemClickListener(this);
        list = mDataManager.getContacts();

        mAdapter = new ContactAdapter(getActivity(), R.layout.contact_allusers_rows, list);
        listView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> av, View parent, int pos, long viewId) {
        // TODO Auto-generated method stub
        Bundle B = new Bundle();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean hasActionBarTitle() {
        return true;
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.ab_all_contacts);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {

            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, ContactImportController.PICK_CONTACTS);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            ContactImportController contactsManager = new ContactImportController(getActivity(), data);
            String name = "";
            String number = "";
            try {
                    name = contactsManager.getContactName();
                    number = contactsManager.getContactPhone();
                    Contact contact = new Contact(name, number);
                    contact.save();
                } catch (Exception e) {
                    Log.e("CONTACTS", e.getMessage());
                }
        }
    }
}

