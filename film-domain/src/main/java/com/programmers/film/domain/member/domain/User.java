package com.programmers.film.domain.member.domain;

import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"nickname"})}
)
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 20, name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "provider")
    private String provider;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<PostAuthority> postAuthorities = new ArrayList<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // TODO : mapping with kakao

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="originalSizeUrl", column = @Column(name = "profile_image")),
        @AttributeOverride(name="smallSizeUrl", column = @Column(name = "profile_thumbnail_image"))
    })
    private ImageUrl profileImageUrl;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    public void addPostAuthority(PostAuthority authority) {
        postAuthorities.add(authority);
        authority.setUser(this);
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setAuthor(this);
    }
}
