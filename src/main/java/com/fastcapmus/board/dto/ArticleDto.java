package com.fastcapmus.board.dto;

import com.fastcapmus.board.domain.Article;
import com.fastcapmus.board.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        Set<HashtagDto> hashtagDtos,
        String createdBy,
        LocalDateTime createdAt,

        String modifiedBy,

        LocalDateTime modifiedAt
) {
    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleDto(id, userAccountDto, title, content, hashtagDtos, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleDto of(UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos) {
        return new ArticleDto(null, userAccountDto, title, content, hashtagDtos, null, null, null, null);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(entity.getId()
                , UserAccountDto.from(entity.getUserAccount())
                , entity.getTitle()
                , entity.getContent()
                , entity.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet())
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
        );
    }

}
