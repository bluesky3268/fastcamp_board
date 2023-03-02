package com.fastcapmus.board.controller;

import com.fastcapmus.board.domain.type.SearchType;
import com.fastcapmus.board.dto.response.ArticleResponse;
import com.fastcapmus.board.dto.response.ArticleWithCommentsResponse;
import com.fastcapmus.board.service.ArticleService;
import com.fastcapmus.board.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @GetMapping
    public String articles(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue,
                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                           ModelMap modelMap) {
        log.info("searchType : {}, searchValue : {}", searchType, searchValue);

        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumber = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        modelMap.addAttribute("articles", articles);
        modelMap.addAttribute("paginationBarNumbers", barNumber);

        log.info(modelMap.get("articles").toString());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String articles(@PathVariable Long articleId, ModelMap modelMap) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
        log.info("article : {}", article);
        modelMap.addAttribute("article", article);
        modelMap.addAttribute("articleComments", article.articleCommentsResponse());
        modelMap.addAttribute("totalCount", articleService.getArticleCount());

        return "articles/detail";
    }
}
