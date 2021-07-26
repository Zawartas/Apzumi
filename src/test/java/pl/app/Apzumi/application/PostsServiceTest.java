package pl.app.Apzumi.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.app.Apzumi.ApzumiApplication;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.db.PostRepository;
import pl.app.Apzumi.domain.Post;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostsServiceTest {

    @Autowired
    PostRepository repository;

    @Autowired
    PostsUseCase postsUseCase;

    @Test
    public void getAllPostFromRepository() {
        Post[] posts = {
                new Post(1, 100L, "Title of some sort", "Something", false),
                new Post(1, 101L, "Another title", "Something less interesting", false)
        };

        postsUseCase.savePosts(posts);

        assertEquals(2, repository.count());
    }

    @Test
    public void userCanUpdatePost() {
        Post[] posts = {
                new Post(1, 100L, "Title of some sort", "Something", false),
                new Post(1, 101L, "Another title", "Something less interesting", false)
        };

        postsUseCase.savePosts(posts);
        PostsUseCase.UpdatePostCommand command =
                new PostsUseCase.UpdatePostCommand(101L, "Changed title", "Changed Body");
        postsUseCase.updatePost(command);

        assertTrue(postsUseCase.findById(101L).get().isUpdated());
        assertEquals("Changed title", postsUseCase.findById(101L).get().getTitle());

    }
}