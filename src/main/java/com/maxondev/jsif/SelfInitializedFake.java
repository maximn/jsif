package com.maxondev.jsif;

import com.maxondev.jsif.builder.Mode;
import com.maxondev.jsif.builder.ProxyTo;
import com.maxondev.jsif.builder.SelfInitializingFakeBuilder;
import com.maxondev.jsif.builder.SelfInitializingFakeSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.maxondev.jsif.Config.ROOT_PATH;

public final class SelfInitializedFake {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean started;
    private ProxyContainer proxyContainer;
    private final SelfInitializingFakeSettings settings;

    public SelfInitializedFake(SelfInitializingFakeSettings settings) {
        this.started = false;

        this.settings = settings;
    }

    public void start() {
        if (started) {
            throw new IllegalStateException("Already started");
        }

        Mode mode = evaluateRunningMode();

        switch (mode) {
            case RECORDER:
                record();
                break;
            case FAKE_SERVER:
                play();
                break;
        }
    }

    private void play() {
        logger.info("Starting in `Play` mode");
        FakeServer fake = new FakeServer(settings.getPort(), settings.getRecordDirectory());
        this.proxyContainer = new ProxyContainer(fake);
        fake.start();
    }

    private void record() {
        logger.info("Starting in `Recorder` mode");
        Recorder recorder = new Recorder(settings.getPort(), settings.getProxyToUrl(), settings.getRecordDirectory());
        this.proxyContainer = new ProxyContainer(recorder);
        recorder.record();
    }

    private Mode evaluateRunningMode() {
        Mode actualMode = settings.getMode();
        if (Mode.AUTO == actualMode)
            if (recordingExists())
                actualMode = Mode.FAKE_SERVER;
            else
                actualMode = Mode.RECORDER;
        return actualMode;
    }

    private boolean recordingExists() {
        return new File(ROOT_PATH + settings.getRecordDirectory()).exists();
    }

    public void stop() {
        proxyContainer.stop();
        started = false;
        proxyContainer = null;
    }

    public static ProxyTo builder() {
        return SelfInitializingFakeBuilder.builder();
    }
}
