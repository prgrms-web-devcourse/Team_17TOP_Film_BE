package com.programmers.film.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.programmers.film.domain"})
@EnableJpaRepositories(basePackages = {"com.programmers.film.domain"})
@SpringBootApplication(scanBasePackages = "com.programmers.film")
public class FilmApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmApiApplication.class, args);
    }
}
