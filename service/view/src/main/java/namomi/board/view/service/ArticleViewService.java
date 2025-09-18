package namomi.board.view.service;

import lombok.RequiredArgsConstructor;
import namomi.board.view.repository.ArticleViewCountBackUpRepository;
import namomi.board.view.repository.ArticleViewCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;
    private static final int BACK_UP_BACH_SIZE = 100;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;

    @Transactional
    public Long increase(Long articleId, Long userId) {
        Long count = articleViewCountRepository.increase(articleId);
        if (count % BACK_UP_BACH_SIZE == 0) {
            articleViewCountBackUpProcessor.backUp(articleId, count);
        }
        return count;
    }

    @Transactional(readOnly = true)
    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }


}
