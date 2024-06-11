--liquibase formatted sql

--changeset salekseev:2024-06-11--create_roles_table
create table roles
(
    id      bigserial primary key,
    name varchar not null
);

--changeset salekseev:2024-06-11--create_users_roles_table
create table users_roles
(
    user_id bigint references users (id),
    role_id bigint references roles (id),
    primary key (user_id, role_id)
);