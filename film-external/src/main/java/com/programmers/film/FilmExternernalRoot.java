package com.programmers.film;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackageClasses = {
    FilmExternernalRoot.class
})
@Configuration
public class FilmExternernalRoot {

}