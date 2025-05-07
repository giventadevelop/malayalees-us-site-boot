package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.PollResponse} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.PollResponseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /poll-responses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollResponseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comment;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter pollId;

    private LongFilter pollOptionId;

    private LongFilter userId;

    private Boolean distinct;

    public PollResponseCriteria() {}

    public PollResponseCriteria(PollResponseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.pollId = other.pollId == null ? null : other.pollId.copy();
        this.pollOptionId = other.pollOptionId == null ? null : other.pollOptionId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PollResponseCriteria copy() {
        return new PollResponseCriteria(this);
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

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
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

    public LongFilter getPollOptionId() {
        return pollOptionId;
    }

    public LongFilter pollOptionId() {
        if (pollOptionId == null) {
            pollOptionId = new LongFilter();
        }
        return pollOptionId;
    }

    public void setPollOptionId(LongFilter pollOptionId) {
        this.pollOptionId = pollOptionId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final PollResponseCriteria that = (PollResponseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(pollId, that.pollId) &&
            Objects.equals(pollOptionId, that.pollOptionId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, createdAt, updatedAt, pollId, pollOptionId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollResponseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (pollId != null ? "pollId=" + pollId + ", " : "") +
            (pollOptionId != null ? "pollOptionId=" + pollOptionId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
