CREATE SEQUENCE user_sequence increment by 1 start 6;
CREATE TABLE bl_user_origin (id int8 NOT NULL, origin_name varchar(255) NULL, CONSTRAINT bl_user_origin_pkey PRIMARY KEY (id));
INSERT INTO bl_user_origin(id, origin_name) VALUES(1, 'BOOKLINK');
INSERT INTO bl_user_origin(id, origin_name) VALUES(2, 'FACEBOOK');
INSERT INTO bl_user_origin(id, origin_name) VALUES(3, 'GOOGLE');
ALTER TABLE bl_user ADD COLUMN origin_id int8 NULL, ADD COLUMN last_pwd_change timestamp NULL;
ALTER TABLE bl_user ADD CONSTRAINT fk_user_origin FOREIGN KEY (origin_id) REFERENCES bl_user_origin(id);
UPDATE bl_user SET origin_id = 1 WHERE origin_id is null;