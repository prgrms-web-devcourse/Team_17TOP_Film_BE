package com.programmers.film.scheduler;

import com.programmers.film.domain.post.domain.PostState;
import com.programmers.film.domain.post.domain.PostStatus;
import com.programmers.film.domain.post.repository.PostRepository;
import com.programmers.film.domain.post.repository.PostStateRepository;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PostRepository postRepository;
    private final PostStateRepository postStateRepository;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Scheduled(cron="30 00 00 * * *", zone = "Asia/Seoul")
    @Transactional
    public void PostStateUpdateScheduled() {
        PostState postState = postStateRepository.findByPostStateValue(PostStatus.OPENABLE.toString()).get();
        LocalDate day = LocalDate.now();
        log.info(day.toString());
        postRepository.findAllByAvailableAtBetween(day,day)
            .forEach( post -> post.setState(postState));
    }
}
