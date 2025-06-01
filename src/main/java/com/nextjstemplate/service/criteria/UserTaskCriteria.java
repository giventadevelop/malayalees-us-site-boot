package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.UserTask} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.UserTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter status;

    private StringFilter priority;

    private ZonedDateTimeFilter dueDate;

    private BooleanFilter completed;

    private StringFilter assigneeName;

    private StringFilter assigneeContactPhone;

    private StringFilter assigneeContactEmail;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter userId;

    private LongFilter eventId;

    private Boolean distinct;

    public UserTaskCriteria() {}

    public UserTaskCriteria(UserTaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.completed = other.completed == null ? null : other.completed.copy();
        this.assigneeName = other.assigneeName == null ? null : other.assigneeName.copy();
        this.assigneeContactPhone = other.assigneeContactPhone == null ? null : other.assigneeContactPhone.copy();
        this.assigneeContactEmail = other.assigneeContactEmail == null ? null : other.assigneeContactEmail.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserTaskCriteria copy() {
        return new UserTaskCriteria(this);
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

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getPriority() {
        return priority;
    }

    public StringFilter priority() {
        if (priority == null) {
            priority = new StringFilter();
        }
        return priority;
    }

    public void setPriority(StringFilter priority) {
        this.priority = priority;
    }

    public ZonedDateTimeFilter getDueDate() {
        return dueDate;
    }

    public ZonedDateTimeFilter dueDate() {
        if (dueDate == null) {
            dueDate = new ZonedDateTimeFilter();
        }
        return dueDate;
    }

    public void setDueDate(ZonedDateTimeFilter dueDate) {
        this.dueDate = dueDate;
    }

    public BooleanFilter getCompleted() {
        return completed;
    }

    public BooleanFilter completed() {
        if (completed == null) {
            completed = new BooleanFilter();
        }
        return completed;
    }

    public void setCompleted(BooleanFilter completed) {
        this.completed = completed;
    }

    public StringFilter getAssigneeName() {
        return assigneeName;
    }

    public StringFilter assigneeName() {
        if (assigneeName == null) {
            assigneeName = new StringFilter();
        }
        return assigneeName;
    }

    public void setAssigneeName(StringFilter assigneeName) {
        this.assigneeName = assigneeName;
    }

    public StringFilter getAssigneeContactPhone() {
        return assigneeContactPhone;
    }

    public StringFilter assigneeContactPhone() {
        if (assigneeContactPhone == null) {
            assigneeContactPhone = new StringFilter();
        }
        return assigneeContactPhone;
    }

    public void setAssigneeContactPhone(StringFilter assigneeContactPhone) {
        this.assigneeContactPhone = assigneeContactPhone;
    }

    public StringFilter getAssigneeContactEmail() {
        return assigneeContactEmail;
    }

    public StringFilter assigneeContactEmail() {
        if (assigneeContactEmail == null) {
            assigneeContactEmail = new StringFilter();
        }
        return assigneeContactEmail;
    }

    public void setAssigneeContactEmail(StringFilter assigneeContactEmail) {
        this.assigneeContactEmail = assigneeContactEmail;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
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

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
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
        final UserTaskCriteria that = (UserTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(completed, that.completed) &&
            Objects.equals(assigneeName, that.assigneeName) &&
            Objects.equals(assigneeContactPhone, that.assigneeContactPhone) &&
            Objects.equals(assigneeContactEmail, that.assigneeContactEmail) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            status,
            priority,
            dueDate,
            completed,
            assigneeName,
            assigneeContactPhone,
            assigneeContactEmail,
            createdAt,
            updatedAt,
            userId,
            eventId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (priority != null ? "priority=" + priority + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (completed != null ? "completed=" + completed + ", " : "") +
            (assigneeName != null ? "assigneeName=" + assigneeName + ", " : "") +
            (assigneeContactPhone != null ? "assigneeContactPhone=" + assigneeContactPhone + ", " : "") +
            (assigneeContactEmail != null ? "assigneeContactEmail=" + assigneeContactEmail + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
