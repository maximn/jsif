package io.jsif.recorder;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockApp;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.jsif.FilesUtils;

import java.io.File;

import static io.jsif.Config.ROOT_PATH;

public class Recorder {
    final private WireMockServer wireMockServer;
    final private String proxyTo;
    final private String recordingPath;

    public Recorder(int port, String proxyTo, String pathToSave) {
        this.proxyTo = proxyTo;

        this.recordingPath = ROOT_PATH + pathToSave;
        Options options = new WireMockConfiguration()
                .port(port)
                .withRootDirectory(recordingPath);
        this.wireMockServer = new WireMockServer(options);
    }

    public void record() {
        File recordingRoot = new File(recordingPath);
        if (recordingRoot.exists())
            if (!FilesUtils.deleteRecursive(recordingRoot))
                throw new RuntimeException("Couldn't delete directory : " + recordingRoot.toString());
        if (!(new File(recordingRoot, WireMockApp.MAPPINGS_ROOT)).mkdirs())
            throw new RuntimeException("Couldn't create directory : " + recordingRoot.toString());

        wireMockServer.startRecording(proxyTo);
        wireMockServer.start();
    }

    public void stop() {
        wireMockServer.stopRecording();
        wireMockServer.stop();
    }
}