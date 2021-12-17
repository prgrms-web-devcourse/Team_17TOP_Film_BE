package com.programmers.film.domain.auth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Getter
@Table(name = "group_permission")
public class GroupPermission {

	@Id
	@Column(name = "id")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne(optional = false)
	@JoinColumn(name = "permission_id")
	private Permission permission;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", id)
			.append("group", group)
			.append("permission", permission)
			.toString();
	}
}