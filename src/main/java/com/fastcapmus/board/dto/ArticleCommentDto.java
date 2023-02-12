package com.fastcapmus.board.dto;

import com.fastcapmus.board.domain.Article;
import com.fastcapmus.board.domain.ArticleComment;
import com.fastcapmus.board.domain.UserAccount;

import java.time.LocalDateTime;

public record ArticleCommentDto(

        Long id,

        Long articleId,

        UserAccountDto userAccountDto,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleCommentDto(id, articleId, userAccountDto, content, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public ArticleComment toEntity(Article entity) {
        return ArticleComment.of(
                userAccountDto.toEntity(),
                entity,
                content
        );
    }

}
