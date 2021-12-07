package com.programmers.film.domain.user.domain;

import com.programmers.film.domain.common.domain.BaseEntity;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.Authority;
import com.programmers.film.domain.post.domain.Post;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Authority> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "originalSizeUrl", column = @Column(name = "profile_image")),
		@AttributeOverride(name = "smallSizeUrl", column = @Column(name = "profile_thumbnail_image"))
	})
	private ImageUrl profileImageUrl;

	private LocalDateTime lastLoginAt;

	@Builder
	public User(Long id, String nickname, String provider) {

		this.id = id;
		this.nickname = nickname;
		this.provider = provider;
	}

	public static User of(String nickname, String provider) {
		return User.builder()
			.nickname(nickname)
			.provider(provider)
			.build();
	}

	public void addAuthority(Authority authority) {
		authorities.add(authority);
		authority.setUser(this);
	}

	public void addPost(Post post) {
		posts.add(post);
		post.setAuthor(this);
	}
}
