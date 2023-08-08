package com.fastcapmus.board.service;

import com.fastcapmus.board.domain.Article;
import com.fastcapmus.board.domain.ArticleComment;
import com.fastcapmus.board.domain.UserAccount;
import com.fastcapmus.board.dto.ArticleCommentDto;
import com.fastcapmus.board.dto.UserAccountDto;
import com.fastcapmus.board.repository.ArticleCommentRepository;
import com.fastcapmus.board.repository.ArticleRepository;
import com.fastcapmus.board.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId).stream().map(ArticleCommentDto::from).toList();
    }

    public void saveArticleComment(ArticleCommentDto dto) {
        Article article = null;
        UserAccount user = null;
        try {
            user = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패 - 회원 정보를 찾을 수 없습니다. : {}", dto.userAccountDto().userId());
            return;
        }

        try {
            article = articleRepository.getReferenceById(dto.articleId());
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패 - 저장하려는 댓글의 게시글을 찾을 수 없습니다. : {}", e.getLocalizedMessage());
            return;
        }

        ArticleComment articleComment = dto.toEntity(article, user);
        if (dto.parentCommentId() != null) {
            ArticleComment parentComment = articleCommentRepository.getReferenceById(dto.parentCommentId());
            parentComment.addChildComment(articleComment);
        } else{
            articleCommentRepository.save(articleComment);
        }

        // 가독성을 높이자
//        if (dto.isParentComment()) {
//            ArticleComment parentComment = articleCommentRepository.getReferenceById(dto.parentCommentId());
//            parentComment.addChildComment(articleComment);
//        }else{
//            articleCommentRepository.save(articleComment);
//        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try{
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if(dto.content() != null) articleComment.setContent(dto.content());
        }catch (EntityNotFoundException e) {
            log.warn("댓글 수정 실패 - 해당 댓글을 찾을 수 없습니다. : {}", dto.id());
        }
    }

    public void deleteArticleComment(Long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }
}
