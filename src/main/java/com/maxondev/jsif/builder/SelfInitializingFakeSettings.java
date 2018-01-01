package com.maxondev.jsif.builder;

public final class SelfInitializingFakeSettings {
    private String proxyToUrl;
    private String recordDirectory;
    private int port;
    private Mode mode;

    public String getProxyToUrl() {
        return proxyToUrl;
    }

    public Mode getMode() {
        return mode;
    }

    void setMode(Mode mode) {
        this.mode = mode;
    }

    void setProxyToUrl(String proxyToUrl) {
        this.proxyToUrl = proxyToUrl;
    }

    public String getRecordDirectory() {
        return recordDirectory;
    }

    void setRecordDirectory(String recordDirectory) {
        this.recordDirectory = recordDirectory;
    }

    public int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    SelfInitializingFakeSettings() {
    }
}