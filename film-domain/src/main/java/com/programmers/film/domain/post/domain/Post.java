package com.programmers.film.domain.post.domain;

import com.programmers.film.common.error.exception.InvalidInputValueException;
import com.programmers.film.domain.common.domain.BaseEntity;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.member.domain.User;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostAuthority> postAuthorities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private PostState state;

    @NotNull
    @Column(nullable = false)
    private String title;

    @Column(name = "preview_text")
    private String previewText;

    @Column(name = "available_at")
    private LocalDate availableAt;

    @Embedded
    private Point location;

    public String getStrAvailableAt() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(this.availableAt);
    }

    public void addPostAuthority(PostAuthority authority) {
        postAuthorities.add(authority);
        authority.setPost(this);
    }

    public void setAuthor(User author) {
        if (Objects.nonNull(this.author)) {
            this.author.getPosts().remove(this);
        }

        this.author = author;
    }

    public Long deletePost() {
        LocalDateTime now = LocalDateTime.now();
        setDeletedAt(now);
        setIsDeleted(1);
        if(getDeletedAt() != now || getIsDeleted() != 1) {
            throw new InvalidInputValueException("게시물 삭제 오류");
        }
        return id;
    }
}
