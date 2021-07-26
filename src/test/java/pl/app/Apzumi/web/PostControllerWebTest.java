package pl.app.Apzumi.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.domain.Post;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PostController.class})
public class PostControllerWebTest {

    @MockBean
    PostsUseCase postsUseCase;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldGetAllPosts() throws Exception {
        // given
        Post post1 = new Post(1, 100L, "Title of some sort", "Something", false);
        Post post2 = new Post(1, 101L, "Another title", "Something less interesting", false);

        // when
        Mockito.when(postsUseCase.findAll()).thenReturn(List.of(post1, post2));

        // expect
        mockMvc.perform(get("/api/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
