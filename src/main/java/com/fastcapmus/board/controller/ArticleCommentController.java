package com.fastcapmus.board.controller;

import com.fastcapmus.board.domain.UserAccount;
import com.fastcapmus.board.dto.ArticleCommentDto;
import com.fastcapmus.board.dto.UserAccountDto;
import com.fastcapmus.board.dto.request.ArticleCommentRequest;
import com.fastcapmus.board.dto.security.BoardPrincipal;
import com.fastcapmus.board.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/comments")
@Controller
@RequiredArgsConstructor
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    /**
     * 댓글의 조회는 이미 게시글을 조회할 때 같이 조회를 하고 있음
     * 따라서 댓글의 등록과 삭제에 대한 기능만 구현해준다.
     */

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest request,
                                        @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleCommentService.saveArticleComment(request.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/" + request.articleId();
    }

    /**
     * FORM 은 GET, POST만 사용할 수 있다.(PUT, DELETE 메서드를 사용할 수 없다.)
     * 우회하는 방법도 있지만 http표준을 준수하기 위해서 사용하지 않기로 함.
     */
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                Long articleId,
                                @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleCommentService.deleteArticleComment(commentId, boardPrincipal.getUsername());
        return "redirect:/articles/" + articleId;
    }

}
