package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.PollOption} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.PollOptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /poll-options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollOptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter optionText;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter pollId;

    private Boolean distinct;

    public PollOptionCriteria() {}

    public PollOptionCriteria(PollOptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.optionText = other.optionText == null ? null : other.optionText.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.pollId = other.pollId == null ? null : other.pollId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PollOptionCriteria copy() {
        return new PollOptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOptionText() {
        return optionText;
    }

    public StringFilter optionText() {
        if (optionText == null) {
            optionText = new StringFilter();
        }
        return optionText;
    }

    public void setOptionText(StringFilter optionText) {
        this.optionText = optionText;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getPollId() {
        return pollId;
    }

    public LongFilter pollId() {
        if (pollId == null) {
            pollId = new LongFilter();
        }
        return pollId;
    }

    public void setPollId(LongFilter pollId) {
        this.pollId = pollId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PollOptionCriteria that = (PollOptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(optionText, that.optionText) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(pollId, that.pollId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, optionText, createdAt, updatedAt, pollId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollOptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (optionText != null ? "optionText=" + optionText + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (pollId != null ? "pollId=" + pollId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
