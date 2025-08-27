package namomi.board.article.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kuke.board.common.snowflake.Snowflake;
import lombok.extern.log4j.Log4j2;
import namomi.board.article.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Log4j2
public class DataInitializer {
    @PersistenceContext
    EntityManager em;

    @Autowired
    TransactionTemplate template;
    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 6000;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.submit(() -> {
                insert();
                latch.countDown();
                log.info("latch.getCount(): {}", latch.getCount());
            });
        }
        latch.await();
        executorService.shutdown();
    }

    void insert() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Article article = Article.create(
                        snowflake.nextId(),
                        "title" + i,
                        "content" + i,
                        1L,
                        1L
                );
                em.persist(article);
            }
        });
    }
}
