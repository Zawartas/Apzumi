package pl.app.Apzumi.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.app.Apzumi.domain.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

public interface PostsUseCase {

    void savePosts(Post[] posts);
    void removeById(Long id);
    Post updatePost(UpdatePostCommand command);
    List<Post> findByTitle(Optional<String> title);
    Optional<Post> findById(Long id);
    List<Post> findAll();

    @Data
    @AllArgsConstructor
    class UpdatePostCommand {

        Long id;

        @NotBlank(message = "Please provide a title")
        private String title;

        @NotEmpty(message = "Please provide an author")
        private String body;
    }

}
