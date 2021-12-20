package com.programmers.film.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.programmers.film.domain.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeAll() {
        generateAndSaveUser("abcde");
        generateAndSaveUser("abdcd");
        generateAndSaveUser("abc");
        generateAndSaveUser("abcd");
        generateAndSaveUser("bbcd");
        generateAndSaveUser("abeed");
        generateAndSaveUser("abbcd");
        generateAndSaveUser("ab");
    }

    @Test
    @DisplayName("검색어로 시작하는 닉네임 리스트를 불러올 수 있다.")
    public void findByNicknameStartsWithAndNicknameGreaterThan() {
        // Given
        String keyword = "a";
        String lastNickname = "";
        int size = 3;
        PageRequest pageRequest = PageRequest.of(0, size,
            JpaSort.unsafe("LENGTH(nickname)").and(Sort.by("nickname")));

        // When
        List<User> users = userRepository.findByNicknameStartsWithAndNicknameGreaterThan(
            keyword, lastNickname, pageRequest).getContent();

        // Then
        users.forEach(System.out::println);
    }

    void generateAndSaveUser(String nickname) {
        User user = User.builder()
            .nickname(nickname)
            .build();
        user.setProvider("kakao", "12345678");
        userRepository.save(user);
    }
}