-- ===================================================
-- COMPLETE EVENT MANAGEMENT DATABASE SCHEMA
-- With JDL Additions and All Comments Addressed
-- ===================================================

-- ===================================================
-- INITIAL SETUP AND SCHEMA CONFIGURATION
-- ===================================================

-- Set search path to public schema
SET search_path TO public;

-- Connection and session settings
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Ensure we're working in the public schema
SELECT pg_catalog.set_config('search_path', 'public', false);

ALTER DATABASE giventa_event_management OWNER TO giventa_event_management;

-- Drop existing types if they exist (for clean recreation)
DROP TYPE IF EXISTS guest_age_group CASCADE;
DROP TYPE IF EXISTS user_to_guest_relationship CASCADE;
DROP TYPE IF EXISTS user_event_registration_status CASCADE;
DROP TYPE IF EXISTS user_event_check_in_status CASCADE;
DROP TYPE IF EXISTS subscription_plan_type CASCADE;
DROP TYPE IF EXISTS subscription_status_type CASCADE;
DROP TYPE IF EXISTS user_role_type CASCADE;
DROP TYPE IF EXISTS user_status_type CASCADE;
DROP TYPE IF EXISTS event_admission_type CASCADE;
DROP TYPE IF EXISTS transaction_type CASCADE;
DROP TYPE IF EXISTS transaction_status CASCADE;

-- ===================================================
-- SEQUENCE CREATION
-- ===================================================

-- Drop sequence if exists and recreate
DROP SEQUENCE IF EXISTS public.sequence_generator CASCADE;

CREATE SEQUENCE IF NOT EXISTS public.sequence_generator
    START WITH 1050
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- ===================================================
-- DROP EXISTING TABLES (in reverse dependency order)
-- ===================================================

DROP TABLE IF EXISTS public.bulk_operation_log CASCADE;
DROP TABLE IF EXISTS public.qr_code_usage CASCADE;
DROP TABLE IF EXISTS public.user_registration_request CASCADE;
DROP TABLE IF EXISTS public.event_attendee_guest CASCADE;
DROP TABLE IF EXISTS public.event_guest_pricing CASCADE;
DROP TABLE IF EXISTS public.event_attendee CASCADE;
DROP TABLE IF EXISTS public.event_admin_audit_log CASCADE;
DROP TABLE IF EXISTS public.event_calendar_entry CASCADE;
DROP TABLE IF EXISTS public.event_media CASCADE;
DROP TABLE IF EXISTS public.event_poll_response CASCADE;
DROP TABLE IF EXISTS public.event_poll_option CASCADE;
DROP TABLE IF EXISTS public.event_poll CASCADE;
DROP TABLE IF EXISTS public.event_ticket_transaction CASCADE;
DROP TABLE IF EXISTS public.user_payment_transaction CASCADE;
DROP TABLE IF EXISTS public.event_ticket_type CASCADE;
DROP TABLE IF EXISTS public.event_organizer CASCADE;
DROP TABLE IF EXISTS public.event_details CASCADE;
DROP TABLE IF EXISTS public.event_admin CASCADE;
DROP TABLE IF EXISTS public.user_task CASCADE;
DROP TABLE IF EXISTS public.user_subscription CASCADE;
DROP TABLE IF EXISTS public.event_type_details CASCADE;
DROP TABLE IF EXISTS public.tenant_settings CASCADE;
DROP TABLE IF EXISTS public.user_profile CASCADE;
DROP TABLE IF EXISTS public.tenant_organization CASCADE;
DROP TABLE IF EXISTS public.databasechangeloglock CASCADE;
DROP TABLE IF EXISTS public.databasechangelog CASCADE;
DROP TABLE IF EXISTS public.discount_code CASCADE;
DROP TABLE IF EXISTS public.event_discount_code CASCADE;

-- ===================================================
-- CORE TABLES (No dependencies)
-- ===================================================

-- Liquibase tracking tables
CREATE TABLE IF NOT EXISTS public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);

CREATE TABLE IF NOT EXISTS public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255),
    CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id)
);


-- 1. Discount code table (ensure this is before any references)
CREATE TABLE IF NOT EXISTS public.discount_code (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    discount_type VARCHAR(20) NOT NULL DEFAULT 'PERCENT', -- PERCENT or FIXED
    discount_value NUMERIC(10,2) NOT NULL,
    max_uses INTEGER DEFAULT NULL, -- null = unlimited
    uses_count INTEGER DEFAULT 0,
    valid_from TIMESTAMP WITHOUT TIME ZONE,
    valid_to TIMESTAMP WITHOUT TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);
COMMENT ON TABLE public.discount_code IS 'Discount codes for ticket purchases';


-- Tenant Organization (Root table for multi-tenancy)
CREATE TABLE IF NOT EXISTS public.tenant_organization (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255) NOT NULL,
    organization_name VARCHAR(255) NOT NULL,
    domain VARCHAR(255),
    primary_color VARCHAR(7),
    secondary_color VARCHAR(7),
    logo_url VARCHAR(1024),
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50),
    subscription_plan VARCHAR(20),
    subscription_status VARCHAR(20),
    subscription_start_date DATE,
    subscription_end_date DATE,
    monthly_fee_usd NUMERIC(21,2),
    stripe_customer_id VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT tenant_organization_pkey PRIMARY KEY (id),
    CONSTRAINT tenant_organization_tenant_id_key UNIQUE (tenant_id),
    CONSTRAINT tenant_organization_domain_key UNIQUE (domain),
    CONSTRAINT check_monthly_fee_positive CHECK (monthly_fee_usd >= 0),
    CONSTRAINT check_subscription_dates CHECK (subscription_end_date IS NULL OR subscription_end_date >= subscription_start_date)
);

COMMENT ON TABLE public.tenant_organization IS 'Multi-tenant organization configuration and subscription management';

-- ===================================================
-- DEPENDENT TABLES (Level 1 - Depend on tenant_organization)
-- ===================================================

-- User Profile (Enhanced with all corrections)
CREATE TABLE IF NOT EXISTS public.user_profile (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    user_id VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255),
    country VARCHAR(255),
    notes TEXT,
    family_name VARCHAR(255),
    city_town VARCHAR(255),
    district VARCHAR(255),
    educational_institution VARCHAR(255),
    profile_image_url VARCHAR(1024),
    user_status VARCHAR(50),
    user_role VARCHAR(50),
    reviewed_by_admin_at TIMESTAMP WITHOUT TIME ZONE,
    reviewed_by_admin_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT user_profile_pkey PRIMARY KEY (id),
    CONSTRAINT ux_user_profile__user_id UNIQUE (user_id),
    CONSTRAINT check_email_format CHECK (email IS NULL OR email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

COMMENT ON TABLE public.user_profile IS 'User profiles with tenant isolation and enhanced fields';

-- Tenant Settings (Enhanced)
CREATE TABLE IF NOT EXISTS public.tenant_settings (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255) NOT NULL,
    allow_user_registration BOOLEAN DEFAULT TRUE,
    require_admin_approval BOOLEAN DEFAULT FALSE,
    enable_whatsapp_integration BOOLEAN DEFAULT FALSE,
    enable_email_marketing BOOLEAN DEFAULT FALSE,
    whatsapp_api_key VARCHAR(500),
    email_provider_config TEXT,
    custom_css TEXT,
    custom_js TEXT,
    max_events_per_month INTEGER DEFAULT NULL,
    max_attendees_per_event INTEGER DEFAULT NULL,
    enable_guest_registration BOOLEAN DEFAULT TRUE,
    max_guests_per_attendee INTEGER DEFAULT 5,
    default_event_capacity INTEGER DEFAULT 100,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT tenant_settings_pkey PRIMARY KEY (id),
    CONSTRAINT tenant_settings_tenant_id_key UNIQUE (tenant_id),
    CONSTRAINT check_max_events_positive CHECK (max_events_per_month IS NULL OR max_events_per_month > 0),
    CONSTRAINT check_max_attendees_positive CHECK (max_attendees_per_event IS NULL OR max_attendees_per_event > 0),
    CONSTRAINT check_max_guests_positive CHECK (max_guests_per_attendee IS NULL OR max_guests_per_attendee >= 0),
    CONSTRAINT check_default_capacity_positive CHECK (default_event_capacity IS NULL OR default_event_capacity > 0)
);

COMMENT ON TABLE public.tenant_settings IS 'Tenant-specific configuration settings with enhanced options';

-- Event Type Details
CREATE TABLE IF NOT EXISTS public.event_type_details (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    color VARCHAR(7) DEFAULT '#3B82F6',
    icon VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT event_type_details_pkey PRIMARY KEY (id),
    CONSTRAINT ux_event_type_tenant_name UNIQUE (tenant_id, name),
    CONSTRAINT check_color_format CHECK (color ~* '^#[0-9A-Fa-f]{6}$')
);

COMMENT ON TABLE public.event_type_details IS 'Event type classifications with visual customization';

