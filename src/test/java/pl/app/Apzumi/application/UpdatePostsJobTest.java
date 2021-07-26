package pl.app.Apzumi.application;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.client.RestTemplate;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.db.PostRepository;
import pl.app.Apzumi.web.RestConsumer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
class UpdatePostsJobTest {

    @SpyBean
    private UpdatePostsJob job;

    @Autowired
    PostRepository repository;

    @Autowired
    PostsUseCase postsUseCase;

    @Autowired
    PostsService postsService;

    private MockWebServer mockWebServer;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void scheduledJobDoesNotUpdateAlreadyUpdatedRecords() {

        // given
        String json = "[{\"userId\": 1, \"id\": 100, \"title\":\"example title\", \"body\":\"example body\"}," +
                "{\"userId\": 1, \"id\": 101, \"title\":\"example title 2\", \"body\":\"example body 2\"}]";

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json)
                .setResponseCode(200));
        RestConsumer restConsumer =
                new RestConsumer(mockWebServer.url("/").toString(),
                        restTemplate, postsUseCase);

        // when
        PostsUseCase.UpdatePostCommand command =
                new PostsUseCase.UpdatePostCommand(101L, "Changed title", "Changed Body");
        postsUseCase.updatePost(command);
        job.run();

        // then
        assertEquals("Changed title", postsUseCase.findById(101L).get().getTitle());

    }
}