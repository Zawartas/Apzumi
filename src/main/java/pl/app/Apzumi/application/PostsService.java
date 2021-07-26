package pl.app.Apzumi.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.db.PostRepository;
import pl.app.Apzumi.domain.Post;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostsService implements PostsUseCase {

    PostRepository repository;

    @Override
    public void savePosts(Post[] posts) {
        repository.saveAll(Arrays.stream(posts).collect(Collectors.toList()));
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Post updatePost(UpdatePostCommand command) {
        return repository.findById(command.getId())
                .map(updatedPost -> {
                    updateFields(command, updatedPost);
                    updatedPost.setUpdated(true);
                    repository.save(updatedPost);
                    return updatedPost;
                })
                .orElseThrow(() -> new IllegalArgumentException("No post with id: " + command.getId()));
    }

    @Override
    public List<Post> findByTitle(Optional<String> title) {
        return repository.findByTitleIsContainingIgnoreCase(title);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return repository.findAll();
    }

    private Post updateFields(UpdatePostCommand command, Post post) {
        if (command.getTitle() != null) {
            post.setTitle(command.getTitle());
        }
        if (command.getBody() != null) {
            post.setBody(command.getBody());
        }
        return post;
    }
}
