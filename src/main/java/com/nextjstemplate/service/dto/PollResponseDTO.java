package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.PollResponse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollResponseDTO implements Serializable {

    @NotNull
    private Long id;

    private String comment;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private PollDTO poll;

    private PollOptionDTO pollOption;

    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PollDTO getPoll() {
        return poll;
    }

    public void setPoll(PollDTO poll) {
        this.poll = poll;
    }

    public PollOptionDTO getPollOption() {
        return pollOption;
    }

    public void setPollOption(PollOptionDTO pollOption) {
        this.pollOption = pollOption;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollResponseDTO)) {
            return false;
        }

        PollResponseDTO pollResponseDTO = (PollResponseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pollResponseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollResponseDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", poll=" + getPoll() +
            ", pollOption=" + getPollOption() +
            ", user=" + getUser() +
            "}";
    }
}
