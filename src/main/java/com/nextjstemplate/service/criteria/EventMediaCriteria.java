package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventMedia} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventMediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-medias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventMediaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter eventMediaType;

    private StringFilter storageType;

    private StringFilter fileUrl;

    private StringFilter preSignedUrl;

    private StringFilter contentType;

    private IntegerFilter fileSize;

    private BooleanFilter isPublic;

    private BooleanFilter eventFlyer;

    private BooleanFilter isEventManagementOfficialDocument;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter eventId;

    private LongFilter uploadedById;

    private Boolean distinct;

    public EventMediaCriteria() {}

    public EventMediaCriteria(EventMediaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.eventMediaType = other.eventMediaType == null ? null : other.eventMediaType.copy();
        this.storageType = other.storageType == null ? null : other.storageType.copy();
        this.fileUrl = other.fileUrl == null ? null : other.fileUrl.copy();
        this.preSignedUrl = other.preSignedUrl == null ? null : other.preSignedUrl.copy();
        this.contentType = other.contentType == null ? null : other.contentType.copy();
        this.fileSize = other.fileSize == null ? null : other.fileSize.copy();
        this.isPublic = other.isPublic == null ? null : other.isPublic.copy();
        this.eventFlyer = other.eventFlyer == null ? null : other.eventFlyer.copy();
        this.isEventManagementOfficialDocument =
            other.isEventManagementOfficialDocument == null ? null : other.isEventManagementOfficialDocument.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.uploadedById = other.uploadedById == null ? null : other.uploadedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventMediaCriteria copy() {
        return new EventMediaCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getEventMediaType() {
        return eventMediaType;
    }

    public StringFilter eventMediaType() {
        if (eventMediaType == null) {
            eventMediaType = new StringFilter();
        }
        return eventMediaType;
    }

    public void setEventMediaType(StringFilter eventMediaType) {
        this.eventMediaType = eventMediaType;
    }

    public StringFilter getStorageType() {
        return storageType;
    }

    public StringFilter storageType() {
        if (storageType == null) {
            storageType = new StringFilter();
        }
        return storageType;
    }

    public void setStorageType(StringFilter storageType) {
        this.storageType = storageType;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public StringFilter fileUrl() {
        if (fileUrl == null) {
            fileUrl = new StringFilter();
        }
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
    }

    public StringFilter getPreSignedUrl() {
        return preSignedUrl;
    }

    public StringFilter preSignedUrl() {
        if (preSignedUrl == null) {
            preSignedUrl = new StringFilter();
        }
        return preSignedUrl;
    }

    public void setPreSignedUrl(StringFilter preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public StringFilter contentType() {
        if (contentType == null) {
            contentType = new StringFilter();
        }
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public IntegerFilter getFileSize() {
        return fileSize;
    }

    public IntegerFilter fileSize() {
        if (fileSize == null) {
            fileSize = new IntegerFilter();
        }
        return fileSize;
    }

    public void setFileSize(IntegerFilter fileSize) {
        this.fileSize = fileSize;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            isPublic = new BooleanFilter();
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public BooleanFilter getEventFlyer() {
        return eventFlyer;
    }

    public BooleanFilter eventFlyer() {
        if (eventFlyer == null) {
            eventFlyer = new BooleanFilter();
        }
        return eventFlyer;
    }

    public void setEventFlyer(BooleanFilter eventFlyer) {
        this.eventFlyer = eventFlyer;
    }

    public BooleanFilter getIsEventManagementOfficialDocument() {
        return isEventManagementOfficialDocument;
    }

    public BooleanFilter isEventManagementOfficialDocument() {
        if (isEventManagementOfficialDocument == null) {
            isEventManagementOfficialDocument = new BooleanFilter();
        }
        return isEventManagementOfficialDocument;
    }

    public void setIsEventManagementOfficialDocument(BooleanFilter isEventManagementOfficialDocument) {
        this.isEventManagementOfficialDocument = isEventManagementOfficialDocument;
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

    public LongFilter getUploadedById() {
        return uploadedById;
    }

    public LongFilter uploadedById() {
        if (uploadedById == null) {
            uploadedById = new LongFilter();
        }
        return uploadedById;
    }

    public void setUploadedById(LongFilter uploadedById) {
        this.uploadedById = uploadedById;
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
        final EventMediaCriteria that = (EventMediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(eventMediaType, that.eventMediaType) &&
            Objects.equals(storageType, that.storageType) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(preSignedUrl, that.preSignedUrl) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(eventFlyer, that.eventFlyer) &&
            Objects.equals(isEventManagementOfficialDocument, that.isEventManagementOfficialDocument) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(uploadedById, that.uploadedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            eventMediaType,
            storageType,
            fileUrl,
            preSignedUrl,
            contentType,
            fileSize,
            isPublic,
            eventFlyer,
            isEventManagementOfficialDocument,
            createdAt,
            updatedAt,
            eventId,
            uploadedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventMediaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (eventMediaType != null ? "eventMediaType=" + eventMediaType + ", " : "") +
            (storageType != null ? "storageType=" + storageType + ", " : "") +
            (fileUrl != null ? "fileUrl=" + fileUrl + ", " : "") +
            (preSignedUrl != null ? "preSignedUrl=" + preSignedUrl + ", " : "") +
            (contentType != null ? "contentType=" + contentType + ", " : "") +
            (fileSize != null ? "fileSize=" + fileSize + ", " : "") +
            (isPublic != null ? "isPublic=" + isPublic + ", " : "") +
            (eventFlyer != null ? "eventFlyer=" + eventFlyer + ", " : "") +
            (isEventManagementOfficialDocument != null ? "isEventManagementOfficialDocument=" + isEventManagementOfficialDocument + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (uploadedById != null ? "uploadedById=" + uploadedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
