package pl.app.Apzumi.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.app.Apzumi.application.port.PostsUseCase;
import pl.app.Apzumi.db.PostRepository;
import pl.app.Apzumi.domain.Post;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
public class UpdatePostsJob {

    RestTemplate restTemplate;
    PostsUseCase postsUseCase;
    PostRepository repository;
    String requestUrl;

    public UpdatePostsJob(RestTemplate restTemplate,
                          PostsUseCase postsUseCase,
                          PostRepository repository,
                          @Value("${app.value.address.for.http.init.request}") String requestUrl) {
        this.restTemplate = restTemplate;
        this.postsUseCase = postsUseCase;
        this.repository = repository;
        this.requestUrl = requestUrl;
    }

    @Transactional
    @Scheduled(cron = "${app.update-cron}")
    public void run() {
        log.info("Daily job starting___");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        Post[] posts = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, Post[].class).getBody();
        Arrays.stream(posts)
                .forEach(post -> {
                    final Optional<Post> postFound = postsUseCase.findById(post.getId());
                    if (postFound.isPresent() && !postFound.get().isUpdated()) {
                        repository.save(postFound.get());
                    }
                });
    }
}
