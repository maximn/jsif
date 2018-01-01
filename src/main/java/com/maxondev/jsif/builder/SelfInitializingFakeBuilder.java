package com.maxondev.jsif.builder;

import com.maxondev.jsif.SelfInitializedFake;

public final class SelfInitializingFakeBuilder implements ProxyTo, RecordTo, ListenOnPort, Build {
    private final SelfInitializingFakeSettings settings = new SelfInitializingFakeSettings();


    private SelfInitializingFakeBuilder() {
    }

    public static ProxyTo builder() {
        return new SelfInitializingFakeBuilder();
    }

    @Override
    public ListenOnPort recordTo(String directoryName) {
        settings.setRecordDirectory(directoryName);
        return this;
    }


    @Override
    public RecordTo proxyTo(String url) {
        settings.setProxyToUrl(url);
        return this;
    }

    @Override
    public Build listenOnPort(int port) {
        settings.setPort(port);
        return this;
    }

    @Override
    public SelfInitializedFake asAutoMode() {
        settings.setMode(Mode.AUTO);
        return new SelfInitializedFake(settings);
    }

    @Override
    public SelfInitializedFake asFakeServer() {
        settings.setMode(Mode.FAKE_SERVER);
        return new SelfInitializedFake(settings);
    }

    @Override
    public SelfInitializedFake asRecorder() {
        settings.setMode(Mode.RECORDER);
        return new SelfInitializedFake(settings);
    }
}
