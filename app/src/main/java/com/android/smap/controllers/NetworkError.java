package com.android.smap.controllers;

/**
 * Network Errors can be passed to an Error View to be dealt with and can
 * be displayed to the user.
 *
 * @author Kai Qin
 */
public class NetworkError {

    private String networkErrorMessage = "";
    private String networkErrorDescription = "";
    private int networkErrorCode;

    public NetworkError() {
        networkErrorCode = 0;
    }

    public NetworkError(String networkErrorMessage, int networkErrorCode) {
        this.networkErrorMessage = networkErrorMessage;
        this.networkErrorCode = networkErrorCode;
    }

    public NetworkError(String networkErrorMessage, String networkErrorDescription, int networkErrorCode) {
        this.networkErrorMessage = networkErrorMessage;
        this.networkErrorDescription = networkErrorDescription;
        this.networkErrorCode = networkErrorCode;
    }

    public String getNetworkErrorMessage() {
        return networkErrorMessage;
    }

    public void setNetworkErrorMessage(String networkErrorMessage) {
        this.networkErrorMessage = networkErrorMessage;
    }

    public String getNetworkErrorDescription() {
        return networkErrorDescription;
    }

    public void setNetworkErrorDescription(String networkErrorDescription) {
        this.networkErrorDescription = networkErrorDescription;
    }

    public int getNetworkErrorCode() {
        return networkErrorCode;
    }

    public void setNetworkErrorCode(int networkErrorCode) {
        this.networkErrorCode = networkErrorCode;
    }
}
