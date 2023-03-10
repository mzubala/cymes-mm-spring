CREATE TABLE IF NOT EXISTS "user"
(
    id
    int
    primary
    key
    generated
    by
    default as
    identity,
    email
    varchar
(
    255
),
    encoded_password varchar
(
    255
),
    first_name varchar
(
    255
),
    last_name varchar
(
    255
)
    );

CREATE TABLE IF NOT EXISTS "user_roles"
(
    user_id
    int
    not
    null,
    roles
    varchar
(
    255
) not null,
    primary key
(
    user_id,
    roles
),
    FOREIGN KEY
(
    user_id
) REFERENCES "user"
(
    id
)
    );

DO
'
declare
    no_users boolean;
begin
    no_users = (SELECT count(*) = 0 FROM "user");
    if no_users
    then
        INSERT INTO "user" (id, email, encoded_password, first_name, last_name)
        VALUES (1, ''admin@admin.com'', ''$2a$12$8ae2jcds5hpsK/pB3rQ3J.iUXF4NGp3UXmdA8DcYrmYXd9.8MAHnq'', ''Admin'', ''Admin'');

        INSERT INTO user_roles VALUES (1, ''ADMIN'');
   end if;
end;
';