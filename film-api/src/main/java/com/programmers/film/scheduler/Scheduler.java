package com.programmers.film.scheduler;

import com.programmers.film.domain.post.domain.PostState;
import com.programmers.film.domain.post.domain.PostStatus;
import com.programmers.film.domain.post.repository.PostRepository;
import com.programmers.film.domain.post.repository.PostStateRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PostRepository postRepository;
    private final PostStateRepository postStateRepository;

    @Scheduled(cron="01 00 00 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void PostStateUpdateScheduled() {
        PostState postState = postStateRepository.findByPostStateValue(PostStatus.OPENABLE.toString()).get();

        postRepository.findAllByAvailableAtBetween(LocalDate.now(),LocalDate.now())
            .forEach( post -> post.setState(postState));
    }
}
