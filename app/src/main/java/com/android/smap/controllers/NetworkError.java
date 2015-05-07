package com.android.smap.controllers;

/**
 * Network Errors can be passed to an Error View to be dealt with and can
 * be displayed to the user.
 *
 * @author Kai Qin
 */
public class NetworkError {

    private String networkMessage = "";
    private String networkDescription = "";
    private int networkErrorCode;

    public NetworkError() {
    }

    public NetworkError(int networkErrorCode) {
        this.networkErrorCode = networkErrorCode;
    }

    public NetworkError(String networkMessage, String networkDescription, int networkErrorCode) {
        this.networkMessage = networkMessage;
        this.networkDescription = networkDescription;
        this.networkErrorCode = networkErrorCode;
    }


    public String getNetworkMessage() {
        return networkMessage;
    }

    public void setNetworkMessage(String networkMessage) {
        this.networkMessage = networkMessage;
    }

    public String getNetworkDescription() {
        return networkDescription;
    }

    public void setNetworkDescription(String networkDescription) {
        this.networkDescription = networkDescription;
    }

    public int getNetworkErrorCode() {
        return networkErrorCode;
    }

    public void setNetworkErrorCode(int networkErrorCode) {
        this.networkErrorCode = networkErrorCode;
    }
}
