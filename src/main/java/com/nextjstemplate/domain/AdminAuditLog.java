package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AdminAuditLog.
 */
@Entity
@Table(name = "admin_audit_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @NotNull
    @Column(name = "table_name", nullable = false)
    private String tableName;

    @NotNull
    @Column(name = "record_id", nullable = false)
    private String recordId;

    @Lob
    @Column(name = "changes")
    private String changes;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userSubscription" }, allowSetters = true)
    private UserProfile admin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdminAuditLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public AdminAuditLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return this.tableName;
    }

    public AdminAuditLog tableName(String tableName) {
        this.setTableName(tableName);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public AdminAuditLog recordId(String recordId) {
        this.setRecordId(recordId);
        return this;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getChanges() {
        return this.changes;
    }

    public AdminAuditLog changes(String changes) {
        this.setChanges(changes);
        return this;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public AdminAuditLog createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfile getAdmin() {
        return this.admin;
    }

    public void setAdmin(UserProfile userProfile) {
        this.admin = userProfile;
    }

    public AdminAuditLog admin(UserProfile userProfile) {
        this.setAdmin(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminAuditLog)) {
            return false;
        }
        return getId() != null && getId().equals(((AdminAuditLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminAuditLog{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", recordId='" + getRecordId() + "'" +
            ", changes='" + getChanges() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
