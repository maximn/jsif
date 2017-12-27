[![Build Status](https://travis-ci.org/maximn/jsif.svg?branch=master)](https://travis-ci.org/maximn/jsif)


# jsif
Self Initializing Fake library for the JVM (inspired by [https://martinfowler.com/bliki/SelfInitializingFake.html](https://martinfowler.com/bliki/SelfInitializingFake.html))

## What does it do?
It creates a self initialized fake based on a communication with a real server.  
On the first time that you run it, it runs in a `Recording` mode, means that it proxies the communication to a real server and at the same time saves the communication in to a file.    
On consecutive runs it'll start up in a `Playback` mode, means that it loads the data from the recorded run and behaves like the real server.


Let's see a real life example, we have this very basic `Weather` class that communicates with an external Weather service (https://www.metaweather.com/).
This class should have **`Integration Test`** to make sure that it functions correctly.


#### *Weather.java*
``` java
package io.sample.app;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Weather {

    private final String baseUrl;

    public Weather(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String forCity(String city) throws IOException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.name());
        return Request
                .Get(baseUrl + "/api/location/search/?query=" + encodedCity)
                .execute()
                .returnContent()
                .asString();
    }
}
```


That's how the test will look With `jsif` (Java SelfInitializingFake)


#### *WeatherIT.java*
``` java
package io.sample.app;

import io.jsif.SelfInitializedFake;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class WeatherIT {
    private static final int fakePort = 8087;
    private static final SelfInitializedFake fake =
            new SelfInitializedFake(fakePort,
                    "https://www.metaweather.com",
                    "weather_recordings");

    @BeforeClass
    public static void setUpBeforeClass() {
        fake.start();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        fake.stop();
    }

    @Test
    public void forCity() throws IOException {
        Weather weather = new Weather("http://localhost:" + fakePort);

        String res = weather.forCity("london");

        Assert.assertThat(res, CoreMatchers.containsString("\"latt_long\":\"51.506321,-0.12714\""));
    }
}
```



### How does it work?
When running for the first time, you'll see in the log   
``[main] INFO io.jsif.SelfInitializedFake - Starting in `Record` mode``  
And the `Fake` will initialize itself - It'll proxy the communication to the real external server and record it. The communication will be persisted in into a json file (under test/resources/weather_recordings)


The next time that you'll run it, you'll see in the log  
``[main] INFO io.jsif.SelfInitializedFake - Starting in `Play` mode``    
The `Fake` will run in `Play` mode now and it'll interact in the same way, but this time instead of proxying the traffic to the real server it'll use the persisted communication from the first run.




#### Under the hood?
This library uses `WireMock` library ([http://wiremock.org/](http://wiremock.org/)) for the recording & playback.    
It wraps `WireMock` and allows an easy `Self Initializing Fake`.