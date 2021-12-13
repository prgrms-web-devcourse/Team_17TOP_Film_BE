package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PointConverter;
import com.programmers.film.api.post.converter.PostConverter;
import com.programmers.film.api.post.dto.common.SimplePostDto;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.util.PostValidateUtil;
import com.programmers.film.api.user.exception.UserIdNotFoundException;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PostValidateUtil postValidateUtil;
    private final PointConverter pointConverter;
    private final PostConverter postConverter;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = true)
    public List<PreviewPostResponse> getMyPosts(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(
                () -> new UserIdNotFoundException("사용자를 찾을 수 없습니다. 볼 수 있는 게시물 목록들을 찾을 수 없습니다."));

        List<PreviewPostResponse> collect = user.getPostAuthorities().stream()
            .map(PostAuthority::getPost)
            .filter(postValidateUtil::checkIsDelete)
            .map(post -> {
                    return postConverter.postToPreviewPostResponse(post);
                }
            )
            .collect(Collectors.toList());

        return collect;
    }

}
