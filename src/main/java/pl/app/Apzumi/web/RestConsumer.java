package pl.app.Apzumi.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.domain.Post;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class RestConsumer {

    private String REQUEST_URL;
    RestTemplate restTemplate;
    PostsUseCase postsCatalog;

    public RestConsumer(@Value("${app.value.address.for.http.init.request}") String REQUEST_URL,
                        RestTemplate restTemplate,
                        PostsUseCase postsCatalog) {
        this.REQUEST_URL = REQUEST_URL;
        this.restTemplate = restTemplate;
        this.postsCatalog = postsCatalog;
    }

    @RequestMapping(value = "/posts")
    public Post[] getAll(@RequestParam Optional<String> text) {
        Post[] posts = restTemplate.getForObject(REQUEST_URL, Post[].class);
        if (text.isPresent()) {
            posts = Arrays.stream(posts)
                    .filter(post -> post.getTitle().contains(text.get()))
                    .toArray(Post[]::new);
        }
        postsCatalog.savePosts(posts);
        return posts;
    }
}
