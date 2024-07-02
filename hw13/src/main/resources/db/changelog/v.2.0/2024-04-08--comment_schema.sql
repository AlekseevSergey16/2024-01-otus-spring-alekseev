--liquibase formatted sql

--changeset salekseev:2024-04-08--comment_schema
create table comments(
    id      bigserial primary key,
    text    varchar not null,
    book_id bigint  not null references books (id) on delete cascade
);
