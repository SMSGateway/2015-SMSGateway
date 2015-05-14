package com.android.smap.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.adapters.DistributionAnswerAdapter;
import com.android.smap.api.models.Dialogue;
import com.android.smap.api.models.Distribution;
import com.android.smap.api.models.FilePart;
import com.android.smap.controllers.ControllerErrorListener;
import com.android.smap.controllers.ControllerListener;
import com.android.smap.controllers.NetworkError;
import com.android.smap.controllers.UploadSurveyController;
import com.android.smap.di.DataManager;
import com.android.smap.ui.ViewQuery;
import com.android.smap.utils.MWUiUtils;
import com.google.inject.Inject;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by kai on 13/05/2015.
 */
public class DistributionAnswersFragment extends BaseFragment implements
        AdapterView.OnItemClickListener,
        ControllerListener,
        ControllerErrorListener,
        View.OnClickListener {

    public static final String EXTRA_DISTRIBUTION_ID = DistributionAnswersFragment.class
            .getCanonicalName() + "id";

    @Inject
    private DataManager mDataManager;
    private Distribution mModel;
    private DistributionAnswerAdapter mAdapter;
    private int mDistributionId;
    private ListView listView;
    private UploadSurveyController mController;

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
                R.layout.fragment_distribution_answer, null);

        ViewQuery query = new ViewQuery(view);

        listView = (ListView) query.find(R.id.list_dialogues).get();
        setupDialoguesList();

        mController = new UploadSurveyController(getActivity(), this, this);
        TextView textView = (TextView) view.findViewById(R.id.txt_distribution_name);
        textView.setText(mModel.getName());

        query.find(R.id.btn_upload).onClick(this).get();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mModel = mDataManager.getDistribution(mDistributionId);
        mAdapter.setModel(mModel.getAnsweredDialogues());
    }

    private void setupDialoguesList() {
        mAdapter = new DistributionAnswerAdapter(getActivity(), mModel.getAnsweredDialogues());
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        // each dialogue in distribution
        List<Dialogue> dialogues = mModel.getAnsweredDialogues();

        if (dialogues.isEmpty()) {
            Toast.makeText(getActivity(), "Nothing to upload.", Toast.LENGTH_SHORT).show();
//            return;
        }

        String answers;
        FilePart filePart = null;
        showLoading(true);

        for (Dialogue dialogue : dialogues) {
            answers = dialogue.getInstanceXml();
            filePart = new FilePart(new ByteArrayInputStream(answers.getBytes()), "survey.xml");

            mController.cleanFileParts();
            mController.addFilePart(filePart);
            mController.start();

            dialogue.setSubmitted(true);
            dialogue.save();
        }

    }

    @Override
    public void onControllerError(NetworkError error) {
        MWUiUtils.showMessagePopup(getActivity(), "" + error.getNetworkErrorCode() + " " + error.getNetworkErrorMessage() + "\n Failed to retrieve Surveys");
        showLoading(false);
    }

    @Override
    public void onControllerResult() {
        showLoading(false);
        Toast.makeText(getActivity(), "Upload successfully.", Toast.LENGTH_SHORT).show();
        mModel = mDataManager.getDistribution(mDistributionId);
        mAdapter.setModel(mModel.getAnsweredDialogues());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = true;
        switch (item.getItemId()) {
            case android.R.id.home: // Actionbar home/up icon
                getActivity().onBackPressed();
                break;
        }
        return handled;
    }
}
