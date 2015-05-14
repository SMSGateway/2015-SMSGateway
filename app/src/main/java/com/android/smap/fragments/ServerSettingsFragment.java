package com.android.smap.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.ui.ViewQuery;
import com.android.smap.utils.MWUiUtils;

public class ServerSettingsFragment extends BaseFragment implements
        OnClickListener {

    private EditText serverHost;
    private EditText serverPort;

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_server_setting, null);
        ViewQuery query = new ViewQuery(view);
        query.find(R.id.btn_submit).onClick(this).get();
        serverHost = (EditText) query.find(R.id.txt_serverIP).get();
        serverPort = (EditText) query.find(R.id.txt_serverPort).get();


        String existingIP = GatewayApp.getPreferenceWrapper().getServerHost();
        String existingPort = GatewayApp.getPreferenceWrapper().getServerPort();

        if (!TextUtils.isEmpty(existingIP)) {
            serverHost.setText(existingIP);
        }
        if (!TextUtils.isEmpty(existingPort)) {
            serverPort.setText(existingPort);
        }
        return view;
    }

    @Override
    public void onClick(View arg0) {

        GatewayApp.getPreferenceWrapper().setServerHost(serverHost.getText().toString());
        GatewayApp.getPreferenceWrapper().setServerPort(serverPort.getText().toString());
        MWUiUtils.hideKeyboard(getActivity());
        MWUiUtils.showMessagePopup(getActivity(), "Details Saved");

    }

    @Override
    public boolean hasActionBarTitle() {
        return true;
    }

    @Override
    public String getActionBarTitle() {
        return getActivity().getResources().getString(R.string.ab_server_settings);
    }
}
