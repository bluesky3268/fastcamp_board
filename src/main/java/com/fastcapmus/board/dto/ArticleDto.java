package com.fastcapmus.board.dto;

import com.fastcapmus.board.domain.Article;

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

    public Article toEntity() {
        return Article.of(
                userAccountDto.toEntity()
                , title
                , content
                , hashtag
        );
    }

}
