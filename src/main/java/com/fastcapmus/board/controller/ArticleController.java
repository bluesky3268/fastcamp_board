package com.fastcapmus.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */
@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String articles(ModelMap modelMap) {
        modelMap.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String articles(@PathVariable Long articleId, ModelMap modelMap) {
        modelMap.addAttribute("article", "article"); // TODO : 실제 데이터를 DTO로 넣어줘야 함
        modelMap.addAttribute("articleComments", List.of());

        return "articles/detail";
    }
}
