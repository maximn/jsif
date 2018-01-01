package com.maxondev.jsif;

class ProxyContainer {

    private final Object proxy;

    ProxyContainer(Object proxy) {
        this.proxy = proxy;
    }

    void stop() {
        if (proxy instanceof Recorder) {
            ((Recorder) proxy).stop();
        } else if (proxy instanceof FakeServer) {
            ((FakeServer) proxy).stop();
        } else {
            throw new IllegalStateException("Proxy is of unknown type : " + proxy.getClass().getName());
        }
    }
}