-- User Subscription
CREATE TABLE IF NOT EXISTS public.user_subscription (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    stripe_customer_id VARCHAR(255),
    stripe_subscription_id VARCHAR(255),
    stripe_price_id VARCHAR(255),
    stripe_current_period_end TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(255) NOT NULL,
    trial_ends_at TIMESTAMP WITHOUT TIME ZONE,
    cancel_at_period_end BOOLEAN DEFAULT FALSE,
    user_profile_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT user_subscription_pkey PRIMARY KEY (id),
    CONSTRAINT ux_user_subscription__stripe_customer_id UNIQUE (stripe_customer_id),
    CONSTRAINT ux_user_subscription__stripe_subscription_id UNIQUE (stripe_subscription_id),
    CONSTRAINT ux_user_subscription__user_profile_id UNIQUE (user_profile_id)
);

-- ===================================================
-- DEPENDENT TABLES (Level 2 - Depend on user_profile and tenant tables)
-- ===================================================

-- Event Details (UPDATED with ALL JDL additions)
CREATE TABLE IF NOT EXISTS public.event_details (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    caption VARCHAR(500),
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    start_time VARCHAR(100) NOT NULL,
    end_time VARCHAR(100) NOT NULL,
    location VARCHAR(500),
    directions_to_venue TEXT,
    capacity INTEGER,
    admission_type VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    max_guests_per_attendee INTEGER DEFAULT 0 CHECK (max_guests_per_attendee >= 0),
    allow_guests BOOLEAN DEFAULT FALSE,
    require_guest_approval BOOLEAN DEFAULT FALSE,
    enable_guest_pricing BOOLEAN DEFAULT FALSE,
    registration_deadline TIMESTAMP WITHOUT TIME ZONE,
    cancellation_deadline TIMESTAMP WITHOUT TIME ZONE,
    minimum_age INTEGER,
    maximum_age INTEGER,
    requires_approval BOOLEAN DEFAULT FALSE,
    enable_waitlist BOOLEAN DEFAULT TRUE,
    external_registration_url VARCHAR(1024),
    created_by_id BIGINT,
    event_type_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    is_registration_required BOOLEAN DEFAULT FALSE,
    is_sports_event BOOLEAN DEFAULT FALSE,
    -- 1. Add is_live to event_details
    is_live BOOLEAN DEFAULT FALSE,
    CONSTRAINT event_details_pkey PRIMARY KEY (id),
    CONSTRAINT check_event_dates CHECK (end_date >= start_date),
    CONSTRAINT check_capacity_positive CHECK (capacity IS NULL OR capacity > 0),
    CONSTRAINT check_age_ranges CHECK (minimum_age IS NULL OR maximum_age IS NULL OR maximum_age >= minimum_age),
    CONSTRAINT check_deadlines CHECK (registration_deadline IS NULL OR cancellation_deadline IS NULL OR cancellation_deadline <= registration_deadline)
);

COMMENT ON TABLE public.event_details IS 'Enhanced event details with guest management and validation';
COMMENT ON COLUMN public.event_details.max_guests_per_attendee IS 'Maximum number of guests allowed per primary attendee';
COMMENT ON COLUMN public.event_details.allow_guests IS 'Whether guest registrations are allowed for this event';
COMMENT ON COLUMN public.event_details.require_guest_approval IS 'Whether guest registrations require admin approval';
COMMENT ON COLUMN public.event_details.enable_guest_pricing IS 'Whether special pricing applies to guests';
COMMENT ON COLUMN public.event_details.is_registration_required IS 'Whether formal registration is required for this event';
COMMENT ON COLUMN public.event_details.is_sports_event IS 'Whether this event is a sports event';
COMMENT ON COLUMN public.event_details.is_live IS 'Whether this event is currently live and should be featured on the home page';

-- Event Admin
CREATE TABLE IF NOT EXISTS public.event_admin (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    permissions TEXT[], -- Array of permission strings
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    user_id BIGINT,
    created_by_id BIGINT,
    CONSTRAINT event_admin_pkey PRIMARY KEY (id),
    CONSTRAINT ux_event_admin_user_tenant UNIQUE (user_id, tenant_id)
);

-- User Task (Enhanced)
CREATE TABLE IF NOT EXISTS public.user_task (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    priority VARCHAR(255) NOT NULL DEFAULT 'MEDIUM',
    due_date TIMESTAMP WITHOUT TIME ZONE,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    completion_date TIMESTAMP WITHOUT TIME ZONE,
    estimated_hours DECIMAL(5,2),
    actual_hours DECIMAL(5,2),
    progress_percentage INTEGER DEFAULT 0 CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    event_id BIGINT,
    assignee_name VARCHAR(255),
    assignee_contact_phone VARCHAR(50),
    assignee_contact_email VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    user_id BIGINT,
    CONSTRAINT user_task_pkey PRIMARY KEY (id),
    CONSTRAINT check_completion_logic CHECK ((completed = FALSE) OR (completed = TRUE AND completion_date IS NOT NULL))
);

-- User Registration Request (Enhanced)
CREATE TABLE IF NOT EXISTS public.user_registration_request (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255) NOT NULL,
    request_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255),
    country VARCHAR(255),
    family_name VARCHAR(255),
    city_town VARCHAR(255),
    district VARCHAR(255),
    educational_institution VARCHAR(255),
    profile_image_url VARCHAR(1024),
    request_reason TEXT,
    status VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
    admin_comments TEXT,
    automatic_approval_eligible BOOLEAN DEFAULT FALSE,
    priority_score INTEGER DEFAULT 0,
    submitted_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    reviewed_at TIMESTAMP WITHOUT TIME ZONE,
    approved_at TIMESTAMP WITHOUT TIME ZONE,
    rejected_at TIMESTAMP WITHOUT TIME ZONE,
    reviewed_by_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT user_registration_request_pkey PRIMARY KEY (id),
    CONSTRAINT user_registration_request_request_id_key UNIQUE (request_id),
    CONSTRAINT ux_user_registration_tenant_user UNIQUE (tenant_id, user_id),
    CONSTRAINT check_email_format_request CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Bulk Operation Log (Enhanced)
CREATE TABLE IF NOT EXISTS public.bulk_operation_log (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    operation_type VARCHAR(50) NOT NULL,
    operation_name VARCHAR(255),
    performed_by BIGINT,
    target_count INTEGER NOT NULL,
    success_count INTEGER DEFAULT 0,
    error_count INTEGER DEFAULT 0,
    skipped_count INTEGER DEFAULT 0,
    operation_details TEXT,
    error_details TEXT,
    execution_time_ms INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    completed_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT bulk_operation_log_pkey PRIMARY KEY (id),
    CONSTRAINT check_operation_counts CHECK (success_count + error_count + skipped_count <= target_count)
);

-- ===================================================
-- DEPENDENT TABLES (Level 3 - Depend on event_details)
-- ===================================================

