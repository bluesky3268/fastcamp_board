package com.fastcapmus.board.dto;

import com.fastcapmus.board.domain.Article;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<ArticleCommentDto> articleCommentDtos,
        String title,
        String content,
        String hashtag,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {
    public static ArticleWithCommentsDto of(Long id, UserAccountDto userAccountDto, Set<ArticleCommentDto> articleCommentDtos, String title, String content, String hashtag, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleWithCommentsDto(id, userAccountDto, articleCommentDtos, title, content, hashtag, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return new ArticleWithCommentsDto(entity.getId()
                , UserAccountDto.from(entity.getUserAccount())
                , entity.getArticleComments().stream().map(ArticleCommentDto::from).collect(Collectors.toCollection(LinkedHashSet::new))
                , entity.getTitle()
                , entity.getContent()
                , entity.getHashtag()
                , entity.getCreatedBy()
                , entity.getCreatedAt()
                , entity.getModifiedBy()
                , entity.getModifiedAt()
        );
    }

}
