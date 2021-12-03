package com.programmers.film.domain.post.domain;

import com.programmers.film.domain.member.domain.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authorities")
public class Authority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : mapping with users
    @ManyToOne //two-way
    @JoinColumn(name = "member_id")
    private User user;

    // TODO : mapping with posts
    @ManyToOne //two-way
    @JoinColumn(name = "post_id")
    private Post post;
}
