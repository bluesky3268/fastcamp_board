package com.fastcapmus.board.dto.request;

import com.fastcapmus.board.dto.ArticleCommentDto;
import com.fastcapmus.board.dto.ArticleDto;
import com.fastcapmus.board.dto.UserAccountDto;

public record ArticleCommentRequest(

        Long articleId,
        String content

) {
    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
          articleId,
          userAccountDto,
          content
        );
    }
}
