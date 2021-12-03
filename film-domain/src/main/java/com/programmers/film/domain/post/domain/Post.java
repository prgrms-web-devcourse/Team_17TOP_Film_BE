package com.programmers.film.domain.post.domain;

import com.programmers.film.domain.common.domain.BaseEntity;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.member.domain.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Authority> authorities = new ArrayList<>();

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
    private String preview_text;

    @Column(name = "available_at")
    @Temporal(TemporalType.DATE)
    private Date availableAt;

    @Embedded
    private Point location;

    public String getStrAvailableAt() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(this.availableAt);
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.setPost(this);
    }

    public void setAuthor(User author) {
        if (Objects.nonNull(this.author)) {
            this.author.getPosts().remove(this);
        }

        this.author = author;
    }


}
