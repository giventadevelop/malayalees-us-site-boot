package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.PollOption} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollOptionDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String optionText;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private PollDTO poll;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollOptionDTO)) {
            return false;
        }

        PollOptionDTO pollOptionDTO = (PollOptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pollOptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollOptionDTO{" +
            "id=" + getId() +
            ", optionText='" + getOptionText() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", poll=" + getPoll() +
            "}";
    }
}