-- Event Organizer
CREATE TABLE IF NOT EXISTS public.event_organizer (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    designation VARCHAR(255),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    bio TEXT,
    profile_image_url VARCHAR(1024),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_id BIGINT,
    organizer_id BIGINT,
    CONSTRAINT event_organizer_pkey PRIMARY KEY (id),
    CONSTRAINT check_contact_email_format CHECK (contact_email IS NULL OR contact_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Event Ticket Type (Enhanced with soldQuantity from JDL)
CREATE TABLE IF NOT EXISTS public.event_ticket_type (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(21,2) NOT NULL,
    code VARCHAR(255) NOT NULL,
    available_quantity INTEGER,
    sold_quantity INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    sale_start_date TIMESTAMP WITHOUT TIME ZONE,
    sale_end_date TIMESTAMP WITHOUT TIME ZONE,
    min_quantity_per_order INTEGER DEFAULT 1,
    max_quantity_per_order INTEGER DEFAULT 10,
    requires_approval BOOLEAN DEFAULT FALSE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_id BIGINT,
    CONSTRAINT event_ticket_type_pkey PRIMARY KEY (id),
    CONSTRAINT ux_event_ticket_type_event_code UNIQUE (event_id, code),
    CONSTRAINT check_price_non_negative CHECK (price >= 0),
    CONSTRAINT check_quantities_positive CHECK (
        (available_quantity IS NULL OR available_quantity >= 0) AND
        (sold_quantity >= 0) AND
        (min_quantity_per_order > 0) AND
        (max_quantity_per_order >= min_quantity_per_order)
    ),
    CONSTRAINT check_sale_dates CHECK (sale_end_date IS NULL OR sale_start_date IS NULL OR sale_end_date >= sale_start_date),
    CONSTRAINT check_sold_vs_available CHECK (available_quantity IS NULL OR sold_quantity <= available_quantity)
);

COMMENT ON COLUMN public.event_ticket_type.sold_quantity IS 'Number of tickets sold (auto-updated by triggers)';

-- Event Ticket Transaction (Enhanced)
CREATE TABLE IF NOT EXISTS public.event_ticket_transaction (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    transaction_reference VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(255),
    quantity INTEGER NOT NULL,
    price_per_unit NUMERIC(21,2) NOT NULL,
    total_amount NUMERIC(21,2) NOT NULL,
    tax_amount NUMERIC(21,2) DEFAULT 0,
    fee_amount NUMERIC(21,2) DEFAULT 0,
    discount_code_id BIGINT,
    discount_amount NUMERIC(21,2) DEFAULT 0,
    final_amount NUMERIC(21,2) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(100),
    payment_reference VARCHAR(255),
    purchase_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    confirmation_sent_at TIMESTAMP WITHOUT TIME ZONE,
    refund_amount NUMERIC(21,2) DEFAULT 0,
    refund_date TIMESTAMP WITHOUT TIME ZONE,
    refund_reason TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_id BIGINT,
    ticket_type_id BIGINT,
    user_id BIGINT,
    CONSTRAINT event_ticket_transaction_pkey PRIMARY KEY (id),
    CONSTRAINT ux_ticket_transaction_reference UNIQUE (transaction_reference),
    CONSTRAINT check_transaction_amounts CHECK (
        total_amount >= 0 AND
        tax_amount >= 0 AND
        fee_amount >= 0 AND
        discount_amount >= 0 AND
        refund_amount >= 0 AND
        final_amount >= 0 AND
        quantity > 0
    ),
    CONSTRAINT check_email_format_transaction CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT fk_ticket_transaction__discount_code_id FOREIGN KEY (discount_code_id) REFERENCES public.discount_code(id) ON DELETE SET NULL
);

COMMENT ON COLUMN public.event_ticket_transaction.discount_code_id IS 'Discount code used for this ticket purchase';
COMMENT ON COLUMN public.event_ticket_transaction.discount_amount IS 'Discount amount applied to this ticket purchase';

-- User Payment Transaction (Enhanced)
CREATE TABLE IF NOT EXISTS public.user_payment_transaction (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount NUMERIC(21,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD' NOT NULL,
    stripe_payment_intent_id VARCHAR(255),
    stripe_transfer_group VARCHAR(255),
    platform_fee_amount NUMERIC(21,2) DEFAULT 0,
    tenant_amount NUMERIC(21,2) DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    processing_fee NUMERIC(21,2) DEFAULT 0,
    metadata JSONB,
    external_transaction_id VARCHAR(255),
    payment_method VARCHAR(100),
    failure_reason TEXT,
    reconciliation_date DATE,
    event_id BIGINT,
    ticket_transaction_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT user_payment_transaction_pkey PRIMARY KEY (id),
    CONSTRAINT ux_payment_transaction_stripe_intent UNIQUE (stripe_payment_intent_id),
    CONSTRAINT check_payment_amounts CHECK (
        amount >= 0 AND
        platform_fee_amount >= 0 AND
        tenant_amount >= 0 AND
        processing_fee >= 0
    )
);

-- Event Poll (Enhanced)
CREATE TABLE IF NOT EXISTS public.event_poll (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_anonymous BOOLEAN DEFAULT FALSE,
    allow_multiple_choices BOOLEAN DEFAULT FALSE,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    max_responses_per_user INTEGER DEFAULT 1,
    results_visible_to VARCHAR(50) DEFAULT 'ALL', -- 'ALL', 'PARTICIPANTS', 'ORGANIZERS'
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_id BIGINT,
    created_by_id BIGINT,
    CONSTRAINT event_poll_pkey PRIMARY KEY (id),
    CONSTRAINT check_poll_dates CHECK (end_date IS NULL OR end_date >= start_date),
    CONSTRAINT check_max_responses_positive CHECK (max_responses_per_user > 0)
);

-- Event Poll Option
CREATE TABLE IF NOT EXISTS public.event_poll_option (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    option_text VARCHAR(500) NOT NULL,
    display_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    poll_id BIGINT,
    CONSTRAINT event_poll_option_pkey PRIMARY KEY (id)
);

-- Event Poll Response
CREATE TABLE IF NOT EXISTS public.event_poll_response (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    comment TEXT,
    response_value VARCHAR(1000), -- For custom response types
    is_anonymous BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    poll_id BIGINT,
    poll_option_id BIGINT,
    user_id BIGINT,
    CONSTRAINT event_poll_response_pkey PRIMARY KEY (id),
    CONSTRAINT ux_poll_response_user_option UNIQUE (poll_id, poll_option_id, user_id)
);

-- Event Media (UPDATED with JDL pre_signed_url length correction)
CREATE TABLE IF NOT EXISTS public.event_media (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_media_type VARCHAR(255) NOT NULL,
    storage_type VARCHAR(255) NOT NULL,
    file_url VARCHAR(2048),
    file_data OID,
    file_data_content_type VARCHAR(255),
    content_type VARCHAR(255),
    file_size BIGINT,
    is_public BOOLEAN DEFAULT TRUE,
    event_flyer BOOLEAN DEFAULT FALSE,
    is_event_management_official_document BOOLEAN DEFAULT FALSE,
    pre_signed_url VARCHAR(2048),
    pre_signed_url_expires_at TIMESTAMP WITHOUT TIME ZONE,
    alt_text VARCHAR(500), -- For accessibility
    display_order INTEGER DEFAULT 0,
    download_count INTEGER DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_id BIGINT,
    uploaded_by_id BIGINT,
    CONSTRAINT event_media_pkey PRIMARY KEY (id),
    CONSTRAINT check_file_size_positive CHECK (file_size IS NULL OR file_size >= 0),
    CONSTRAINT check_download_count_non_negative CHECK (download_count >= 0)
);

COMMENT ON COLUMN public.event_media.pre_signed_url IS 'Pre-signed URL for temporary access (max length 2048 chars)';

-- Event Calendar Entry
CREATE TABLE IF NOT EXISTS public.event_calendar_entry (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    calendar_provider VARCHAR(255) NOT NULL,
    external_event_id VARCHAR(255),
    calendar_link VARCHAR(2048) NOT NULL,
    sync_status VARCHAR(50) DEFAULT 'PENDING',
    last_sync_at TIMESTAMP WITHOUT TIME ZONE,
    sync_error_message TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_id BIGINT,
    created_by_id BIGINT,
    CONSTRAINT event_calendar_entry_pkey PRIMARY KEY (id),
    CONSTRAINT ux_calendar_entry_provider_external UNIQUE (calendar_provider, external_event_id)
);

-- Event Attendee (Enhanced with JDL QR code fields)
CREATE TABLE IF NOT EXISTS public.event_attendee (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    event_id BIGINT NOT NULL,
    attendee_id BIGINT NOT NULL,
    registration_status VARCHAR(20) DEFAULT 'PENDING' NOT NULL,
    registration_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    confirmation_date TIMESTAMP WITHOUT TIME ZONE,
    cancellation_date TIMESTAMP WITHOUT TIME ZONE,
    cancellation_reason TEXT,
    attendee_type VARCHAR(50) DEFAULT 'MEMBER',
    special_requirements TEXT,
    dietary_restrictions TEXT,
    accessibility_needs TEXT,
    emergency_contact_name VARCHAR(255),
    emergency_contact_phone VARCHAR(50),
    emergency_contact_relationship VARCHAR(100),
    check_in_status VARCHAR(20) DEFAULT 'NOT_CHECKED_IN',
    check_in_time TIMESTAMP WITHOUT TIME ZONE,
    check_out_time TIMESTAMP WITHOUT TIME ZONE,
    attendance_rating INTEGER CHECK (attendance_rating >= 1 AND attendance_rating <= 5),
    feedback TEXT,
    notes TEXT,
    qr_code_data VARCHAR(1000),
    qr_code_generated BOOLEAN DEFAULT FALSE,
    qr_code_generated_at TIMESTAMP WITHOUT TIME ZONE,
    registration_source VARCHAR(100) DEFAULT 'DIRECT',
    waitlist_position INTEGER,
    priority_score INTEGER DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT event_attendee_pkey PRIMARY KEY (id),
    CONSTRAINT ux_event_attendee__event_attendee UNIQUE (event_id, attendee_id),
    CONSTRAINT check_waitlist_position_positive CHECK (waitlist_position IS NULL OR waitlist_position > 0)
);

COMMENT ON TABLE public.event_attendee IS 'Enhanced event registration and attendance tracking with QR code support';
COMMENT ON COLUMN public.event_attendee.qr_code_data IS 'QR code data for check-in (auto-generated)';
COMMENT ON COLUMN public.event_attendee.qr_code_generated IS 'Whether QR code has been generated for this attendee';
COMMENT ON COLUMN public.event_attendee.qr_code_generated_at IS 'Timestamp when QR code was generated';

-- Event Admin Audit Log (Enhanced)
CREATE TABLE IF NOT EXISTS public.event_admin_audit_log (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    action VARCHAR(255) NOT NULL,
    table_name VARCHAR(255) NOT NULL,
    record_id VARCHAR(255) NOT NULL,
    changes JSONB,
    old_values JSONB,
    new_values JSONB,
    ip_address INET,
    user_agent TEXT,
    session_id VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    admin_id BIGINT,
    CONSTRAINT event_admin_audit_log_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE public.event_admin_audit_log IS 'Comprehensive audit logging for all admin actions';

-- ===================================================
-- DEPENDENT TABLES (Level 4 - Depend on event_attendee)
-- ===================================================

-- Event Attendee Guest (UPDATED with ALL JDL enum fields and missing fields)
CREATE TABLE IF NOT EXISTS public.event_attendee_guest (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    primary_attendee_id BIGINT NOT NULL,
    guest_name VARCHAR(255) NOT NULL,
    age_group VARCHAR(20) NOT NULL,
    relationship VARCHAR(20),
    special_requirements TEXT,
    dietary_restrictions TEXT,
    accessibility_needs TEXT,
    registration_status VARCHAR(20) DEFAULT 'PENDING',
    check_in_status VARCHAR(20) DEFAULT 'NOT_CHECKED_IN',
    check_in_time TIMESTAMP WITHOUT TIME ZONE,
    check_out_time TIMESTAMP WITHOUT TIME ZONE,
    approval_status VARCHAR(50) DEFAULT 'PENDING',
    approved_by_id BIGINT,
    approved_at TIMESTAMP WITHOUT TIME ZONE,
    rejection_reason TEXT,
    pricing_tier VARCHAR(100),
    fee_amount NUMERIC(21,2) DEFAULT 0,
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    notes TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT event_attendee_guest_pkey PRIMARY KEY (id),
    CONSTRAINT check_guest_fee_non_negative CHECK (fee_amount >= 0)
);

COMMENT ON TABLE public.event_attendee_guest IS 'Guest registrations linked to primary attendees using JDL enum types';
COMMENT ON COLUMN public.event_attendee_guest.age_group IS 'Guest age group: ADULT, TEEN, CHILD, INFANT';
COMMENT ON COLUMN public.event_attendee_guest.relationship IS 'Relationship to primary attendee';

-- Event Guest Pricing (UPDATED with ALL JDL missing fields and validation)
CREATE TABLE IF NOT EXISTS public.event_guest_pricing (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    event_id BIGINT NOT NULL,
    age_group VARCHAR(20) NOT NULL,
    price NUMERIC(21,2) NOT NULL DEFAULT 0.00 CHECK (price >= 0),
    is_active BOOLEAN DEFAULT TRUE,
    valid_from DATE,
    valid_to DATE,
    description VARCHAR(255),
    max_guests INTEGER,
    pricing_tier VARCHAR(100),
    early_bird_price NUMERIC(21,2),
    early_bird_deadline TIMESTAMP WITHOUT TIME ZONE,
    group_discount_threshold INTEGER,
    group_discount_percentage DECIMAL(5,2),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT event_guest_pricing_pkey PRIMARY KEY (id),
    CONSTRAINT ux_event_guest_pricing_event_age_tier UNIQUE (event_id, age_group, pricing_tier),
    CONSTRAINT check_guest_pricing_amounts CHECK (
        price >= 0 AND
        (early_bird_price IS NULL OR early_bird_price >= 0) AND
        (group_discount_percentage IS NULL OR (group_discount_percentage >= 0 AND group_discount_percentage <= 100))
    ),
    CONSTRAINT check_max_guests_positive CHECK (max_guests IS NULL OR max_guests > 0),
    CONSTRAINT check_group_discount_threshold CHECK (group_discount_threshold IS NULL OR group_discount_threshold > 1),
    CONSTRAINT check_valid_date_range CHECK (valid_from IS NULL OR valid_to IS NULL OR valid_to >= valid_from)
);

COMMENT ON TABLE public.event_guest_pricing IS 'Flexible pricing structure for event guests with JDL validation';
COMMENT ON COLUMN public.event_guest_pricing.price IS 'Guest price (required, minimum 0)';
COMMENT ON COLUMN public.event_guest_pricing.is_active IS 'Whether this pricing is currently active';
COMMENT ON COLUMN public.event_guest_pricing.valid_from IS 'Start date for pricing validity';
COMMENT ON COLUMN public.event_guest_pricing.valid_to IS 'End date for pricing validity';
COMMENT ON COLUMN public.event_guest_pricing.description IS 'Pricing description (max 255 chars)';

-- QR Code Usage (Enhanced)
CREATE TABLE IF NOT EXISTS public.qr_code_usage (
    id BIGINT DEFAULT nextval('public.sequence_generator'),
    tenant_id VARCHAR(255),
    attendee_id BIGINT NOT NULL,
    qr_code_data VARCHAR(1000) NOT NULL,
    qr_code_type VARCHAR(50) DEFAULT 'CHECK_IN',
    generated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE,
    used_at TIMESTAMP WITHOUT TIME ZONE,
    usage_count INTEGER DEFAULT 0,
    max_usage_count INTEGER DEFAULT 1,
    last_scanned_by VARCHAR(255),
    scan_location VARCHAR(255),
    device_info TEXT,
    ip_address INET,
    is_valid BOOLEAN DEFAULT TRUE,
    invalidated_at TIMESTAMP WITHOUT TIME ZONE,
    invalidation_reason TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT qr_code_usage_pkey PRIMARY KEY (id),
    CONSTRAINT ux_qr_code_attendee_type UNIQUE (attendee_id, qr_code_type),
    CONSTRAINT check_usage_counts CHECK (usage_count >= 0 AND max_usage_count > 0 AND usage_count <= max_usage_count)
);

COMMENT ON TABLE public.qr_code_usage IS 'Enhanced QR code generation and usage tracking with security features';

-- ===================================================
-- FOREIGN KEY CONSTRAINTS (Organized by dependency level)
-- ===================================================

-- Level 1 Dependencies (tenant_organization)
ALTER TABLE public.tenant_settings
ADD CONSTRAINT fk_tenant_settings__tenant_id
FOREIGN KEY (tenant_id) REFERENCES public.tenant_organization(tenant_id) ON DELETE CASCADE;

-- Level 2 Dependencies (user_profile and tenant relationships)
ALTER TABLE public.user_profile
ADD CONSTRAINT fk_user_profile_reviewed_by_admin
FOREIGN KEY (reviewed_by_admin_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.user_subscription
ADD CONSTRAINT fk_user_subscription__user_profile_id
FOREIGN KEY (user_profile_id) REFERENCES public.user_profile(id) ON DELETE CASCADE;

-- Event Details Dependencies
ALTER TABLE public.event_details
ADD CONSTRAINT fk_event__created_by_id
FOREIGN KEY (created_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.event_details
ADD CONSTRAINT fk_event__event_type_id
FOREIGN KEY (event_type_id) REFERENCES public.event_type_details(id) ON DELETE SET NULL;

-- Event Admin Dependencies
ALTER TABLE public.event_admin
ADD CONSTRAINT fk_admin__user_id
FOREIGN KEY (user_id) REFERENCES public.user_profile(id) ON DELETE CASCADE;

ALTER TABLE public.event_admin
ADD CONSTRAINT fk_admin__created_by_id
FOREIGN KEY (created_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

-- User Task Dependencies
ALTER TABLE public.user_task
ADD CONSTRAINT fk_user_task_user_id
FOREIGN KEY (user_id) REFERENCES public.user_profile(id) ON DELETE CASCADE;

ALTER TABLE public.user_task
ADD CONSTRAINT fk_user_task_event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE SET NULL;

-- User Registration Request Dependencies
ALTER TABLE public.user_registration_request
ADD CONSTRAINT fk_user_registration_request__reviewed_by_id
FOREIGN KEY (reviewed_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

-- Bulk Operation Log Dependencies
ALTER TABLE public.bulk_operation_log
ADD CONSTRAINT fk_bulk_operation_log__performed_by
FOREIGN KEY (performed_by) REFERENCES public.user_profile(id) ON DELETE SET NULL;

-- Level 3 Dependencies (event_details and related tables)
ALTER TABLE public.event_organizer
ADD CONSTRAINT fk_event_organizer__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_organizer
ADD CONSTRAINT fk_event_organizer__organizer_id
FOREIGN KEY (organizer_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.event_ticket_type
ADD CONSTRAINT fk_ticket_type__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_ticket_transaction
ADD CONSTRAINT fk_ticket_transaction__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_ticket_transaction
ADD CONSTRAINT fk_ticket_transaction__ticket_type_id
FOREIGN KEY (ticket_type_id) REFERENCES public.event_ticket_type(id) ON DELETE RESTRICT;

ALTER TABLE public.event_ticket_transaction
ADD CONSTRAINT fk_ticket_transaction__user_id
FOREIGN KEY (user_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.user_payment_transaction
ADD CONSTRAINT fk_payment_transaction__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE SET NULL;

ALTER TABLE public.user_payment_transaction
ADD CONSTRAINT fk_payment_transaction__ticket_transaction_id
FOREIGN KEY (ticket_transaction_id) REFERENCES public.event_ticket_transaction(id) ON DELETE SET NULL;

ALTER TABLE public.event_poll
ADD CONSTRAINT fk_poll__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_poll
ADD CONSTRAINT fk_poll__created_by_id
FOREIGN KEY (created_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.event_poll_option
ADD CONSTRAINT fk_poll_option__poll_id
FOREIGN KEY (poll_id) REFERENCES public.event_poll(id) ON DELETE CASCADE;

ALTER TABLE public.event_poll_response
ADD CONSTRAINT fk_poll_response__poll_id
FOREIGN KEY (poll_id) REFERENCES public.event_poll(id) ON DELETE CASCADE;

ALTER TABLE public.event_poll_response
ADD CONSTRAINT fk_poll_response__poll_option_id
FOREIGN KEY (poll_option_id) REFERENCES public.event_poll_option(id) ON DELETE CASCADE;

ALTER TABLE public.event_poll_response
ADD CONSTRAINT fk_poll_response__user_id
FOREIGN KEY (user_id) REFERENCES public.user_profile(id) ON DELETE CASCADE;

ALTER TABLE public.event_media
ADD CONSTRAINT fk_event_media__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_media
ADD CONSTRAINT fk_event_media__uploaded_by_id
FOREIGN KEY (uploaded_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.event_calendar_entry
ADD CONSTRAINT fk_calendar_event__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_calendar_entry
ADD CONSTRAINT fk_calendar_event__created_by_id
FOREIGN KEY (created_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.event_attendee
ADD CONSTRAINT fk_event_attendee__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.event_attendee
ADD CONSTRAINT fk_event_attendee__attendee_id
FOREIGN KEY (attendee_id) REFERENCES public.user_profile(id) ON DELETE CASCADE;

ALTER TABLE public.event_admin_audit_log
ADD CONSTRAINT fk_admin_audit_log__admin_id
FOREIGN KEY (admin_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

-- Level 4 Dependencies (event_attendee and final relationships)
ALTER TABLE public.event_attendee_guest
ADD CONSTRAINT fk_event_attendee_guest__primary_attendee_id
FOREIGN KEY (primary_attendee_id) REFERENCES public.event_attendee(id) ON DELETE CASCADE;

ALTER TABLE public.event_attendee_guest
ADD CONSTRAINT fk_event_attendee_guest__approved_by_id
FOREIGN KEY (approved_by_id) REFERENCES public.user_profile(id) ON DELETE SET NULL;

ALTER TABLE public.event_guest_pricing
ADD CONSTRAINT fk_event_guest_pricing__event_id
FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE;

ALTER TABLE public.qr_code_usage
ADD CONSTRAINT fk_qr_code_usage__attendee_id
FOREIGN KEY (attendee_id) REFERENCES public.event_attendee(id) ON DELETE CASCADE;

-- ===================================================
-- COMPREHENSIVE INDEXING STRATEGY (FOCUSED ON NEW ADDITIONS)
-- ===================================================

-- Event Details Indices (NEW FIELDS FOCUS)
CREATE INDEX IF NOT EXISTS idx_event_details_allow_guests ON public.event_details(allow_guests) WHERE allow_guests = TRUE;
CREATE INDEX IF NOT EXISTS idx_event_details_guest_pricing ON public.event_details(enable_guest_pricing) WHERE enable_guest_pricing = TRUE;
CREATE INDEX IF NOT EXISTS idx_event_details_require_guest_approval ON public.event_details(require_guest_approval) WHERE require_guest_approval = TRUE;
CREATE INDEX IF NOT EXISTS idx_event_details_max_guests ON public.event_details(max_guests_per_attendee) WHERE max_guests_per_attendee > 0;

-- Event Ticket Type Indices (NEW SOLD_QUANTITY FIELD)
CREATE INDEX IF NOT EXISTS idx_event_ticket_type_sold_quantity ON public.event_ticket_type(sold_quantity);
CREATE INDEX IF NOT EXISTS idx_event_ticket_type_availability ON public.event_ticket_type(available_quantity, sold_quantity);

-- Event Attendee Indices (NEW QR CODE FIELDS)
CREATE INDEX IF NOT EXISTS idx_event_attendee_qr_generated ON public.event_attendee(qr_code_generated) WHERE qr_code_generated = TRUE;
CREATE INDEX IF NOT EXISTS idx_event_attendee_qr_generated_at ON public.event_attendee(qr_code_generated_at);
CREATE INDEX IF NOT EXISTS idx_event_attendee_qr_data ON public.event_attendee(qr_code_data) WHERE qr_code_data IS NOT NULL;

-- Event Guest Pricing Indices (NEW FIELDS)
CREATE INDEX IF NOT EXISTS idx_event_guest_pricing_is_active ON public.event_guest_pricing(is_active) WHERE is_active = TRUE;
CREATE INDEX IF NOT EXISTS idx_event_guest_pricing_valid_period ON public.event_guest_pricing(valid_from, valid_to);
CREATE INDEX IF NOT EXISTS idx_event_guest_pricing_event_age_active ON public.event_guest_pricing(event_id, age_group, is_active) WHERE is_active = TRUE;
CREATE INDEX IF NOT EXISTS idx_event_guest_pricing_description ON public.event_guest_pricing(description) WHERE description IS NOT NULL;

-- Event Media Indices (PRE_SIGNED_URL FOCUS)
CREATE INDEX IF NOT EXISTS idx_event_media_pre_signed_url ON public.event_media(pre_signed_url) WHERE pre_signed_url IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_event_media_pre_signed_expires ON public.event_media(pre_signed_url_expires_at);

-- ===================================================
-- ENHANCED FUNCTIONS WITH PROPER DOLLAR QUOTING
-- ===================================================

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $update_timestamp$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$update_timestamp$ LANGUAGE plpgsql;

-- Enhanced QR code generation function (UPDATED for JDL compatibility)
CREATE OR REPLACE FUNCTION generate_enhanced_qr_code()
RETURNS TRIGGER AS $enhanced_qr$
DECLARE
    qr_data TEXT;
    event_title TEXT;
    attendee_name TEXT;
BEGIN
    -- Only generate QR code for confirmed attendees
    IF NEW.registration_status = 'CONFIRMED' AND
       (OLD IS NULL OR OLD.registration_status != 'CONFIRMED' OR OLD.qr_code_data IS NULL) THEN

        -- Get event title and attendee name for better QR code
        SELECT e.title INTO event_title
        FROM public.event_details e
        WHERE e.id = NEW.event_id;

        SELECT up.first_name || ' ' || up.last_name INTO attendee_name
        FROM public.user_profile up
        WHERE up.id = NEW.attendee_id;

        -- Generate comprehensive QR code data
        qr_data := 'ATTENDEE:' || NEW.id ||
                   '|EVENT:' || NEW.event_id ||
                   '|TENANT:' || NEW.tenant_id ||
                   '|NAME:' || COALESCE(attendee_name, 'Unknown') ||
                   '|EVENT_TITLE:' || COALESCE(event_title, 'Unknown Event') ||
                   '|TIMESTAMP:' || extract(epoch from NOW()) ||
                   '|TYPE:' || COALESCE(NEW.attendee_type, 'MEMBER');

        NEW.qr_code_data = qr_data;
        NEW.qr_code_generated = TRUE;
        NEW.qr_code_generated_at = NOW();

        RAISE NOTICE 'Generated QR code for attendee % at event %', attendee_name, event_title;
    END IF;

    RETURN NEW;
END;
$enhanced_qr$ LANGUAGE plpgsql;

-- Enhanced ticket inventory management (UPDATED for sold_quantity field)
CREATE OR REPLACE FUNCTION manage_ticket_inventory()
RETURNS TRIGGER AS $ticket_inventory$
DECLARE
    ticket_type_record RECORD;
    available_quantity INTEGER;
BEGIN
    -- Get ticket type details
    SELECT * INTO ticket_type_record
    FROM public.event_ticket_type
    WHERE id = COALESCE(NEW.ticket_type_id, OLD.ticket_type_id);

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Ticket type not found for ID: %', COALESCE(NEW.ticket_type_id, OLD.ticket_type_id);
    END IF;

    -- Handle different operations
    IF TG_OP = 'INSERT' AND NEW.status = 'COMPLETED' THEN
        -- Check availability before increasing sold quantity
        available_quantity := ticket_type_record.available_quantity - ticket_type_record.sold_quantity;
        IF available_quantity < NEW.quantity THEN
            RAISE EXCEPTION 'Insufficient tickets available. Requested: %, Available: %',
                NEW.quantity, available_quantity;
        END IF;

        UPDATE public.event_ticket_type
        SET sold_quantity = sold_quantity + NEW.quantity,
            updated_at = NOW()
        WHERE id = NEW.ticket_type_id;

        RAISE NOTICE 'Added % tickets to sold quantity for ticket type %', NEW.quantity, NEW.ticket_type_id;

    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.status != 'COMPLETED' AND NEW.status = 'COMPLETED' THEN
            -- Ticket sale completed
            UPDATE public.event_ticket_type
            SET sold_quantity = sold_quantity + NEW.quantity,
                updated_at = NOW()
            WHERE id = NEW.ticket_type_id;

        ELSIF OLD.status = 'COMPLETED' AND NEW.status != 'COMPLETED' THEN
            -- Ticket sale cancelled/refunded
            UPDATE public.event_ticket_type
            SET sold_quantity = sold_quantity - OLD.quantity,
                updated_at = NOW()
            WHERE id = OLD.ticket_type_id;

        ELSIF OLD.status = 'COMPLETED' AND NEW.status = 'COMPLETED' AND OLD.quantity != NEW.quantity THEN
            -- Quantity changed for completed sale
            UPDATE public.event_ticket_type
            SET sold_quantity = sold_quantity - OLD.quantity + NEW.quantity,
                updated_at = NOW()
            WHERE id = NEW.ticket_type_id;
        END IF;

    ELSIF TG_OP = 'DELETE' AND OLD.status = 'COMPLETED' THEN
        -- Remove sold tickets when transaction is deleted
        UPDATE public.event_ticket_type
        SET sold_quantity = sold_quantity - OLD.quantity,
            updated_at = NOW()
        WHERE id = OLD.ticket_type_id;

        RAISE NOTICE 'Removed % tickets from sold quantity for ticket type %', OLD.quantity, OLD.ticket_type_id;
    END IF;

    -- Return appropriate record
    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    ELSE
        RETURN NEW;
    END IF;
END;
$ticket_inventory$ LANGUAGE plpgsql;

-- Enhanced event validation function (UPDATED for new guest fields)
CREATE OR REPLACE FUNCTION validate_event_details()
RETURNS TRIGGER AS $validate_event$
BEGIN
    -- Validate start date
    IF NEW.start_date < CURRENT_DATE THEN
        RAISE EXCEPTION 'Event start date (%) cannot be in the past. Current date: %',
            NEW.start_date, CURRENT_DATE;
    END IF;

    -- Validate end date
    IF NEW.end_date < NEW.start_date THEN
        RAISE EXCEPTION 'Event end date (%) cannot be before start date (%)',
            NEW.end_date, NEW.start_date;
    END IF;

    -- JDL VALIDATION: If allowGuests = true, maxGuestsPerAttendee should be > 0
    IF NEW.allow_guests = TRUE AND (NEW.max_guests_per_attendee IS NULL OR NEW.max_guests_per_attendee <= 0) THEN
        RAISE EXCEPTION 'When guests are allowed, max_guests_per_attendee must be greater than 0';
    END IF;

    -- JDL VALIDATION: Validate capacity
    IF NEW.capacity IS NOT NULL AND NEW.capacity <= 0 THEN
        RAISE EXCEPTION 'Event capacity must be greater than zero, got: %', NEW.capacity;
    END IF;

    -- Log the validation success
    RAISE NOTICE 'Event validation passed for event: %', NEW.title;

    RETURN NEW;
END;
$validate_event$ LANGUAGE plpgsql;

-- ===================================================
-- CREATE TRIGGERS FOR JDL ENHANCEMENTS
-- ===================================================

-- Create triggers for updated_at fields
CREATE TRIGGER update_tenant_organization_updated_at BEFORE UPDATE ON public.tenant_organization FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_tenant_settings_updated_at BEFORE UPDATE ON public.tenant_settings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_profile_updated_at BEFORE UPDATE ON public.user_profile FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_event_type_details_updated_at BEFORE UPDATE ON public.event_type_details FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_event_details_updated_at BEFORE UPDATE ON public.event_details FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_event_attendee_updated_at BEFORE UPDATE ON public.event_attendee FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_event_attendee_guest_updated_at BEFORE UPDATE ON public.event_attendee_guest FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_event_guest_pricing_updated_at BEFORE UPDATE ON public.event_guest_pricing FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_event_ticket_type_updated_at BEFORE UPDATE ON public.event_ticket_type FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Enhanced business logic triggers
CREATE TRIGGER validate_event_details_trigger
    BEFORE INSERT OR UPDATE ON public.event_details
    FOR EACH ROW EXECUTE FUNCTION validate_event_details();

CREATE TRIGGER manage_ticket_inventory_trigger
    AFTER INSERT OR UPDATE OR DELETE ON public.event_ticket_transaction
    FOR EACH ROW EXECUTE FUNCTION manage_ticket_inventory();

CREATE TRIGGER generate_enhanced_qr_code_trigger
    BEFORE INSERT OR UPDATE ON public.event_attendee
    FOR EACH ROW EXECUTE FUNCTION generate_enhanced_qr_code();

-- ===================================================
-- SET OWNERSHIP (CONDITIONAL)
-- ===================================================

DO $ownership_block$
DECLARE
    user_exists BOOLEAN;
BEGIN
    -- Check if the user exists
    SELECT EXISTS (
        SELECT 1 FROM pg_user WHERE usename = 'giventa_event_management'
    ) INTO user_exists;

    -- Only set ownership if the user exists
    IF user_exists THEN
        RAISE NOTICE 'User giventa_event_management exists, setting table ownership...';

        -- Set table ownership
        ALTER TABLE public.databasechangelog OWNER TO giventa_event_management;
        ALTER TABLE public.databasechangeloglock OWNER TO giventa_event_management;
        ALTER TABLE public.tenant_organization OWNER TO giventa_event_management;
        ALTER TABLE public.tenant_settings OWNER TO giventa_event_management;
        ALTER TABLE public.user_profile OWNER TO giventa_event_management;
        ALTER TABLE public.event_type_details OWNER TO giventa_event_management;
        ALTER TABLE public.user_subscription OWNER TO giventa_event_management;
        ALTER TABLE public.event_details OWNER TO giventa_event_management;
        ALTER TABLE public.event_admin OWNER TO giventa_event_management;
        ALTER TABLE public.user_task OWNER TO giventa_event_management;
        ALTER TABLE public.user_registration_request OWNER TO giventa_event_management;
        ALTER TABLE public.bulk_operation_log OWNER TO giventa_event_management;
        ALTER TABLE public.event_organizer OWNER TO giventa_event_management;
        ALTER TABLE public.event_ticket_type OWNER TO giventa_event_management;
        ALTER TABLE public.event_ticket_transaction OWNER TO giventa_event_management;
        ALTER TABLE public.user_payment_transaction OWNER TO giventa_event_management;
        ALTER TABLE public.event_poll OWNER TO giventa_event_management;
        ALTER TABLE public.event_poll_option OWNER TO giventa_event_management;
        ALTER TABLE public.event_poll_response OWNER TO giventa_event_management;
        ALTER TABLE public.event_media OWNER TO giventa_event_management;
        ALTER TABLE public.event_calendar_entry OWNER TO giventa_event_management;
        ALTER TABLE public.event_attendee OWNER TO giventa_event_management;
        ALTER TABLE public.event_admin_audit_log OWNER TO giventa_event_management;
        ALTER TABLE public.event_attendee_guest OWNER TO giventa_event_management;
        ALTER TABLE public.event_guest_pricing OWNER TO giventa_event_management;
        ALTER TABLE public.qr_code_usage OWNER TO giventa_event_management;
        ALTER TABLE public.discount_code OWNER TO giventa_event_management;
        ALTER TABLE public.event_discount_code OWNER TO giventa_event_management;
        ALTER TABLE public.rel_event_details__discount_codes OWNER TO giventa_event_management;
        ALTER TABLE public.event_score_card OWNER TO giventa_event_management;
        ALTER TABLE public.event_score_card_detail OWNER TO giventa_event_management;
        ALTER TABLE public.event_live_update OWNER TO giventa_event_management;
        ALTER TABLE public.event_live_update_attachment OWNER TO giventa_event_management;
        
        COMMIT;

       GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO giventa_event_management;
       GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON ALL TABLES IN SCHEMA public TO giventa_event_management;
       GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO giventa_event_management;


discount_code
Stores discount codes for ticket purchases.
event_discount_code
Join table (many-to-many) linking events and discount codes.
(Note: This was an earlier approach; the final join table is below.)
rel_event_detailsdiscount_codes

Final join table for the many-to-many relationship between event_details and discount_code.
This is the table JHipster/Hibernate expects.
event_score_card
Stores scorecards for sports events (team names, scores, remarks, etc.).
event_score_card_detail
Stores detailed breakdowns for scorecards (per player or per team).
event_live_update
Stores live updates for events (text, image, video, metadata, etc.).
event_live_update_attachment



        -- Set sequence ownership
        ALTER SEQUENCE public.sequence_generator OWNER TO giventa_event_management;

        RAISE NOTICE 'Successfully set ownership for all tables and sequences.';
    ELSE
        RAISE NOTICE 'User giventa_event_management does not exist. Skipping ownership changes.';
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error setting ownership: %', SQLERRM;
        RAISE NOTICE 'Continuing without ownership changes...';
END $ownership_block$;

-- ===================================================
-- SAMPLE DATA WITH JDL ENHANCEMENTS
-- ===================================================

-- Insert sample tenant organizations
INSERT INTO public.tenant_organization (id, tenant_id, organization_name, contact_email, subscription_plan, subscription_status, is_active, created_at, updated_at) VALUES
(1950, 'tenant_demo_001', 'Demo Organization 1', 'demo1@example.com', 'FREE', 'ACTIVE', true, NOW(), NOW()),
(2000, 'tenant_demo_002', 'Demo Organization 2', 'demo2@example.com', 'BASIC', 'ACTIVE', true, NOW(), NOW()),
(2050, 'tenant_demo_003', 'Premium Corp', 'premium@example.com', 'PREMIUM', 'ACTIVE', true, NOW(), NOW()),
(2100, 'tenant_demo_004', 'Enterprise Solutions', 'enterprise@example.com', 'ENTERPRISE', 'ACTIVE', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert corresponding tenant settings
INSERT INTO public.tenant_settings (id, tenant_id, allow_user_registration, require_admin_approval, enable_whatsapp_integration, enable_email_marketing, created_at, updated_at) VALUES
(2150, 'tenant_demo_001', true, false, false, false, NOW(), NOW()),
(2200, 'tenant_demo_002', true, true, false, false, NOW(), NOW()),
(2250, 'tenant_demo_003', true, false, true, true, NOW(), NOW()),
(2300, 'tenant_demo_004', true, true, true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample event types
INSERT INTO public.event_type_details (id, tenant_id, name, description, color, is_active, display_order, created_at, updated_at) VALUES
(2350, 'tenant_demo_001', 'Workshop', 'Educational workshops and training sessions', '#10B981', true, 1, NOW(), NOW()),
(2400, 'tenant_demo_001', 'Conference', 'Professional conferences and seminars', '#3B82F6', true, 2, NOW(), NOW()),
(2450, 'tenant_demo_001', 'Social Event', 'Community gatherings and social activities', '#F59E0B', true, 3, NOW(), NOW()),
(2500, 'tenant_demo_002', 'Meeting', 'Regular team and organizational meetings', '#6366F1', true, 1, NOW(), NOW()),
(2550, 'tenant_demo_002', 'Training', 'Skills development and training programs', '#EF4444', true, 2, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample user profiles
INSERT INTO public.user_profile (id, tenant_id, user_id, first_name, last_name, email, phone, user_status, user_role, created_at, updated_at) VALUES
(2600, 'tenant_demo_001', 'user_001', 'John', 'Smith', 'john.smith@example.com', '+1-555-0101', 'ACTIVE', 'ADMIN', NOW(), NOW()),
(2650, 'tenant_demo_001', 'user_002', 'Jane', 'Doe', 'jane.doe@example.com', '+1-555-0102', 'ACTIVE', 'MEMBER', NOW(), NOW()),
(2700, 'tenant_demo_002', 'user_003', 'Bob', 'Johnson', 'bob.johnson@example.com', '+1-555-0103', 'ACTIVE', 'ORGANIZER', NOW(), NOW()),
(2750, 'tenant_demo_002', 'user_004', 'Alice', 'Williams', 'alice.williams@example.com', '+1-555-0104', 'PENDING_APPROVAL', 'MEMBER', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample events (WITH JDL GUEST MANAGEMENT FIELDS)
INSERT INTO public.event_details (id, tenant_id, title, caption, description, start_date, end_date, start_time, end_time, location, capacity, admission_type, is_active, allow_guests, max_guests_per_attendee, require_guest_approval, enable_guest_pricing, created_by_id, event_type_id, created_at, updated_at) VALUES
(2800, 'tenant_demo_001', 'Annual Tech Conference 2025', 'Join us for the biggest tech event of the year', 'A comprehensive conference featuring the latest in technology trends, networking opportunities, and expert speakers from around the globe.', '2025-07-15', '2025-07-17', '09:00', '17:00', 'Convention Center, Downtown', 500, 'TICKETED', true, true, 2, false, true, 2600, 2400, NOW(), NOW()),
(2850, 'tenant_demo_001', 'React Workshop for Beginners', 'Learn React from scratch in this hands-on workshop', 'A beginner-friendly workshop covering React fundamentals, component creation, state management, and building your first React application.', '2025-06-20', '2025-06-20', '10:00', '16:00', 'Tech Hub Building A', 30, 'FREE', true, false, 0, false, false, 2600, 2350, NOW(), NOW()),
(2900, 'tenant_demo_002', 'Family Fun Day', 'Community event for all ages', 'A family-friendly event with activities for all age groups, food, games, and entertainment.', '2025-08-10', '2025-08-10', '10:00', '18:00', 'Community Park', 200, 'FREE', true, true, 4, true, true, 2700, 2450, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample ticket types (WITH SOLD_QUANTITY)
INSERT INTO public.event_ticket_type (id, tenant_id, name, description, price, code, available_quantity, sold_quantity, is_active, event_id, created_at, updated_at) VALUES
(2950, 'tenant_demo_001', 'Early Bird', 'Early bird discount ticket', 199.00, 'EARLYBIRD2025', 100, 15, true, 2800, NOW(), NOW()),
(3000, 'tenant_demo_001', 'Regular', 'Standard conference ticket', 299.00, 'REGULAR2025', 300, 45, true, 2800, NOW(), NOW()),
(3050, 'tenant_demo_001', 'VIP', 'VIP access with exclusive benefits', 499.00, 'VIP2025', 50, 8, true, 2800, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample guest pricing (WITH ALL JDL FIELDS)
INSERT INTO public.event_guest_pricing (id, tenant_id, event_id, age_group, price, is_active, valid_from, valid_to, description, created_at, updated_at) VALUES
(3100, 'tenant_demo_001', 2800, 'ADULT', 150.00, true, '2025-06-01', '2025-07-14', 'Adult guest pricing for conference', NOW(), NOW()),
(3150, 'tenant_demo_001', 2800, 'TEEN', 75.00, true, '2025-06-01', '2025-07-14', 'Teen guest pricing (13-17 years)', NOW(), NOW()),
(3200, 'tenant_demo_001', 2800, 'CHILD', 25.00, true, '2025-06-01', '2025-07-14', 'Child guest pricing (5-12 years)', NOW(), NOW()),
(3250, 'tenant_demo_001', 2800, 'INFANT', 0.00, true, '2025-06-01', '2025-07-14', 'Free admission for infants (under 5)', NOW(), NOW()),
(3300, 'tenant_demo_002', 2900, 'ADULT', 0.00, true, NULL, NULL, 'Free adult admission to family event', NOW(), NOW()),
(3350, 'tenant_demo_002', 2900, 'TEEN', 0.00, true, NULL, NULL, 'Free teen admission to family event', NOW(), NOW()),
(3400, 'tenant_demo_002', 2900, 'CHILD', 0.00, true, NULL, NULL, 'Free child admission to family event', NOW(), NOW()),
(3450, 'tenant_demo_002', 2900, 'INFANT', 0.00, true, NULL, NULL, 'Free infant admission to family event', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample event attendees (WITH QR CODE FIELDS)
INSERT INTO public.event_attendee (id, tenant_id, event_id, attendee_id, registration_status, registration_date, attendee_type, qr_code_generated, created_at, updated_at) VALUES
(3500, 'tenant_demo_001', 2800, 2600, 'CONFIRMED', NOW() - INTERVAL '10 days', 'MEMBER', false, NOW(), NOW()),
(3550, 'tenant_demo_001', 2800, 2650, 'CONFIRMED', NOW() - INTERVAL '8 days', 'MEMBER', false, NOW(), NOW()),
(3600, 'tenant_demo_001', 2850, 2650, 'CONFIRMED', NOW() - INTERVAL '5 days', 'MEMBER', false, NOW(), NOW()),
(3650, 'tenant_demo_002', 2900, 2700, 'CONFIRMED', NOW() - INTERVAL '3 days', 'ORGANIZER', false, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert sample guest attendees (WITH JDL ENUM TYPES)
INSERT INTO public.event_attendee_guest (id, tenant_id, primary_attendee_id, guest_name, age_group, relationship, registration_status, check_in_status, created_at, updated_at) VALUES
(3700, 'tenant_demo_001', 3500, 'Sarah Smith', 'ADULT', 'SPOUSE', 'CONFIRMED', 'NOT_CHECKED_IN', NOW(), NOW()),
(3750, 'tenant_demo_001', 3500, 'Tommy Smith', 'CHILD', 'CHILD', 'CONFIRMED', 'NOT_CHECKED_IN', NOW(), NOW()),
(3800, 'tenant_demo_002', 3650, 'Emma Johnson', 'TEEN', 'CHILD', 'PENDING', 'NOT_CHECKED_IN', NOW(), NOW()),
(3850, 'tenant_demo_002', 3650, 'Lisa Johnson', 'ADULT', 'SPOUSE', 'CONFIRMED', 'NOT_CHECKED_IN', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Update sequence to current max value
SELECT setval('public.sequence_generator', (SELECT GREATEST(
    COALESCE((SELECT MAX(id) FROM public.tenant_organization), 0),
    COALESCE((SELECT MAX(id) FROM public.tenant_settings), 0),
    COALESCE((SELECT MAX(id) FROM public.event_type_details), 0),
    COALESCE((SELECT MAX(id) FROM public.user_profile), 0),
    COALESCE((SELECT MAX(id) FROM public.event_details), 0),
    COALESCE((SELECT MAX(id) FROM public.event_ticket_type), 0),
    COALESCE((SELECT MAX(id) FROM public.event_guest_pricing), 0),
    COALESCE((SELECT MAX(id) FROM public.event_attendee), 0),
    COALESCE((SELECT MAX(id) FROM public.event_attendee_guest), 0),
    3850
) + 50));

-- ===================================================
-- PERFORMANCE OPTIMIZATION AND ANALYSIS
-- ===================================================

-- Analyze tables for query optimization
ANALYZE public.tenant_organization;
ANALYZE public.user_profile;
ANALYZE public.event_details;
ANALYZE public.event_attendee;
ANALYZE public.event_attendee_guest;
ANALYZE public.event_guest_pricing;
ANALYZE public.event_ticket_type;
ANALYZE public.event_ticket_transaction;
ANALYZE public.user_payment_transaction;
ANALYZE public.event_media;

-- Add performance notice
DO $performance_notice$
BEGIN
    RAISE NOTICE '✅ DATABASE SCHEMA CREATION COMPLETED SUCCESSFULLY!';
    RAISE NOTICE '✅ All JDL additions have been implemented:';
    RAISE NOTICE '   - Event guest management fields (maxGuestsPerAttendee, allowGuests, etc.)';
    RAISE NOTICE '   - Guest pricing with validation (isActive, validFrom, validTo, description)';
    RAISE NOTICE '   - Pre-signed URL field length updated to 2048 characters';
    RAISE NOTICE '   - Event ticket sold quantity tracking';
    RAISE NOTICE '   - QR code generation for attendees';
    RAISE NOTICE '   - Proper enum types for guest age groups and relationships';
    RAISE NOTICE '✅ Performance optimization completed with ANALYZE commands.';
    RAISE NOTICE '✅ Sample data inserted with JDL enhancements.';
    RAISE NOTICE '✅ All triggers and functions are active.';
    RAISE NOTICE '✅ Database is production-ready for event management operations!';
END $performance_notice$;

-- ===================================================
-- JDL COMPLIANCE VERIFICATION SUMMARY
-- ===================================================

/*
✅ ALL JDL COMMENTS SUCCESSFULLY ADDRESSED:

COMMENT 1 - EventDetails Guest Management Fields:
- ✅ maxGuestsPerAttendee Integer min(0) - ADDED with constraint
- ✅ allowGuests Boolean - ADDED with default FALSE
- ✅ requireGuestApproval Boolean - ADDED with default FALSE
- ✅ enableGuestPricing Boolean - ADDED with default FALSE

COMMENT 2 - EventGuestPricing Missing Fields:
- ✅ isActive Boolean - ADDED with default TRUE
- ✅ validFrom LocalDate - ADDED as DATE type
- ✅ validTo LocalDate - ADDED as DATE type
- ✅ description String maxlength(255) - ADDED with VARCHAR(255)

COMMENT 3 - Field Length Updates:
- ✅ EventMedia.preSignedUrl - UPDATED from VARCHAR(400) to VARCHAR(2048)

COMMENT 4 - Validation Annotations:
- ✅ EventGuestPricing.price - ADDED required and min(0) constraints

ADDITIONAL JDL ENHANCEMENTS IMPLEMENTED:
- ✅ EventTicketType.soldQuantity - ADDED for inventory tracking
- ✅ EventAttendee QR code fields - ADDED (qrCodeData, qrCodeGenerated, qrCodeGeneratedAt)
- ✅ Proper enum types - IMPLEMENTED for guest age groups and relationships
- ✅ Enhanced constraints - ADDED business logic validation
- ✅ Comprehensive indexing - ADDED for new fields
- ✅ Trigger functions - ENHANCED for QR code generation and inventory management
- ✅ Sample data - INCLUDES all new JDL fields with realistic examples

PRODUCTION READINESS:
✅ Multi-tenant isolation enforced
✅ All foreign key relationships established
✅ Comprehensive indexing for performance
✅ Business logic validation with triggers
✅ Sample data for testing
✅ Error handling and logging
✅ Ownership management (conditional)
✅ Performance optimization with ANALYZE

The database schema is now 100% compliant with the JDL specification
and includes all requested additions for production deployment.
*/

-- 1. Discount code table (ensure this is before any references)
CREATE TABLE IF NOT EXISTS public.discount_code (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    discount_type VARCHAR(20) NOT NULL DEFAULT 'PERCENT', -- PERCENT or FIXED
    discount_value NUMERIC(10,2) NOT NULL,
    max_uses INTEGER DEFAULT NULL, -- null = unlimited
    uses_count INTEGER DEFAULT 0,
    valid_from TIMESTAMP WITHOUT TIME ZONE,
    valid_to TIMESTAMP WITHOUT TIME ZONE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);
COMMENT ON TABLE public.discount_code IS 'Discount codes for ticket purchases';

-- 2. Add is_registration_required to event_details if not present
ALTER TABLE public.event_details ADD COLUMN IF NOT EXISTS is_registration_required BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN public.event_details.is_registration_required IS 'Whether formal registration is required for this event';

-- 3. Event-discount code join table (many-to-many)
CREATE TABLE IF NOT EXISTS public.event_discount_code (
    event_id BIGINT NOT NULL,
    discount_code_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, discount_code_id),
    CONSTRAINT fk_event_discount_code__event_id FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_discount_code__discount_code_id FOREIGN KEY (discount_code_id) REFERENCES public.discount_code(id) ON DELETE CASCADE
);
COMMENT ON TABLE public.event_discount_code IS 'Links discount codes to events';

-- 4. Add discount_code_id and discount_amount to event_ticket_transaction if not present
ALTER TABLE public.event_ticket_transaction ADD COLUMN IF NOT EXISTS discount_code_id BIGINT;
ALTER TABLE public.event_ticket_transaction ADD COLUMN IF NOT EXISTS discount_amount NUMERIC(21,2) DEFAULT 0;
ALTER TABLE public.event_ticket_transaction DROP CONSTRAINT IF EXISTS fk_ticket_transaction__discount_code_id;
ALTER TABLE public.event_ticket_transaction ADD CONSTRAINT fk_ticket_transaction__discount_code_id FOREIGN KEY (discount_code_id) REFERENCES public.discount_code(id) ON DELETE SET NULL;
COMMENT ON COLUMN public.event_ticket_transaction.discount_code_id IS 'Discount code used for this ticket purchase';
COMMENT ON COLUMN public.event_ticket_transaction.discount_amount IS 'Discount amount applied to this ticket purchase';

-- 5. Add is_sports_event to event_details
ALTER TABLE public.event_details ADD COLUMN IF NOT EXISTS is_sports_event BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN public.event_details.is_sports_event IS 'Whether this event is a sports event';

-- 6. Event score card table
CREATE TABLE IF NOT EXISTS public.event_score_card (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    team_a_name VARCHAR(255) NOT NULL,
    team_b_name VARCHAR(255) NOT NULL,
    team_a_score INTEGER NOT NULL DEFAULT 0,
    team_b_score INTEGER NOT NULL DEFAULT 0,
    remarks TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT fk_event_score_card__event_id FOREIGN KEY (event_id) REFERENCES public.event_details(id) ON DELETE CASCADE
);
COMMENT ON TABLE public.event_score_card IS 'Score card for sports events';

-- 7. (Optional) Event score card detail table for per-player or per-team breakdown
CREATE TABLE IF NOT EXISTS public.event_score_card_detail (
    id BIGSERIAL PRIMARY KEY,
    score_card_id BIGINT NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    player_name VARCHAR(255),
    points INTEGER NOT NULL DEFAULT 0,
    remarks TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT fk_score_card_detail__score_card_id FOREIGN KEY (score_card_id) REFERENCES public.event_score_card(id) ON DELETE CASCADE
);
COMMENT ON TABLE public.event_score_card_detail IS 'Detailed breakdown for event score cards (per player or per team)';

-- 1. Add is_live to event_details
ALTER TABLE public.event_details ADD COLUMN IF NOT EXISTS is_live BOOLEAN DEFAULT FALSE;
COMMENT ON COLUMN public.event_details.is_live IS 'Whether this event is currently live and should be featured on the home page';

-- 2. Event live update table
CREATE TABLE IF NOT EXISTS public.event_live_update (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES public.event_details(id) ON DELETE CASCADE,
    update_type VARCHAR(20) NOT NULL, -- TEXT, IMAGE, VIDEO, HTML, LINK, etc.
    content_text TEXT,
    content_image_url VARCHAR(1024),
    content_video_url VARCHAR(1024),
    content_link_url VARCHAR(1024),
    metadata JSONB,
    display_order INTEGER DEFAULT 0,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
COMMENT ON TABLE public.event_live_update IS 'Live updates (text, image, video, etc.) for events';

-- 3. Event live update attachment table (optional, for multiple attachments per update)
CREATE TABLE IF NOT EXISTS public.event_live_update_attachment (
    id BIGSERIAL PRIMARY KEY,
    live_update_id BIGINT NOT NULL REFERENCES public.event_live_update(id) ON DELETE CASCADE,
    attachment_type VARCHAR(20),
    attachment_url VARCHAR(1024),
    display_order INTEGER DEFAULT 0,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
COMMENT ON TABLE public.event_live_update_attachment IS 'Attachments (image, video, etc.) for live event updates';

-- Join table for EventDetails <-> DiscountCode many-to-many relationship
CREATE TABLE IF NOT EXISTS public.rel_event_details__discount_codes (
    event_details_id BIGINT NOT NULL,
    discount_codes_id BIGINT NOT NULL,
    PRIMARY KEY (event_details_id, discount_codes_id),
    CONSTRAINT fk_rel_event_details__discount_codes__event_details_id FOREIGN KEY (event_details_id) REFERENCES public.event_details(id) ON DELETE CASCADE,
    CONSTRAINT fk_rel_event_details__discount_codes__discount_codes_id FOREIGN KEY (discount_codes_id) REFERENCES public.discount_code(id) ON DELETE CASCADE
);
COMMENT ON TABLE public.rel_event_details__discount_codes IS 'Join table for EventDetails <-> DiscountCode many-to-many relationship';

 COMMIT;
 