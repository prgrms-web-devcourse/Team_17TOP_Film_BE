package com.programmers.film.domain.post.domain;

import com.programmers.film.domain.user.domain.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_details")
@AllArgsConstructor
public class PostDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postDetail", orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opener_id")
    private User opener;

    @Column(name = "opened_at")
    private LocalDate openedAt;

    @Lob
    @Column(name = "content")
    private String content;

    public void addPostImage(PostImage postImage) {
        postImages.add(postImage);
        postImage.setPostDetail(this);
    }

    public void firstOpen(User user){
        this.opener = user;
        this.openedAt = LocalDate.now();

    }

    public void setPost(Post post){
        this.post=post;
    }

    public PostDetail(String content){
        this.content = content;
    }

}
