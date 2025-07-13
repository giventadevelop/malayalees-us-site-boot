
CREATE ROLE giventa_event_management WITH LOGIN CREATEDB PASSWORD 'giventa_event_management';

ALTER DATABASE giventa_event_management OWNER to giventa_event_management ;

REASSIGN OWNED BY nextjs_template_boot TO giventa_event_management;

GRANT USAGE ON SCHEMA public TO giventa_event_management;

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON ALL TABLES IN SCHEMA public TO giventa_event_management;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO giventa_event_management;

-- For the table
SELECT grantee, privilege_type
FROM information_schema.role_table_grants
WHERE table_name = 'event_details';



-- For the schema
SELECT nspname, usename, has_schema_privilege(usename, nspname, 'USAGE')
FROM pg_namespace, pg_user
WHERE nspname = 'public';

SELECT datname, usename AS owner
FROM pg_database
JOIN pg_user ON pg_database.datdba = pg_user.usesysid;


DELETE FROM public.user_task WHERE user_id NOT IN (SELECT id FROM public.user_profile);

ALTER TABLE EVENT
ALTER COLUMN start_time TYPE VARCHAR(100) USING start_time::VARCHAR,
ALTER COLUMN end_time TYPE VARCHAR(100) USING end_time::VARCHAR;

ALTER TABLE EVENT_MEDIA ADD COLUMN pre_signed_url VARCHAR(400);

INSERT INTO public.event_media
(

id, title, description, event_media_type, storage_type, file_url, file_data, file_data_content_type, content_type, file_size, is_public, event_flyer, is_event_management_official_document, created_at, updated_at, event_id, uploaded_by_id, pre_signed_url)
VALUES(1756, 'xxcx', 'xxxx', 'image/jpeg', 'S3', 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/event-id/1502/hanzh_1747836718150_f0603b68.jpg', NULL, NULL, 'image/jpeg', 51594, false, false, false, '2025-05-21 14:11:58.661', '2025-05-21 14:11:58.661', 1502, NULL, 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/event-id/1502/hanzh_1747836718150_f0603b68.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250521T141158Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIATIT5HARDKCWNLQMU%2F20250521%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=edaa7bb0b8fb5bc6f27370959fa184a86daf04542b7b601d7b12abcc9b9042c7');


ALTER TABLE public.event_media 
ALTER COLUMN file_url TYPE varchar(1200),
ALTER COLUMN pre_signed_url TYPE varchar(2048);

SELECT * FROM public.event_media
WHERE tenant_id = 'tenant_demo_001'
AND event_flyer = TRUE
AND event_id = 2251
LIMIT 20 OFFSET 0;


SELECT pid, datname, usename, application_name, client_addr 
FROM pg_stat_activity 
WHERE datname = 'giventa_event_management';

CREATE DATABASE giventa_event_management;

SELECT current_database();

SELECT rolname FROM pg_roles;

REASSIGN OWNED BY postgres TO giventa_event_management;


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

-- same as above 

DROP TABLE public.event_details CASCADE;
DROP TABLE public.event_attendee CASCADE;

DROP TABLE public.event_poll  CASCADE;

DROP TABLE public.event_poll_option   CASCADE;

DROP TABLE public.event_ticket_transaction CASCADE;

DROP TABLE public.tenant_organization  CASCADE;

DROP TABLE public.user_profile   CASCADE;

 SELECT unnest(enum_range(NULL::user_role_type));
SELECT unnest(enum_range(NULL::user_status_type));

SELECT tablename, tableowner
FROM pg_tables
WHERE schemaname = 'public';


INSERT INTO public.event_details VALUES (2801, 'tenant_demo_001', 'Annual Tech Conference 2025', 'Join us for the biggest tech event of the year', 'A comprehensive conference featuring the latest in technology trends, networking opportunities, and expert speakers from around the globe.', '2025-07-15', '2025-07-17', '09:00', '17:00', 'Convention Center, Downtown', NULL, 500, 'TICKETED', true, 2, true, false, true, NULL, NULL, NULL, NULL, false, true, NULL, 2600, 2400, '2025-06-08 20:16:39.532111', '2025-06-08 20:16:39.532111', false, false, false);
--INSERT INTO public.event_details VALUES (2900, 'tenant_demo_002', 'Family Fun Day', 'Community event for all ages', 'A family-friendly event with activities for all age groups, food, games, and entertainment.', '2025-08-10', '2025-08-10', '10:00', '18:00', 'Community Park', NULL, 200, 'FREE', true, 4, true, true, true, NULL, NULL, NULL, NULL, false, true, NULL, 2700, 2450, '2025-06-08 20:16:39.532111', '2025-06-08 20:16:39.532111', false, false, false);
--INSERT INTO public.event_details VALUES (2850, 'tenant_demo_001', 'React Workshop for Beginners', 'Learn React from scratch in this hands-on workshop', 'A beginner-friendly workshop covering React fundamentals, component creation, state management, and building your first React application.', '2025-06-20', '2025-06-20', '10:00', '16:00', 'Tech Hub Building A', NULL, 30, 'FREE', true, 0, false, false, false, NULL, NULL, NULL, NULL, false, true, NULL, 2600, 2350, '2025-06-08 20:16:39.532111', '2025-06-08 20:46:43.612386', true, false, false);
--INSERT INTO public.event_details VALUES (3951, 'tenant_demo_001', 'xxxcxc', 'cxcx', 'cxcxcxc', '2025-06-22', '2025-06-22', '10:15 AM', '11:15 AM', 'xcxcx', 'xcxcxc', 2, 'free', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, false, true, NULL, NULL, 2350, '2025-06-09 01:16:21.63', '2025-06-08 21:17:06.742427', true, NULL, NULL);
I

