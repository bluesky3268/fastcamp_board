package com.fastcapmus.board.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Spring Data REST 통합테스트는 불필요하여 제외함")
@DisplayName("Data REST 테스트")
@Transactional // DB에 실제로 쿼리가 나가는 테스트이기 때문에 @Transactional을 붙여주는게 좋다.
@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest
/**
 * localhost:8080/api를 찍고 들어가서 HAL Explorer로 확인해보고
 * 직접 localhost:8080/api/articles를 찍어도 결과값은 정상적으로 받아지는데 테스트는 404가 뜬다.
 * 이유는 @WebMvcTest는
 * Controller이외의 빈들을 로드하지 않고 컨트롤러와 연관된 빈들만 최소한으로 읽어들이기 때문에
 * Data Rest의 AutoConfiguration을 읽지 않아서 테스트 오류가 발생하는 것이다.
 *
 * 이를 해결하기 위해서 @SpringBootTest를 사용한다.
 * 하지만 MockMvc를 알 수 없기 때문에 @AutoConfigureMockMvc를 같이 붙여준다.
 */
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void getArticlesTest() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void getArticleTest() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글의 댓글 리스트 조회")
    @Test
    void getCommentsOfArticle() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void getArticleComments() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void getComment() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 회원 관련된 API는 일절 제공하지 않는다.")
    @Test
    void whenRequestUserAccount_thenThrowException() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/userAccoutns")).andExpect(status().isNotFound());
        mvc.perform(post("/api/userAccoutns")).andExpect(status().isNotFound());
        mvc.perform(put("/api/userAccoutns")).andExpect(status().isNotFound());
        mvc.perform(patch("/api/userAccoutns")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/userAccoutns")).andExpect(status().isNotFound());
        mvc.perform(head("/api/userAccoutns")).andExpect(status().isNotFound());
    }
}
