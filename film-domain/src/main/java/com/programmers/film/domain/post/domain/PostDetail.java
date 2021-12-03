package com.programmers.film.domain.post.domain;

import com.programmers.film.domain.member.domain.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_details")
public class PostDetail {

    @Id
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private Post post;

    // TODO : mapping with post images
    // two-way
    @OneToMany(mappedBy = "post_detail", orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "opner_id")
    private User user;

    @Column(name = "opened_at")
    @Temporal(TemporalType.DATE)
    private Date openedAt;

    private String content;


}
