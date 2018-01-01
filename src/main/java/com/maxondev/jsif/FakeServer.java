package com.maxondev.jsif;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;

class FakeServer {
    private final WireMockServer wireMockServer;

    FakeServer(int port, String pathToLoad) {
        this.wireMockServer =
                new WireMockServer(port,
                        new SingleRootFileSource(Config.ROOT_PATH + pathToLoad),
                        false);
    }

    void start() {
        wireMockServer.start();
    }

    void stop() {
        wireMockServer.stop();
    }
}