INSERT INTO public.event_guest_pricing VALUES (3101, 'tenant_demo_001', 2800, 'ADULT', 150.00, true, '2025-06-01', '2025-07-14', 'Adult guest pricing for conference', NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-08 20:16:39.631864', '2025-06-08 20:16:39.631864');
--INSERT INTO public.event_guest_pricing VALUES (3151, 'tenant_demo_001', 2800, 'TEEN', 75.00, true, '2025-06-01', '2025-07-14', 'Teen guest pricing (13-17 years)', NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-08 20:16:39.631864', '2025-06-08 20:16:39.631864');
--INSERT INTO public.event_guest_pricing VALUES (3201, 'tenant_demo_001', 2800, 'CHILD', 25.00, true, '2025-06-01', '2025-07-14', 'Child guest pricing (5-12 years)', NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-08 20:16:39.631864', '2025-06-08 20:16:39.631864');


	INSERT INTO public.event_ticket_type (id, tenant_id, event_id, code, name, description, price, available_quantity, sold_quantity, is_active, created_at, updated_at)
	VALUES
	(1, 'tenant_demo_001', 6151, 'STD', 'Standard', 'Standard ticket for Spring Gala', 50.00, 100, 10, true, now(), now()),
	(2, 'tenant_demo_001', 6151, 'VIP', 'VIP', 'VIP ticket for Tech Conference', 200.00, 50, 5, true, now(), now()),
	(3, 'tenant_demo_001', 6151, 'RUN', 'Runner', 'Runner ticket for Charity Run', 0.00, 300, 100, true, now(), now()),
	(4, 'tenant_demo_001', 6151, 'FAM', 'Family', 'Family ticket for Picnic', 20.00, 30, 10, true, now(), now()),
	(5, 'tenant_demo_001', 6151, 'DIN', 'Dinner', 'Dinner ticket for VIP Dinner', 100.00, 20, 8, true, now(), now()),
	(6, 'tenant_demo_001', 6151, 'FEST', 'Festival', 'Festival ticket for Summer Fest', 30.00, 200, 50, true, now(), now());
	
	UPDATE public.event_details
SET start_date = '2025-04-20';

   UPDATE public.event_details SET start_date = '2025-08-01', end_date = '2025-08-01' WHERE id = 1;
   UPDATE public.event_details SET start_date = '2025-08-15', end_date = '2025-08-15' WHERE id = 2;
   UPDATE public.event_details SET start_date = '2025-08-30', end_date = '2025-08-30' WHERE id = 3;
   UPDATE public.event_details SET start_date = '2025-09-10', end_date = '2025-09-10' WHERE id = 4;
   UPDATE public.event_details SET start_date = '2025-09-20', end_date = '2025-09-20' WHERE id = 5;
   UPDATE public.event_details SET start_date = '2025-09-30', end_date = '2025-09-30' WHERE id = 6;
   
   
   UPDATE public.user_profile
		SET is_email_subscribed = true, is_email_subscription_token_used = false
		WHERE email = 'giventauser@gmail.com';
   
    UPDATE public.user_profile
		SET is_email_subscribed = false, is_email_subscription_token_used = true
		WHERE email = 'giventauser@gmail.com';
   
   UPDATE public.user_profile
		SET email_subscription_token  = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnYWluQGhvdG1haWwuY29tIn0.hQFTfyU4gHgoKhVAjXajSqvdgCiN3qEYpdSZuhXr-4_NUO69m3VeTEAMzyXceTr8WWRWpGM6qahTk2ZkAaxLzA';
   
   commit;
  
   
   
	
	
