package com.maxondev.jsif;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.io.File;

import static com.maxondev.jsif.Config.ROOT_PATH;

class Recorder {
    final private WireMockServer wireMockServer;
    final private String proxyTo;
    final private String recordingPath;

    Recorder(int port, String proxyTo, String pathToSave) {
        this.proxyTo = proxyTo;

        this.recordingPath = ROOT_PATH + pathToSave;
        Options options = new WireMockConfiguration()
                .port(port)
                .withRootDirectory(recordingPath)
                .notifier(new Slf4jNotifier(true));
        this.wireMockServer = new WireMockServer(options);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (wireMockServer.isRunning())
                this.stop();
        }));
    }

    void record() {
        File recordingRoot = new File(recordingPath);
        if (!(new File(recordingRoot, WireMockApp.MAPPINGS_ROOT)).mkdirs())
            throw new RuntimeException("Couldn't create directory : " + recordingRoot.toString());

        wireMockServer.startRecording(proxyTo);
        wireMockServer.start();
    }

    void stop() {
        wireMockServer.stopRecording();
        wireMockServer.stop();
    }
}