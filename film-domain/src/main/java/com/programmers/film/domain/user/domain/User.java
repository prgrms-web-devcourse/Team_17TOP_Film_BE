package com.programmers.film.domain.user.domain;

import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.domain.auth.domain.Auth;
import com.programmers.film.domain.common.domain.BaseEntity;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"nickname"})}
)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 20, name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "provider")
    private String provider;

    @NotNull
    @Column(name = "provider_id")
    private String providerId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "originalSizeUrl", column = @Column(name = "profile_image")),
        @AttributeOverride(name = "smallSizeUrl", column = @Column(name = "profile_thumbnail_image"))
    })
    private ImageUrl profileImageUrl;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PostAuthority> postAuthorities = new ArrayList<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public User(Long id, String nickname, ImageUrl profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void setProvider(String provider, String providerId) {
        checkArgument(provider != null, "provider must be provided.");
        checkArgument(providerId != null, "providerId must be provided.");

        this.provider = provider;
        this.providerId = providerId;
    }

    public void addAuthority(PostAuthority authority) {
        postAuthorities.add(authority);
        authority.setUser(this);
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setAuthor(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("nickname", nickname)
            .append("provider", provider)
            .append("providerId", providerId)
            .append("profileImageUrl", profileImageUrl)
            .append("lastLoginAt", lastLoginAt)
            .append("postAuthorities", postAuthorities)
            .append("posts", posts)
            .toString();
    }
}