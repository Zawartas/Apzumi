package pl.app.Apzumi.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.application.port.PostsUseCase.UpdatePostCommand;
import pl.app.Apzumi.domain.Post;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PostController {

    PostsUseCase postsCatalog;

    @GetMapping(value = "/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAll(@RequestParam Optional<String> text) {
        if (text.isPresent()) {
            return postsCatalog.findByTitle(text);
        }
        return postsCatalog.findAll();
    }

    @PatchMapping(value = "/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePost(@PathVariable(value = "id") Long id, @RequestBody UpdatePostCommand command) {
        command.setId(id);
        try {
            postsCatalog.updatePost(command);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/post/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        postsCatalog.removeById(id);
    }
}
