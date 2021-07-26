package pl.app.Apzumi.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.domain.Post;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class PostControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    PostsUseCase postsUseCase;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getAllPosts() {
        // given
        Post post1 = new Post(1, 100L, "Title of some sort", "Something", false);
        Post post2 = new Post(1, 101L, "Another title", "Something less interesting", false);
        Mockito.when(postsUseCase.findAll()).thenReturn(List.of(post1, post2));
        ParameterizedTypeReference<List<Post>> type = new ParameterizedTypeReference<>() {
        };

        // when
        RequestEntity<Void> request = RequestEntity.get(URI.create("http://localhost:" + port + "/api/posts")).build();
        ResponseEntity<List<Post>> response = restTemplate.exchange(request, type);

        // then
        assertEquals(2, response.getBody().size());
    }
}