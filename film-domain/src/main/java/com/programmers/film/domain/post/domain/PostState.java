package com.programmers.film.domain.post.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_states")
public class PostState {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 20, name = "post_state_value")
    private String postStateValue;

    @Override
    public String toString() {
        return postStateValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostState postState = (PostState) o;
        return getId().equals(postState.getId()) && getPostStateValue().equals(postState.getPostStateValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPostStateValue());
    }
}
