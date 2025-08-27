package namomi.board.article.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import namomi.board.article.service.response.ArticlePageResponse;
import namomi.board.article.service.response.ArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

@Slf4j
public class ArticleApiTest {

    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));
        log.info("response: {}", response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(218295677122854912L);
        log.info("response: {}", response);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest() {
        update(218295677122854912L);
        ArticleResponse response = read(218295677122854912L);
        log.info("response: {}", response);
    }

    void update(long articleId) {
        restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("hi 2", "my content  22"))
                .retrieve();
    }

    @Test
    void deleteTest() {
        restClient.delete()
                .uri("/v1/articles/{articleId}", 218295677122854912L)
                .retrieve();
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&pageSize=30&page=1")
                .retrieve()
                .body(ArticlePageResponse.class);

        log.info("response.getArticleCount(): = {}", response.getArticleCount());
        for (ArticleResponse article : response.getArticles()) {
            log.info("article: {}", article);
        }
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }

}
