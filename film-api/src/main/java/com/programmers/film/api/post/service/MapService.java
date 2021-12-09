package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PointConverter;
import com.programmers.film.api.post.dto.common.SimplePostDto;
import com.programmers.film.api.post.dto.response.GetMapResponse;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.repository.PostAuthorityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapService {
    private final PostAuthorityRepository postAuthorityRepository;
    private final PointConverter pointConverter;

    @Transactional(readOnly = true)
    public GetMapResponse getMapData(Long userId) {
        List<SimplePostDto> collect = postAuthorityRepository.findByUserId(userId).stream()
            .map(PostAuthority::getPost)
            .filter(post -> post.getIsDeleted() == 0)
            .map(post -> SimplePostDto.builder()
                .postId(post.getId())
                .state(post.getState().toString())
                .location(pointConverter.doublePointToStringPoint(post.getLocation()))
                .build()
            )
            .toList();
        return new GetMapResponse(collect);
    }
}

