package com.programmers.film.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;

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
}