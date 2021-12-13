package com.programmers.film.api.post.util;

import com.programmers.film.api.post.exception.PostCanNotOpenException;
import com.programmers.film.api.post.exception.PostIdNotFoundException;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PostValidateUtil {
    /**
    * is deleted return true / or not false
     **/
    public boolean checkIsDelete(Post post){
        if(post.getIsDeleted() == 1) {
            return true;
        }
        return false;
    }

    public boolean checkAuthority(Post post, User user) {
        List<PostAuthority> postAuthoritiesByPost = post.getPostAuthorities();

        List<PostAuthority> collect = postAuthoritiesByPost.stream()
            .filter(postAuthority -> postAuthority.getUser().getId()
                .equals(user.getId())).collect(Collectors.toList());

        if(collect.size() <= 0) {
            return true;
        }
        return false;
    }
}
