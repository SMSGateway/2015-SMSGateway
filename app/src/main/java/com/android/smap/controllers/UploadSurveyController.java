package com.android.smap.controllers;

import android.content.Context;
import android.util.Log;

import com.android.smap.api.models.FilePart;
import com.android.smap.api.models.FormList;
import com.android.smap.api.models.FormList.Form;
import com.android.smap.api.requests.FormListRequest;
import com.android.smap.api.requests.HttpUrlRequest;
import com.android.smap.api.requests.UploadSurveyRequest;
import com.android.smap.utils.Refresher;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class UploadSurveyController extends RequestController implements
        Controller,
        HttpListener<String>,
        HttpErrorListener {

    protected ControllerListener mListener;
    protected ControllerErrorListener mErrorListener;
    protected ArrayList<FilePart> fileParts;

    public UploadSurveyController(Context context,
                                  ControllerListener listener,
                                  ControllerErrorListener errorListener) {
        super(context);
        mListener = listener;
        mErrorListener = errorListener;
    }

    public void setFileParts(ArrayList<FilePart> fileParts) {
        this.fileParts = fileParts;
    }

    public void addFilePart(FilePart filePart) {
        if (fileParts == null || fileParts.isEmpty())
            this.fileParts = new ArrayList<FilePart>();

        this.fileParts.add(filePart);
    }

    public void cleanFileParts() {
        if (fileParts != null && !fileParts.isEmpty())
            this.fileParts.clear();
    }

    protected HttpUrlRequest getHttpUrlRequest() {
        return new UploadSurveyRequest(this, this);
    }

    @Override
    public ControllerListener getControllerListener() {
        return mListener;
    }

    @Override
    public ControllerErrorListener getControllerErrorListener() {
        return mErrorListener;
    }

    @Override
    public void start() {
        HttpUrlRequest request = getHttpUrlRequest();
        request.setFileParts(fileParts);
        request.executeRequest();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onErrorResponse(NetworkError networkError) {
        getControllerErrorListener().onControllerError(networkError);
    }

    @Override
    public void onResponse(String response) {
        getControllerListener().onControllerResult();
    }
}
