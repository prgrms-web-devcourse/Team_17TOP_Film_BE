package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final PointConverter pointConverter;

    @Transactional(readOnly = true)
    public List<AuthorityImage> getAuthorityImageList(Post post) {
        List<PostAuthority> postAuthorities = post.getPostAuthorities();
        List<AuthorityImage> authorityImageList = new ArrayList<>();
        for(int i = 0; i < postAuthorities.size(); i++) {
            PostAuthority postAuthority = postAuthorities.get(i);
            authorityImageList.add(
                AuthorityImage.builder()
                    .authorityId(postAuthority.getUser().getId())
                    .imageOrder(i)
                    .imageUrl(postAuthority.getUser().getProfileImageUrl().getOriginalSizeUrl())
                    .build()
            );
        }
        return authorityImageList;
    }

    @Transactional(readOnly = true)
    public PreviewPostResponse PostToPreviewPostResponse(Post post) {
        List<PostAuthority> postAuthorities = post.getPostAuthorities();
        return PreviewPostResponse.builder()
            .postId(post.getId())
            .title(post.getTitle())
            .previewText(post.getPreviewText())
            .availableAt(post.getAvailableAt())
            .location(pointConverter.doublePointToStringPoint(post.getLocation()))
            .authorityCount(postAuthorities.size())
            .authorityImageList(getAuthorityImageList(post))
            .build();
    }

    public DeletePostResponse PostToDeletePostResponse(Post post) {
        return DeletePostResponse.builder()
            .postId(post.getId())
            .build();
    }
}
