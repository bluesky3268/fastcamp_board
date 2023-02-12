package com.fastcapmus.board.service;

import com.fastcapmus.board.domain.type.SearchType;
import com.fastcapmus.board.dto.ArticleDto;
import com.fastcapmus.board.dto.ArticleWithCommentsDto;
import com.fastcapmus.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        return Page.empty();
//        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {
    }

    public void updateArticle(ArticleDto dto) {

    }

    public void deleteArticle(Long articleId) {

    }
}
