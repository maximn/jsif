package com.maxondev.jsif.player;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.maxondev.jsif.Config;

public class FakeServer {
    private final WireMockServer wireMockServer;

    public FakeServer(int port, String pathToLoad) {
        this.wireMockServer =
                new WireMockServer(port,
                        new SingleRootFileSource(Config.ROOT_PATH + pathToLoad),
                        false);
    }

    public void start() {
        wireMockServer.start();
    }

    public void stop() {
        wireMockServer.stop();
    }
}