--liquibase formatted sql

--changeset salekseev:2024-06-10--create_user_table
create table users
(
    id      bigserial primary key,
    username varchar not null,
    password varchar not null
)