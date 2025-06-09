package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventLiveUpdate} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventLiveUpdateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-live-updates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter updateType;

    private StringFilter contentText;

    private StringFilter contentImageUrl;

    private StringFilter contentVideoUrl;

    private StringFilter contentLinkUrl;

    private IntegerFilter displayOrder;

    private BooleanFilter isDefault;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventLiveUpdateCriteria() {}

    public EventLiveUpdateCriteria(EventLiveUpdateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.updateType = other.updateType == null ? null : other.updateType.copy();
        this.contentText = other.contentText == null ? null : other.contentText.copy();
        this.contentImageUrl = other.contentImageUrl == null ? null : other.contentImageUrl.copy();
        this.contentVideoUrl = other.contentVideoUrl == null ? null : other.contentVideoUrl.copy();
        this.contentLinkUrl = other.contentLinkUrl == null ? null : other.contentLinkUrl.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.isDefault = other.isDefault == null ? null : other.isDefault.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventLiveUpdateCriteria copy() {
        return new EventLiveUpdateCriteria(this);
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

    public StringFilter getUpdateType() {
        return updateType;
    }

    public StringFilter updateType() {
        if (updateType == null) {
            updateType = new StringFilter();
        }
        return updateType;
    }

    public void setUpdateType(StringFilter updateType) {
        this.updateType = updateType;
    }

    public StringFilter getContentText() {
        return contentText;
    }

    public StringFilter contentText() {
        if (contentText == null) {
            contentText = new StringFilter();
        }
        return contentText;
    }

    public void setContentText(StringFilter contentText) {
        this.contentText = contentText;
    }

    public StringFilter getContentImageUrl() {
        return contentImageUrl;
    }

    public StringFilter contentImageUrl() {
        if (contentImageUrl == null) {
            contentImageUrl = new StringFilter();
        }
        return contentImageUrl;
    }

    public void setContentImageUrl(StringFilter contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public StringFilter getContentVideoUrl() {
        return contentVideoUrl;
    }

    public StringFilter contentVideoUrl() {
        if (contentVideoUrl == null) {
            contentVideoUrl = new StringFilter();
        }
        return contentVideoUrl;
    }

    public void setContentVideoUrl(StringFilter contentVideoUrl) {
        this.contentVideoUrl = contentVideoUrl;
    }

    public StringFilter getContentLinkUrl() {
        return contentLinkUrl;
    }

    public StringFilter contentLinkUrl() {
        if (contentLinkUrl == null) {
            contentLinkUrl = new StringFilter();
        }
        return contentLinkUrl;
    }

    public void setContentLinkUrl(StringFilter contentLinkUrl) {
        this.contentLinkUrl = contentLinkUrl;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            displayOrder = new IntegerFilter();
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            isDefault = new BooleanFilter();
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
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
        final EventLiveUpdateCriteria that = (EventLiveUpdateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(updateType, that.updateType) &&
            Objects.equals(contentText, that.contentText) &&
            Objects.equals(contentImageUrl, that.contentImageUrl) &&
            Objects.equals(contentVideoUrl, that.contentVideoUrl) &&
            Objects.equals(contentLinkUrl, that.contentLinkUrl) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            updateType,
            contentText,
            contentImageUrl,
            contentVideoUrl,
            contentLinkUrl,
            displayOrder,
            isDefault,
            createdAt,
            updatedAt,
            eventId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (updateType != null ? "updateType=" + updateType + ", " : "") +
            (contentText != null ? "contentText=" + contentText + ", " : "") +
            (contentImageUrl != null ? "contentImageUrl=" + contentImageUrl + ", " : "") +
            (contentVideoUrl != null ? "contentVideoUrl=" + contentVideoUrl + ", " : "") +
            (contentLinkUrl != null ? "contentLinkUrl=" + contentLinkUrl + ", " : "") +
            (displayOrder != null ? "displayOrder=" + displayOrder + ", " : "") +
            (isDefault != null ? "isDefault=" + isDefault + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
