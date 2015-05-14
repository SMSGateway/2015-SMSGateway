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

public class DistributionAnswerAdapter extends VelocAdapter {

    private List<Dialogue> mModel;
    private DataManager mDataManager;

    @Inject
    public DistributionAnswerAdapter(Context context, List<Dialogue> model) {
        super(context);
        this.mModel = model;
        mDataManager = GatewayApp.getDependencyContainer().getDataManager();
    }

    @Override
    public View newView(LayoutInflater inflator, int position, ViewGroup parent) {
        return inflator.inflate(R.layout.item_distribution_answer, null,
                false);
    }

    @Override
    public void bindView(Context context, View view, ViewQuery query,
                         int position) {

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
        String upload;

        if (dialogue.isSubmitted())
            upload = "Yes";
        else
            upload = "No";

        query.find(R.id.txt_name).text(contactName);
        query.find(R.id.txt_number).text("Ph: " + phoneNumber);
        query.find(R.id.txt_completed_total).text("Total: " + String.valueOf(total));
        query.find(R.id.txt_completed_progress).text("Answers: " + String.valueOf(completed));
        query.find(R.id.txt_upload_label).text("Upload: " + upload);
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

}
