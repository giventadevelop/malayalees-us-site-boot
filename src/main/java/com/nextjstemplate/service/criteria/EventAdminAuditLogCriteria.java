package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventAdminAuditLog} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventAdminAuditLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-admin-audit-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAdminAuditLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter action;

    private StringFilter tableName;

    private StringFilter recordId;

    private ZonedDateTimeFilter createdAt;

    private LongFilter adminId;

    private Boolean distinct;

    public EventAdminAuditLogCriteria() {}

    public EventAdminAuditLogCriteria(EventAdminAuditLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.tableName = other.tableName == null ? null : other.tableName.copy();
        this.recordId = other.recordId == null ? null : other.recordId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.adminId = other.adminId == null ? null : other.adminId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventAdminAuditLogCriteria copy() {
        return new EventAdminAuditLogCriteria(this);
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

    public StringFilter getAction() {
        return action;
    }

    public StringFilter action() {
        if (action == null) {
            action = new StringFilter();
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public StringFilter getTableName() {
        return tableName;
    }

    public StringFilter tableName() {
        if (tableName == null) {
            tableName = new StringFilter();
        }
        return tableName;
    }

    public void setTableName(StringFilter tableName) {
        this.tableName = tableName;
    }

    public StringFilter getRecordId() {
        return recordId;
    }

    public StringFilter recordId() {
        if (recordId == null) {
            recordId = new StringFilter();
        }
        return recordId;
    }

    public void setRecordId(StringFilter recordId) {
        this.recordId = recordId;
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

    public LongFilter getAdminId() {
        return adminId;
    }

    public LongFilter adminId() {
        if (adminId == null) {
            adminId = new LongFilter();
        }
        return adminId;
    }

    public void setAdminId(LongFilter adminId) {
        this.adminId = adminId;
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
        final EventAdminAuditLogCriteria that = (EventAdminAuditLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(action, that.action) &&
            Objects.equals(tableName, that.tableName) &&
            Objects.equals(recordId, that.recordId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(adminId, that.adminId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, action, tableName, recordId, createdAt, adminId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAdminAuditLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (action != null ? "action=" + action + ", " : "") +
            (tableName != null ? "tableName=" + tableName + ", " : "") +
            (recordId != null ? "recordId=" + recordId + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (adminId != null ? "adminId=" + adminId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
