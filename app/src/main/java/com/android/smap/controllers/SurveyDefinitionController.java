package com.android.smap.controllers;

import org.smap.surveyModel.SurveyModel;

import android.content.Context;

import com.android.smap.api.requests.HttpUrlRequest;
import com.android.smap.api.requests.SurveyDefinitionRequest;
import com.android.smap.api.models.Survey;

public class SurveyDefinitionController extends
        RawRequestController<Survey> {

    private String mFormUrl;

    public SurveyDefinitionController(Context context,
                                      ControllerListener listener,
                                      ControllerErrorListener errorListener,
                                      String formKey) {
        super(context, listener, errorListener);
        mListener = listener;
        mErrorListener = errorListener;
        mFormUrl = formKey;
    }

    public void setFormKey(String formKey) {
        this.mFormUrl = formKey;
    }


    @Override
    protected HttpUrlRequest getHttpUrlRequest() {
        return new SurveyDefinitionRequest(mFormUrl, this, this);
    }

    @Override
    protected Survey addResponseToDatabase(String rawXML) {
        Survey survey = new Survey();

        if ((Survey.findByFormXml(rawXML)).isEmpty()) {
            SurveyModel SurveyManager = SurveyModel.createSurveyModelFromXform(rawXML);
            survey.setName(SurveyManager.getSurveyName());
            survey.setFormXml(rawXML);
            survey.save();
        }

        return survey;
    }
}
