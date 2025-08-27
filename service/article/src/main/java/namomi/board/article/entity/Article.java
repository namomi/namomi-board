package namomi.board.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Table(name = "article")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article implements Persistable<Long> {

    @Id
    private Long articleId;
    private String title;
    private String content;
    private Long boardId; // shard key
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Article create(Long articleId, String title, String content, Long boardId, Long writerId) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.boardId = boardId;
        article.writerId = writerId;
        article.createdAt = LocalDateTime.now();
        article.modifiedAt = article.createdAt;
        return article;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        modifiedAt = LocalDateTime.now();
    }

    @Override
    public Long getId() {
        return articleId;
    }

    @Override
    public boolean isNew() {
        return articleId == null;
    }
}
