package com.fastcapmus.board.controller;

import com.fastcapmus.board.config.SecurityConfig;
import com.fastcapmus.board.config.TestSecurityConfig;
import com.fastcapmus.board.dto.ArticleCommentDto;
import com.fastcapmus.board.dto.request.ArticleCommentRequest;
import com.fastcapmus.board.service.ArticleCommentService;
import com.fastcapmus.board.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean
    private ArticleCommentService articleCommentService;

    public ArticleCommentControllerTest(@Autowired MockMvc mvc,
                                        @Autowired FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsService", setupBefore = TEST_EXECUTION)
    @DisplayName("[view][POST] 댓글 등록 - 정상호출")
    @Test
    void givenArticleCommentInfo_whenRequest_thenSaveArticleComment() throws Exception {
        // given
        long articleId = 1L;
        ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "article comment test");

        willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));

        // when then
        mvc.perform(post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(request))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));

        then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
    }

    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "userDetailsService", setupBefore = TEST_EXECUTION)
    @DisplayName("[view][POST] 댓글 삭제 - 정상호출")
    @Test
    void givenArticleCommentIdToDelete_WhenRequest_thenDeletesArticleComment() throws Exception {
        // given
        long articleId = 1L;
        long articleCommentId = 1L;
        String userId = "testuser";

        willDoNothing().given(articleCommentService).deleteArticleComment(articleCommentId, userId);


        //when then
        mvc.perform(post("/comments/" + articleCommentId + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(Map.of("articleId", articleId)))
                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));

        then(articleCommentService).should().deleteArticleComment(articleCommentId, userId);
    }


}
