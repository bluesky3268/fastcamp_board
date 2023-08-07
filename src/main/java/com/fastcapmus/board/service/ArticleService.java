package com.fastcapmus.board.service;

import com.fastcapmus.board.domain.Article;
import com.fastcapmus.board.domain.UserAccount;
import com.fastcapmus.board.domain.type.SearchType;
import com.fastcapmus.board.dto.ArticleDto;
import com.fastcapmus.board.dto.ArticleWithCommentsDto;
import com.fastcapmus.board.repository.ArticleRepository;
import com.fastcapmus.board.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;


    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank())
            return articleRepository.findAll(pageable).map(article -> ArticleDto.from(article));

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(article -> ArticleDto.from(article));
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(article -> ArticleDto.from(article));
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(article -> ArticleDto.from(article));
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(article -> ArticleDto.from(article));
            case HASHTAG -> articleRepository.findByHashtagNames(Arrays.stream(searchKeyword.split(" ")).toList(), pageable).map(article -> ArticleDto.from(article));
        };

    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다. - (articleId : " + articleId  + ")"));
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(article -> ArticleWithCommentsDto.from(article))
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다. (articleId : " + articleId + ")"));
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleRepository.save(dto.toEntity(userAccount));
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if (article.getUserAccount().equals(userAccount)) {
                // not null 조건인 필드(컬럼)들은 null체크하는 방어 로직을 짜줘야 한다.
                if (dto.title() != null) article.setTitle(dto.title());
                if (dto.content() != null) article.setContent(dto.content());
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 수정 실패 : 해당 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다. : {}", e.getLocalizedMessage());
        }
    }

    public void deleteArticle(Long articleId, String userId) {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if(hashtag == null || hashtag.isBlank()) return Page.empty(pageable);

        return articleRepository.findByHashtagNames(null, pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
