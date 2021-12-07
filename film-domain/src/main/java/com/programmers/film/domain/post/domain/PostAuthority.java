package com.programmers.film.domain.post.domain;

import com.programmers.film.domain.user.domain.User;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_authorities")
public class PostAuthority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setUser(User user) {
        if (Objects.nonNull(this.user)) {
            this.user.getPostAuthorities().remove(this);
        }

        this.user = user;
    }

    public void setPost(Post post) {
        if (Objects.nonNull(this.post)) {
            this.post.getPostAuthorities().remove(this);
        }

        this.post = post;
    }
}
