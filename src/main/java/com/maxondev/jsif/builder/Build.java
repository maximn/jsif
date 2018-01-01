package com.maxondev.jsif.builder;

import com.maxondev.jsif.SelfInitializedFake;

public interface Build {
    SelfInitializedFake asAutoMode();
    SelfInitializedFake asFakeServer();
    SelfInitializedFake asRecorder();
}
