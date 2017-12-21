package e2e;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.jsif.player.FakeServer;
import io.jsif.recorder.Recorder;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CreateAndUseFake {
    private final static int dummyPort = 7088;
    private final static int proxyPort = 7089;
    private final String recordingsPath = "some_path";
    private final String baseUrl = "http://localhost";
    private String httpPath = "/some/thing";

    private WireMockServer setUpDummyRemoteServer() {
        WireMockServer dummy = new WireMockServer(dummyPort);
        dummy.stubFor(get(urlEqualTo(httpPath))
                .willReturn(aResponse()
                        .withBody("Hello world!" + UUID.randomUUID().toString())));
        dummy.start();
        return dummy;
    }

    private String doHttpCall(int port) throws IOException {
        Content content = Request.Get(baseUrl + ":" + port + httpPath)
                .execute().returnContent();
        return content.asString();
    }

    @Test
    public void test() throws IOException {
        // Start up recorder
        Recorder recorder = new Recorder(proxyPort, baseUrl + ":" + dummyPort, recordingsPath);
        recorder.record();

        // set up dummy server
        WireMockServer dummy = setUpDummyRemoteServer();

        // do call proxying via jisf proxy (test -> recorder -> dummy -> recorder -> test)
        String resultFromRemote = doHttpCall(proxyPort);

        // Now we can stop both remote server & recorder proxy
        recorder.stop();
        dummy.stop();

        // Let's load recorded data and start the fake
        FakeServer fakeServer = new FakeServer(proxyPort, recordingsPath);
        fakeServer.start();

        // This call will go to the recorded fake
        String recordedResult = doHttpCall(proxyPort);

        Assert.assertEquals(resultFromRemote, recordedResult);
    }
}