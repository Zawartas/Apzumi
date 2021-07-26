package pl.app.Apzumi.web;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import pl.app.Apzumi.application.port.PostsUseCase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestClientTest(RestConsumer.class)
public class RestConsumerTest {

    private MockWebServer mockWebServer;
    @Autowired
    private RestTemplate restTemplate;
    @MockBean
    private PostsUseCase postsCatalog;

    @Test
    public void testGetAllPostsFromRemoteServer() {

        String json = "[{\"userId\": 1, \"id\": 1, \"title\":\"example title\", \"body\":\"example body\"}," +
                "{\"userId\": 1, \"id\": 2, \"title\":\"example title 2\", \"body\":\"example body 2\"}]";

        MockWebServer mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json)
                .setResponseCode(200));

        RestConsumer restConsumer =
                new RestConsumer(mockWebServer.url("/").toString(),
                        restTemplate, postsCatalog);

        assertEquals(2, restConsumer.getAll(Optional.empty()).length);
    }

}
