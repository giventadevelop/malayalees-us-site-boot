package com.nextjstemplate.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventMediaDTO implements Serializable {

//    @NotNull
    private Long id;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private String eventMediaType;

    @NotNull
    private String storageType;

    private String fileUrl;

    @Lob
    private byte[] fileData;

    private String fileDataContentType;
    private String contentType;

    private Integer fileSize;

    private Boolean isPublic;

    private Boolean eventFlyer;

    private Boolean isEventManagementOfficialDocument;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private EventDTO event;

    private UserProfileDTO uploadedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventMediaType() {
        return eventMediaType;
    }

    public void setEventMediaType(String eventMediaType) {
        this.eventMediaType = eventMediaType;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileDataContentType() {
        return fileDataContentType;
    }

    public void setFileDataContentType(String fileDataContentType) {
        this.fileDataContentType = fileDataContentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getEventFlyer() {
        return eventFlyer;
    }

    public void setEventFlyer(Boolean eventFlyer) {
        this.eventFlyer = eventFlyer;
    }

    public Boolean getIsEventManagementOfficialDocument() {
        return isEventManagementOfficialDocument;
    }

    public void setIsEventManagementOfficialDocument(Boolean isEventManagementOfficialDocument) {
        this.isEventManagementOfficialDocument = isEventManagementOfficialDocument;
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

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public UserProfileDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UserProfileDTO uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventMediaDTO)) {
            return false;
        }

        EventMediaDTO eventMediaDTO = (EventMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventMediaDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", eventMediaType='" + getEventMediaType() + "'" +
            ", storageType='" + getStorageType() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", fileData='" + getFileData() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", fileSize=" + getFileSize() +
            ", isPublic='" + getIsPublic() + "'" +
            ", eventFlyer='" + getEventFlyer() + "'" +
            ", isEventManagementOfficialDocument='" + getIsEventManagementOfficialDocument() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", uploadedBy=" + getUploadedBy() +
            "}";
    }
}
