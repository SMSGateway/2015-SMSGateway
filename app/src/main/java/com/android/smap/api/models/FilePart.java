package com.android.smap.api.models;

import java.io.InputStream;

/**
 * Each file is a inputStream for post request
 * <p/>
 * Created by kai on 11/05/2015.
 */
public class FilePart {
    private String fieldName;
    private InputStream inputStream;
    private String fileName;

    public FilePart(InputStream inputStream, String fileName) {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.fieldName = "xml_submission_data";
    }

    public FilePart(String fieldName, InputStream inputStream, String fileName) {
        this.fieldName = fieldName;
        this.inputStream = inputStream;
        this.fileName = fileName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
