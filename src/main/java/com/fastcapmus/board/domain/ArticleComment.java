package com.fastcapmus.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdBy"),
        @Index(columnList = "createdAt")
})
@Entity
public class ArticleComment extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "articleId")
    private Article article; // 게시글(id)

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserAccount userAccount; // 유저 정보

    @Setter
    @Column(nullable = true, updatable = false)
    private Long parentCommentId; // 부모댓글(id)

    @ToString.Exclude
    @OrderBy("createdAt ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<ArticleComment> childComments = new LinkedHashSet<>();

    @Setter
    @Column(nullable = false, length = 500)
    private String content; // 내용

    protected ArticleComment() {
    }

    private ArticleComment(UserAccount userAccount, Article article, Long parentCommentId, String content) {
        this.userAccount = userAccount;
        this.article = article;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public static ArticleComment of(UserAccount userAccount, Article article, String content) {
        return new ArticleComment(userAccount, article, null, content);
    }

    public void addChildComment(ArticleComment child) {
        child.setParentCommentId(this.id);
        this.getChildComments().add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
