package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @NotNull
    @Column(name = "storage_type", nullable = false)
    private String storageType;

    @Column(name = "file_url")
    private String fileUrl;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "file_data_content_type")
    private String fileDataContentType;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "is_public")
    private Boolean isPublic;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userSubscription" }, allowSetters = true)
    private UserProfile uploadedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Media id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Media title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Media description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public Media mediaType(String mediaType) {
        this.setMediaType(mediaType);
        return this;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getStorageType() {
        return this.storageType;
    }

    public Media storageType(String storageType) {
        this.setStorageType(storageType);
        return this;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public Media fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public byte[] getFileData() {
        return this.fileData;
    }

    public Media fileData(byte[] fileData) {
        this.setFileData(fileData);
        return this;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileDataContentType() {
        return this.fileDataContentType;
    }

    public Media fileDataContentType(String fileDataContentType) {
        this.fileDataContentType = fileDataContentType;
        return this;
    }

    public void setFileDataContentType(String fileDataContentType) {
        this.fileDataContentType = fileDataContentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Media contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public Media fileSize(Integer fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public Media isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Media createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Media updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Media event(Event event) {
        this.setEvent(event);
        return this;
    }

    public UserProfile getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(UserProfile userProfile) {
        this.uploadedBy = userProfile;
    }

    public Media uploadedBy(UserProfile userProfile) {
        this.setUploadedBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return getId() != null && getId().equals(((Media) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Media{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", mediaType='" + getMediaType() + "'" +
            ", storageType='" + getStorageType() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", fileData='" + getFileData() + "'" +
            ", fileDataContentType='" + getFileDataContentType() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", fileSize=" + getFileSize() +
            ", isPublic='" + getIsPublic() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
