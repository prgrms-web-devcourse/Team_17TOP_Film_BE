package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PointConverter;
import com.programmers.film.api.post.dto.common.SimplePostDto;
import com.programmers.film.api.post.dto.response.GetMapResponse;
import com.programmers.film.api.post.util.PostValidateUtil;
import com.programmers.film.api.user.exception.UserIdNotFoundException;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapService {
    private final UserRepository userRepository;
    private final PointConverter pointConverter;
    private final PostValidateUtil postValidateUtil;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = true)
    public GetMapResponse getMapData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(
                () -> new UserIdNotFoundException("사용자를 찾을 수 없습니다. 볼 수 있는 게시물 목록들을 찾을 수 없습니다."));

        List<SimplePostDto> collect = user.getPostAuthorities().stream()
            .map(PostAuthority::getPost)
            .filter(postValidateUtil::checkIsDelete)
            .map(post -> {
                log.trace("postId : " + post.getId() + " / post state : " + post.getState().toString());
                return SimplePostDto.builder()
                    .postId(post.getId())
                    .state(post.getState().toString())
                    .location(pointConverter.doublePointToStringPoint(post.getLocation()))
                    .build();
                }
            )
            .collect(Collectors.toList());
        return new GetMapResponse(collect);
    }
}

