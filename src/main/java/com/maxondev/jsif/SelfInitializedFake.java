package com.maxondev.jsif;

import com.maxondev.jsif.player.FakeServer;
import com.maxondev.jsif.recorder.Recorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.maxondev.jsif.Config.ROOT_PATH;

public class SelfInitializedFake {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String proxyTo;
    private final String recordingPath;
    private final int port;
    private boolean started;
    private ProxyContainer proxyContainer;

    public SelfInitializedFake(int port, String proxyTo, String recordingPath) {
        this.started = false;

        this.port = port;
        this.proxyTo = proxyTo;

        this.recordingPath = recordingPath;
    }

    public void start() {
        if (started) {
            throw new IllegalStateException("Fake is already started");
        }

        if (recordingExists()) {
            logger.info("Starting in `Play` mode");
            FakeServer fake = new FakeServer(this.port, recordingPath);
            this.proxyContainer = new ProxyContainer(fake);
            fake.start();
        }
        else {
            logger.info("Starting in `Record` mode");
            Recorder recorder = new Recorder(port, proxyTo, recordingPath);
            this.proxyContainer = new ProxyContainer(recorder);
            recorder.record();
        }
        this.started = true;
    }

    private boolean recordingExists() {
        return new File(ROOT_PATH + recordingPath).exists();
    }

    public void stop() {
        proxyContainer.stop();
        started = false;
        proxyContainer = null;
    }
}
