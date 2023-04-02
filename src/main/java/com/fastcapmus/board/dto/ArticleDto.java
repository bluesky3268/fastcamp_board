package com.fastcapmus.board.dto;

import com.fastcapmus.board.domain.Article;
import com.fastcapmus.board.domain.UserAccount;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        String createdBy,
        LocalDateTime createdAt,

        String modifiedBy,

        LocalDateTime modifiedAt
) {
    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleDto of(UserAccountDto userAccountDto, String title, String content, String hashtag) {
        return new ArticleDto(null, userAccountDto, title, content, hashtag, null, null, null, null);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(entity.getId()
                , UserAccountDto.from(entity.getUserAccount())
                , entity.getTitle()
                , entity.getContent()
                , entity.getHashtag()
                , entity.getCreatedBy()
                , entity.getCreatedAt()
                , entity.getModifiedBy()
                , entity.getModifiedAt()
        );
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.of(
                userAccount
                , title
                , content
                , hashtag
        );
    }

}
