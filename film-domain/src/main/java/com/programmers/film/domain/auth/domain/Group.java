package com.programmers.film.domain.auth.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Entity
@Table(name = "groups")
public class Group {

	@OneToMany(mappedBy = "group")
	private final List<GroupPermission> permissions = new ArrayList<>();

	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	private String name;

	public List<GrantedAuthority> getAuthorities() {
		return permissions.stream()
			.map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
			.collect(toList());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", id)
			.append("name", name)
			.append("authorities", getAuthorities())
			.toString();
	}

}
