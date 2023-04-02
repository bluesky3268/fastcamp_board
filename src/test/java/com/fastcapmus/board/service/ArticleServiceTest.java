package com.fastcapmus.board.service;

import com.fastcapmus.board.domain.Article;
import com.fastcapmus.board.domain.UserAccount;
import com.fastcapmus.board.domain.type.SearchType;
import com.fastcapmus.board.dto.ArticleDto;
import com.fastcapmus.board.dto.ArticleWithCommentsDto;
import com.fastcapmus.board.dto.UserAccountDto;
import com.fastcapmus.board.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.*;

@DisplayName("비지니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks // Mock을 주입하는 대상
    private ArticleService sut; // 테스트 대상

    @Mock // Mock을 주입하는 대상을 제외한 나머지
    private ArticleRepository articleRepository;

    @DisplayName("검색어없이 게시글을 검색하면 게시글 페이지 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        // 제목, 내용, ID, 닉네임, 해시태그
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        // Then
        assertThat(articles).isEmpty();

        then(articleRepository).should().findAll(pageable);

    }

    @DisplayName("검색어와 함께 게시글을 검색하면 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParaments_whenSearchingArticles_thenReturnArticlepage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);

        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();

        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("게시글 조회하면 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given
        Long articleId = 1L;
        Article article = createArticle();

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        // 제목, 내용, ID, 닉네임, 해시태그
        ArticleWithCommentsDto dto = sut.getArticle(articleId);

        // Then
        assertThat(dto).hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());

        then(articleRepository).should().findById(articleId);

    }

    @DisplayName("없는 게시글을 조회하면, 예외를 발생시킨다.")
    @Test
    void givenNonexistentArticle_whenSearchingArticle_thenThrowsException() {
        // Given
        Long articleId = 0L;

        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getArticle(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 게시글이 없습니다. (articleId : " + articleId + ")");

        then(articleRepository).should().findById(articleId);

    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);

        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);

        // Then
        assertThat(articles).isEmpty();
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // given
        List<String> expectedHashtags = List.of("#java", "#spring", "springboot");
        given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

        // when
        List<String> actualHashtags = sut.getHashtags();

        // then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(articleRepository).should().findAllDistinctHashtags();
    }

    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnArticlesPage() {
        // Given
        String hashtag = "#java";
        Pageable pageable = Pageable.ofSize(20);

        given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));


        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtag, pageable);

        // Then
        assertThat(articles).isEmpty();
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtag(hashtag, pageable);
    }


    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSaveArticle() {
        /**
         * 실제로 저장이 되는것을 테스트하는 것(sociable test)이 아니라
         * solitary test(단순 서비스 레이어만 테스트)이다.
         */
        // Given
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.saveArticle(dto);

        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 수정할 정보를 입력하면, 게시글을 수정한다")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        /**
         * 실제로 저장이 되는것을 테스트하는 것(sociable test)이 아니라
         * solitary test(단순 서비스 레이어만 테스트)이다.
         */
        // Given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("new title", "new content", "#springboot");

        given(articleRepository.getReferenceById(dto.id())).willReturn(article);

        // When
        sut.updateArticle(dto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());

        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글 ID 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        /**
         * 실제로 저장이 되는것을 테스트하는 것(sociable test)이 아니라
         * solitary test(단순 서비스 레이어만 테스트)이다.
         */
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);


        // When
        sut.deleteArticle(articleId);

        // Then
        then(articleRepository).should().deleteById(articleId);
    }

    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다.")
    @Test
    void givenNothing_whenCOuntingArticles_thenReturnsArticleCount() {
        //given
        long expected = 0L;
        given(articleRepository.count()).willReturn(expected);

        // when
        long actual = sut.getArticleCount();

        // then
        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().count();
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "hyunbenny",
                "password",
                "hyunbenny@email.com",
                "hyunbenny",
                null
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                "hyunbenny",
                LocalDateTime.now(),
                "hyunbenny",
                LocalDateTime.now()
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "hyunbenny",
                "password",
                "hyunbenny@mail.com",
                "hyunbenny",
                "This is memo",
                "hyunbenny",
                LocalDateTime.now(),
                "hyunbenny",
                LocalDateTime.now()
        );
    }

}