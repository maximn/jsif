package com.maxondev.jsif;

import com.maxondev.jsif.player.FakeServer;
import com.maxondev.jsif.recorder.Recorder;

class ProxyContainer {

    private final Object proxy;

    ProxyContainer(Object proxy) {
        this.proxy = proxy;
    }

    void stop() {
        if (proxy instanceof Recorder) {
            ((Recorder)proxy).stop();
        } else if (proxy instanceof FakeServer) {
            ((FakeServer) proxy).stop();
        }
        else {
            throw new IllegalStateException("Proxy is of unknown type : " + proxy.getClass().getName());
        }
    }
}
