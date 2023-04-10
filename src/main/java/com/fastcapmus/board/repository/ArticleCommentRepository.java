package com.fastcapmus.board.repository;

import com.fastcapmus.board.domain.ArticleComment;
import com.fastcapmus.board.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>
        , QuerydslPredicateExecutor<ArticleComment>  // Data REST를 이용하여 기본적으로 제공하는 조회 기능(예 : /api/articles?title=Quisque ut erat.) 사용을 위해서 추가
        , QuerydslBinderCustomizer<QArticleComment> // 내 입맛에 맞게 사용하기 위해서 추가
{

    List<ArticleComment> findByArticle_Id(Long articleId);

    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 리스팅을 하지 않은 프로퍼티를 검색에서 제외하기 -> true로 변경(기본값 : false)
        bindings.excludeUnlistedProperties(true);
        // 원하는 필드를 추가
        bindings.including(root.content, root.createdAt, root.createdBy);
        // == 매치가 아니라 like 매치로 변경
        bindings.bind(root.content).first((path, value) -> path.containsIgnoreCase(value));
        bindings.bind(root.createdBy).first((path, value) -> path.containsIgnoreCase(value));
        bindings.bind(root.createdAt).first((path, value) -> path.eq(value)); // 시분초까지 동일해야 하기 때문에 권장되는 방식이 아님 -> 일단은 해놓자
    }

}
