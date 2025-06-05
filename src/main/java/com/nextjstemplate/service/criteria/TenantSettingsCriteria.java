package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.TenantSettings} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.TenantSettingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantSettingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private BooleanFilter allowUserRegistration;

    private BooleanFilter requireAdminApproval;

    private BooleanFilter enableWhatsappIntegration;

    private BooleanFilter enableEmailMarketing;

    private StringFilter whatsappApiKey;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter tenantOrganizationId;

    private Boolean distinct;

    public TenantSettingsCriteria() {}

    public TenantSettingsCriteria(TenantSettingsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.allowUserRegistration = other.allowUserRegistration == null ? null : other.allowUserRegistration.copy();
        this.requireAdminApproval = other.requireAdminApproval == null ? null : other.requireAdminApproval.copy();
        this.enableWhatsappIntegration = other.enableWhatsappIntegration == null ? null : other.enableWhatsappIntegration.copy();
        this.enableEmailMarketing = other.enableEmailMarketing == null ? null : other.enableEmailMarketing.copy();
        this.whatsappApiKey = other.whatsappApiKey == null ? null : other.whatsappApiKey.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.tenantOrganizationId = other.tenantOrganizationId == null ? null : other.tenantOrganizationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TenantSettingsCriteria copy() {
        return new TenantSettingsCriteria(this);
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

    public BooleanFilter getAllowUserRegistration() {
        return allowUserRegistration;
    }

    public BooleanFilter allowUserRegistration() {
        if (allowUserRegistration == null) {
            allowUserRegistration = new BooleanFilter();
        }
        return allowUserRegistration;
    }

    public void setAllowUserRegistration(BooleanFilter allowUserRegistration) {
        this.allowUserRegistration = allowUserRegistration;
    }

    public BooleanFilter getRequireAdminApproval() {
        return requireAdminApproval;
    }

    public BooleanFilter requireAdminApproval() {
        if (requireAdminApproval == null) {
            requireAdminApproval = new BooleanFilter();
        }
        return requireAdminApproval;
    }

    public void setRequireAdminApproval(BooleanFilter requireAdminApproval) {
        this.requireAdminApproval = requireAdminApproval;
    }

    public BooleanFilter getEnableWhatsappIntegration() {
        return enableWhatsappIntegration;
    }

    public BooleanFilter enableWhatsappIntegration() {
        if (enableWhatsappIntegration == null) {
            enableWhatsappIntegration = new BooleanFilter();
        }
        return enableWhatsappIntegration;
    }

    public void setEnableWhatsappIntegration(BooleanFilter enableWhatsappIntegration) {
        this.enableWhatsappIntegration = enableWhatsappIntegration;
    }

    public BooleanFilter getEnableEmailMarketing() {
        return enableEmailMarketing;
    }

    public BooleanFilter enableEmailMarketing() {
        if (enableEmailMarketing == null) {
            enableEmailMarketing = new BooleanFilter();
        }
        return enableEmailMarketing;
    }

    public void setEnableEmailMarketing(BooleanFilter enableEmailMarketing) {
        this.enableEmailMarketing = enableEmailMarketing;
    }

    public StringFilter getWhatsappApiKey() {
        return whatsappApiKey;
    }

    public StringFilter whatsappApiKey() {
        if (whatsappApiKey == null) {
            whatsappApiKey = new StringFilter();
        }
        return whatsappApiKey;
    }

    public void setWhatsappApiKey(StringFilter whatsappApiKey) {
        this.whatsappApiKey = whatsappApiKey;
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

    public LongFilter getTenantOrganizationId() {
        return tenantOrganizationId;
    }

    public LongFilter tenantOrganizationId() {
        if (tenantOrganizationId == null) {
            tenantOrganizationId = new LongFilter();
        }
        return tenantOrganizationId;
    }

    public void setTenantOrganizationId(LongFilter tenantOrganizationId) {
        this.tenantOrganizationId = tenantOrganizationId;
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
        final TenantSettingsCriteria that = (TenantSettingsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(allowUserRegistration, that.allowUserRegistration) &&
            Objects.equals(requireAdminApproval, that.requireAdminApproval) &&
            Objects.equals(enableWhatsappIntegration, that.enableWhatsappIntegration) &&
            Objects.equals(enableEmailMarketing, that.enableEmailMarketing) &&
            Objects.equals(whatsappApiKey, that.whatsappApiKey) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(tenantOrganizationId, that.tenantOrganizationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            allowUserRegistration,
            requireAdminApproval,
            enableWhatsappIntegration,
            enableEmailMarketing,
            whatsappApiKey,
            createdAt,
            updatedAt,
            tenantOrganizationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantSettingsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (allowUserRegistration != null ? "allowUserRegistration=" + allowUserRegistration + ", " : "") +
            (requireAdminApproval != null ? "requireAdminApproval=" + requireAdminApproval + ", " : "") +
            (enableWhatsappIntegration != null ? "enableWhatsappIntegration=" + enableWhatsappIntegration + ", " : "") +
            (enableEmailMarketing != null ? "enableEmailMarketing=" + enableEmailMarketing + ", " : "") +
            (whatsappApiKey != null ? "whatsappApiKey=" + whatsappApiKey + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (tenantOrganizationId != null ? "tenantOrganizationId=" + tenantOrganizationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
