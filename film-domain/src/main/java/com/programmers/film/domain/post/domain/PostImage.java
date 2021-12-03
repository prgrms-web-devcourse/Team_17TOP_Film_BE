package com.programmers.film.domain.post.domain;

import com.programmers.film.domain.common.domain.ImageUrl;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_images")
public class PostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO : mapping with post detail
    @ManyToOne //two-way
    @JoinColumn(name = "post_detail_id")
    private PostDetail postDetail;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="originalSizeUrl", column = @Column(name = "original_url")),
        @AttributeOverride(name="smallSizeUrl", column = @Column(name = "small_size_url"))
    })
    private ImageUrl imageUrl;
}
