package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "user_id", length = 255, nullable = false, unique = true)
    private String userId;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    @Size(max = 255)
    @Column(name = "phone", length = 255)
    private String phone;

    @Size(max = 255)
    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Size(max = 255)
    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Size(max = 255)
    @Column(name = "city", length = 255)
    private String city;

    @Size(max = 255)
    @Column(name = "state", length = 255)
    private String state;

    @Size(max = 255)
    @Column(name = "zip_code", length = 255)
    private String zipCode;

    @Size(max = 255)
    @Column(name = "country", length = 255)
    private String country;

    @Size(max = 255)
    @Column(name = "notes", length = 255)
    private String notes;

    @Size(max = 255)
    @Column(name = "family_name", length = 255)
    private String familyName;

    @Size(max = 255)
    @Column(name = "city_town", length = 255)
    private String cityTown;

    @Size(max = 255)
    @Column(name = "district", length = 255)
    private String district;

    @Size(max = 255)
    @Column(name = "educational_institution", length = 255)
    private String educationalInstitution;

    @Size(max = 255)
    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Size(max = 50)
    @Column(name = "user_status", length = 50)
    private String userStatus;

    @Size(max = 50)
    @Column(name = "user_role", length = 50)
    private String userRole;

    @Column(name = "reviewed_by_admin_at")
    private LocalDate reviewedByAdminAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile reviewedByAdmin;

    @JsonIgnoreProperties(value = { "userProfile" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userProfile")
    private UserSubscription userSubscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public UserProfile tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return this.userId;
    }

    public UserProfile userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserProfile firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserProfile lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public UserProfile email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserProfile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public UserProfile addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public UserProfile addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public UserProfile city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public UserProfile state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public UserProfile zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public UserProfile country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNotes() {
        return this.notes;
    }

    public UserProfile notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public UserProfile familyName(String familyName) {
        this.setFamilyName(familyName);
        return this;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCityTown() {
        return this.cityTown;
    }

    public UserProfile cityTown(String cityTown) {
        this.setCityTown(cityTown);
        return this;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getDistrict() {
        return this.district;
    }

    public UserProfile district(String district) {
        this.setDistrict(district);
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEducationalInstitution() {
        return this.educationalInstitution;
    }

    public UserProfile educationalInstitution(String educationalInstitution) {
        this.setEducationalInstitution(educationalInstitution);
        return this;
    }

    public void setEducationalInstitution(String educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public UserProfile profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserStatus() {
        return this.userStatus;
    }

    public UserProfile userStatus(String userStatus) {
        this.setUserStatus(userStatus);
        return this;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public UserProfile userRole(String userRole) {
        this.setUserRole(userRole);
        return this;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public LocalDate getReviewedByAdminAt() {
        return this.reviewedByAdminAt;
    }

    public UserProfile reviewedByAdminAt(LocalDate reviewedByAdminAt) {
        this.setReviewedByAdminAt(reviewedByAdminAt);
        return this;
    }

    public void setReviewedByAdminAt(LocalDate reviewedByAdminAt) {
        this.reviewedByAdminAt = reviewedByAdminAt;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public UserProfile createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public UserProfile updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfile getReviewedByAdmin() {
        return this.reviewedByAdmin;
    }

    public void setReviewedByAdmin(UserProfile userProfile) {
        this.reviewedByAdmin = userProfile;
    }

    public UserProfile reviewedByAdmin(UserProfile userProfile) {
        this.setReviewedByAdmin(userProfile);
        return this;
    }

    public UserSubscription getUserSubscription() {
        return this.userSubscription;
    }

    public void setUserSubscription(UserSubscription userSubscription) {
        if (this.userSubscription != null) {
            this.userSubscription.setUserProfile(null);
        }
        if (userSubscription != null) {
            userSubscription.setUserProfile(this);
        }
        this.userSubscription = userSubscription;
    }

    public UserProfile userSubscription(UserSubscription userSubscription) {
        this.setUserSubscription(userSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", notes='" + getNotes() + "'" +
            ", familyName='" + getFamilyName() + "'" +
            ", cityTown='" + getCityTown() + "'" +
            ", district='" + getDistrict() + "'" +
            ", educationalInstitution='" + getEducationalInstitution() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            ", userRole='" + getUserRole() + "'" +
            ", reviewedByAdminAt='" + getReviewedByAdminAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
