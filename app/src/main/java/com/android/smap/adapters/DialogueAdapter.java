package com.android.smap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.smap.GatewayApp;
import com.android.smap.R;
import com.android.smap.api.models.Contact;
import com.android.smap.api.models.Dialogue;
import com.android.smap.di.DataManager;
import com.android.smap.ui.VelocAdapter;
import com.android.smap.ui.ViewQuery;
import com.android.smap.utils.MWCommsUtils;
import com.google.inject.Inject;
import com.mjw.android.swipe.DistributionDetailsSwipeListener;
import com.mjw.android.swipe.SwipeListView;

import java.util.List;

public class DialogueAdapter extends VelocAdapter implements
        DistributionDetailsSwipeListener.MultiSelectBackViewActionAdapter {

    private List<Dialogue> mModel;
    private SwipeListView mListViewRef;
    private DataManager mDataManager;

    @Inject
    public DialogueAdapter(Context context, List<Dialogue> model,
                           SwipeListView ref) {
        super(context);
        this.mModel = model;
        this.mListViewRef = ref;
        mDataManager = GatewayApp.getDependencyContainer().getDataManager();
    }

    @Override
    public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
        return inflator.inflate(R.layout.item_survey_contact_slider, null,
                false);
    }

    @Override
    public void bindView(Context context, View view, ViewQuery query,
                         int position) {

        // clean up choice selections when scrolling
        mListViewRef.recycle(view, position);

        Dialogue dialogue = getItem(position);

        Contact contact;
        String contactName, phoneNumber;
        if ((contact = dialogue.contact) != null) {
            contactName = contact.getName();
            phoneNumber = contact.getNumber();
        } else {
            contactName = "";
            phoneNumber = "";
        }

        String updatedAt = dialogue.updatedAt;
        int completed = dialogue.getQuestionNumber();
        int total = dialogue.getSurvey().getNumberOfQuestions();

        String template = getContext().getResources().getString(
                R.string.surveys_of_total);
        String totalCount = String.format(template, total);

        query.find(R.id.txt_name).text(contactName);
        query.find(R.id.txt_number).text("Ph: " + phoneNumber);
        query.find(R.id.txt_completed_progress).text(String.valueOf(completed));
        query.find(R.id.txt_completed_total).text(totalCount);
        query.find(R.id.txt_timestamp).text(updatedAt);

    }

    @Override
    public int getCount() {
        return mModel.size();
    }

    @Override
    public Dialogue getItem(int position) {
        return mModel.get(position);
    }

    public void setModel(List<Dialogue> model) {
        this.mModel = model;
        notifyDataSetChanged();
    }

    @Override
    public void actionAllSelected(int[] pos) {

        for (int i : pos) {
            this.action(i);
        }
    }

    @Override
    public void action(int pos) {
        Dialogue dialogue = getItem(pos);
        mDataManager.removeContactFromDistribution(
                dialogue.contact.getId(),
                dialogue.getDistribution().getId());
        mModel.remove(pos);
        notifyDataSetChanged();

    }

    @Override
    public void onBackViewPressed(int position) {

        MWCommsUtils.call(getContext(), getItem(position).getContact().getNumber());

    }
}
