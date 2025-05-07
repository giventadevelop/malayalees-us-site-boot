package com.nextjstemplate.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.AdminAuditLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminAuditLogDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String action;

    @NotNull
    private String tableName;

    @NotNull
    private String recordId;

    @Lob
    private String changes;

    @NotNull
    private Instant createdAt;

    private UserProfileDTO admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfileDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserProfileDTO admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminAuditLogDTO)) {
            return false;
        }

        AdminAuditLogDTO adminAuditLogDTO = (AdminAuditLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adminAuditLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminAuditLogDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", recordId='" + getRecordId() + "'" +
            ", changes='" + getChanges() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", admin=" + getAdmin() +
            "}";
    }
}
