package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.BulkOperationLog} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.BulkOperationLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bulk-operation-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BulkOperationLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter operationType;

    private IntegerFilter targetCount;

    private IntegerFilter successCount;

    private IntegerFilter errorCount;

    private ZonedDateTimeFilter createdAt;

    private LongFilter performedById;

    private Boolean distinct;

    public BulkOperationLogCriteria() {}

    public BulkOperationLogCriteria(BulkOperationLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.operationType = other.operationType == null ? null : other.operationType.copy();
        this.targetCount = other.targetCount == null ? null : other.targetCount.copy();
        this.successCount = other.successCount == null ? null : other.successCount.copy();
        this.errorCount = other.errorCount == null ? null : other.errorCount.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.performedById = other.performedById == null ? null : other.performedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BulkOperationLogCriteria copy() {
        return new BulkOperationLogCriteria(this);
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

    public StringFilter getOperationType() {
        return operationType;
    }

    public StringFilter operationType() {
        if (operationType == null) {
            operationType = new StringFilter();
        }
        return operationType;
    }

    public void setOperationType(StringFilter operationType) {
        this.operationType = operationType;
    }

    public IntegerFilter getTargetCount() {
        return targetCount;
    }

    public IntegerFilter targetCount() {
        if (targetCount == null) {
            targetCount = new IntegerFilter();
        }
        return targetCount;
    }

    public void setTargetCount(IntegerFilter targetCount) {
        this.targetCount = targetCount;
    }

    public IntegerFilter getSuccessCount() {
        return successCount;
    }

    public IntegerFilter successCount() {
        if (successCount == null) {
            successCount = new IntegerFilter();
        }
        return successCount;
    }

    public void setSuccessCount(IntegerFilter successCount) {
        this.successCount = successCount;
    }

    public IntegerFilter getErrorCount() {
        return errorCount;
    }

    public IntegerFilter errorCount() {
        if (errorCount == null) {
            errorCount = new IntegerFilter();
        }
        return errorCount;
    }

    public void setErrorCount(IntegerFilter errorCount) {
        this.errorCount = errorCount;
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

    public LongFilter getPerformedById() {
        return performedById;
    }

    public LongFilter performedById() {
        if (performedById == null) {
            performedById = new LongFilter();
        }
        return performedById;
    }

    public void setPerformedById(LongFilter performedById) {
        this.performedById = performedById;
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
        final BulkOperationLogCriteria that = (BulkOperationLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(operationType, that.operationType) &&
            Objects.equals(targetCount, that.targetCount) &&
            Objects.equals(successCount, that.successCount) &&
            Objects.equals(errorCount, that.errorCount) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(performedById, that.performedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, operationType, targetCount, successCount, errorCount, createdAt, performedById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulkOperationLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (operationType != null ? "operationType=" + operationType + ", " : "") +
            (targetCount != null ? "targetCount=" + targetCount + ", " : "") +
            (successCount != null ? "successCount=" + successCount + ", " : "") +
            (errorCount != null ? "errorCount=" + errorCount + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (performedById != null ? "performedById=" + performedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
