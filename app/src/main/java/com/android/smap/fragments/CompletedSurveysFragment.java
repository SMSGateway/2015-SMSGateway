package com.android.smap.fragments;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

/**
 * show Completed Surveys and upload
 * <p/>
 * Created by kai on 13/05/2015.
 */
public class CompletedSurveysFragment extends BaseFragment implements
        ControllerListener,
        ControllerErrorListener {

    public static final String EXTRA_DISTRIBUTION_ID = CompletedSurveysFragment.class
            .getCanonicalName() + "id";

    @Inject
    private List<Dialogue> dialogues;
    private DistributionAnswerAdapter mAdapter;
    private ListView listView;
    private UploadSurveyController mController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogues = Dialogue.findAllCompletedDialogue();
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_complete_surveys, null);

        ViewQuery query = new ViewQuery(view);

        listView = (ListView) query.find(R.id.list_dialogues).get();
        setupDialoguesViewList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dialogues = Dialogue.findAllCompletedDialogue();
        mAdapter.setModel(dialogues);
    }

    private void setupDialoguesViewList() {
        mAdapter = new DistributionAnswerAdapter(getActivity(), dialogues);
        listView.setAdapter(mAdapter);
    }

    public void uploadCompletedSurveys() {
        if (dialogues == null || dialogues.isEmpty()) {
            Toast.makeText(getActivity(), "Nothing to upload.", Toast.LENGTH_SHORT).show();
            return;
        }

        String answers;
        FilePart filePart = null;
        showLoading(true);

        for (Dialogue dialogue : dialogues) {
            answers = dialogue.getInstanceXml();
            Log.i("answers", answers);
            filePart = new FilePart(new ByteArrayInputStream(answers.getBytes()), "survey.xml");

            mController = new UploadSurveyController(getActivity(), this, this);
            mController.cleanFileParts();
            mController.addFilePart(filePart);
            mController.start();

            dialogue.setSubmitted(true);
            dialogue.save();
        }

    }

    @Override
    public void onControllerError(NetworkError error) {
        int errorCode = error.getNetworkErrorCode();
        if (errorCode >= 400)
            MWUiUtils.showMessagePopup(getActivity(), "Error:" + error.getNetworkErrorCode() + " "
                    + error.getNetworkErrorMessage() + "\n Failed to upload Surveys");
        else
            MWUiUtils.showMessagePopup(getActivity(), error.getNetworkErrorMessage() + "\n Failed to upload Surveys");

        showLoading(false);
    }

    @Override
    public void onControllerResult() {
        showLoading(false);
        Toast.makeText(getActivity(), "Upload successfully.", Toast.LENGTH_SHORT).show();
        dialogues = Dialogue.findAllCompletedDialogue();
        mAdapter.setModel(dialogues);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_complete_survey, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {

            case android.R.id.home: // Actionbar home/up icon
                getActivity().onBackPressed();
                break;
            case R.id.action_upload:
                uploadCompletedSurveys();
                break;
        }
        return handled;
    }
}
