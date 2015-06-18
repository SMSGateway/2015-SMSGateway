package com.android.smap.fragments;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.activities.FragmentContainerActivity.Builder;
import com.android.smap.adapters.SurveyAdapter;
import com.android.smap.api.models.FormList;
import com.android.smap.api.models.Survey;
import com.android.smap.controllers.ControllerErrorListener;
import com.android.smap.controllers.ControllerListener;
import com.android.smap.controllers.FormListController;
import com.android.smap.controllers.NetworkError;
import com.android.smap.controllers.SurveyDefinitionController;
import com.android.smap.di.DataManager;
import com.android.smap.ui.ViewQuery;
import com.android.smap.utils.MWUiUtils;
import com.google.inject.Inject;
import com.mjw.android.swipe.MultiChoiceSwipeListener;
import com.mjw.android.swipe.SwipeListView;

import java.util.List;

public class SurveysFragment extends BaseFragment implements
        ControllerListener,
        ControllerErrorListener {

    @Inject
    private DataManager mDataManager;
    private List<Survey> mModel;
    private SurveyAdapter mAdapter;
    private SwipeListView mSwipeListView;
    private FormListController mController;
    private SurveyDefinitionController controller;
    private List<FormList.Form> forms;

    @Override
    public View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_surveys, null);

        ViewQuery query = new ViewQuery(view);
        mSwipeListView = (SwipeListView) query.find(R.id.list_surveys).get();

        mDataManager = GatewayApp.getDependencyContainer().getDataManager();
        mModel = mDataManager.getSurveys();
        setupSurveysList();

        return view;
    }

    private void setupSurveysList() {

        mAdapter = new SurveyAdapter(getActivity(), mModel,
                mSwipeListView);
        mSwipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        mSwipeListView
                .setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode,
                                                          int position,
                                                          long id, boolean checked) {
                        mode.setTitle("Remove ("
                                + mSwipeListView.getCountSelected() + ")");
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode,
                                                       MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete_survey:
                                mSwipeListView.dismissSelected();
                                mode.finish();
                                return true;
                            default:
                                return false;
                        }

                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode,
                                                      Menu menu) {
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_delete, menu);
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        mSwipeListView.unselectedChoiceStates();
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode,
                                                       Menu menu) {
                        return false;
                    }
                });

        mSwipeListView.setSwipeListViewListener(new MultiChoiceSwipeListener(mAdapter) {
            @Override
            public void onClickFrontView(int position) {
                super.onClickFrontView(position);
                Survey survey = (Survey) mAdapter.getItem(position);
                Bundle b = new Bundle();
                b.putLong(DistributionDetailFragment.EXTRA_DISTRIBUTION_ID, survey.getId());
                startActivity(new Builder(getActivity(), SurveyDistributionsFragment.class)
                        .arguments(b).title(R.string.ab_distributions).build());

            }
        });

        mSwipeListView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        mAdapter.setModel(mDataManager.getSurveys());
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_local_survey, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {

            case android.R.id.home: // Actionbar home/up icon
                // getActivity().onBackPressed();
                break;
            case R.id.action_add: // fetch all the remote surveys
                fetchListSurveys();
                break;
            case R.id.action_view_complete_survey: // go to a view
                startActivity(new Builder(getActivity(), CompletedSurveysFragment.class)
                        .title(R.string.ab_complete_surveys).build());
                break;
        }
        return handled;
    }

    @Override
    public boolean hasActionBarTitle() {
        return true;
    }

    @Override
    public String getActionBarTitle() {
        return getActivity().getResources().getString(R.string.ab_surveys);
    }

    @Override
    public void onControllerError(NetworkError error) {
        int errorCode = error.getNetworkErrorCode();
        String hint;
        if (errorCode >= 400)
            hint = "" + error.getNetworkErrorCode() + " " + error.getNetworkErrorMessage() + "\n Failed to retrieve Surveys";
        else
            hint = error.getNetworkErrorMessage() + "\n Failed to retrieve Surveys";

        MWUiUtils.showMessagePopup(getActivity(), hint);
        showLoading(false);
    }

    @Override
    public void onControllerResult() {

    //each form has been downloaded
    if(GatewayApp.getNewSurveys().size() == forms.size())
    {
        for(Survey survey: mDataManager.getSurveys()){

         if(!GatewayApp.getNewSurveys().contains(survey.getFormXml())){
           survey.delete();
          }
        }
    }
        mAdapter.setModel(mDataManager.getSurveys());
//        Toast.makeText(getActivity(), "One survey is retrieved.", Toast.LENGTH_SHORT).show();
    }

    public void fetchListSurveys() {
        // fetch the form list
        mController = new FormListController(getActivity(), new ControllerListener() {
            @Override
            public void onControllerResult() {
                GatewayApp.getNewSurveys().clear();
                fetchEachSurvey();
            }
        }, this);
        mController.start();
    }

    public void fetchEachSurvey() {
        forms = mController.getModel().getForms();

        // fetch each survey
        for (FormList.Form form : forms) {
            controller = new SurveyDefinitionController(
                    getActivity(), this, this, form.getUrl());
            controller.start();
        }
    }
}
