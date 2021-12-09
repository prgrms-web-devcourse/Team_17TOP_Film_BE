package com.programmers.film.domain.auth.domain;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;

import com.programmers.film.domain.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Getter
@Table(name = "auths")
public class Auth {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "provider")
	private String provider;

	@Column(name = "provider_id")
	private String providerId;

	@Column(name = "profile_image")
	private String profileImage;

	@ManyToOne(optional = false)
	@JoinColumn(name = "group_id")
	private Group group;

	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	protected Auth() {/*no-op*/}

	public Auth(String username, String provider, String providerId, String profileImage,
		Group group) {
		checkArgument(isNotEmpty(username), "username must be provided.");
		checkArgument(isNotEmpty(provider), "provider must be provided.");
		checkArgument(isNotEmpty(providerId), "providerId must be provided.");
		checkArgument(group != null, "group must be provided.");

		this.username = username;
		this.provider = provider;
		this.providerId = providerId;
		this.profileImage = profileImage;
		this.group = group;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", id)
			.append("username", username)
			.append("provider", provider)
			.append("providerId", providerId)
			.append("profileImage", profileImage)
			.toString();
	}

}
