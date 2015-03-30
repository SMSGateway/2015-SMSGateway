package com.android.smap.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.ui.ViewQuery;
import com.android.smap.utils.MWUiUtils;

public class SettingsFragment extends BaseFragment implements
        OnClickListener {

    private EditText password;
    private EditText username;

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_settings,
                null);
        ViewQuery query = new ViewQuery(view);
        query.find(R.id.btn_submit).onClick(this).get();
        username = (EditText) query.find(R.id.txt_username).get();
        password = (EditText) query.find(R.id.txt_password).get();


        String existingName = GatewayApp.getPreferenceWrapper().getUserName();
        String existingPassword = GatewayApp.getPreferenceWrapper().getPassword();

        if(!TextUtils.isEmpty(existingName)){
            username.setText(existingName);
        }
        if(!TextUtils.isEmpty(existingPassword)){
            password.setText(existingPassword);
        }
        return view;
    }

    @Override
    public void onClick(View arg0) {

        GatewayApp.getPreferenceWrapper().setUserName(
                username.getText().toString());
        GatewayApp.getPreferenceWrapper().setPassword(
                password.getText().toString());
        MWUiUtils.hideKeyboard(getActivity());
        MWUiUtils.showMessagePopup(getActivity(), "Details Saved");

    }

    @Override
    public boolean hasActionBarTitle() {
        return true;
    }

    @Override
    public String getActionBarTitle() {
        return getActivity().getResources().getString(R.string.ab_settings);
    }
}
