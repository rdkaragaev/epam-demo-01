-- liquibase formatted sql

-- changeset rdkaragaev:1
CREATE TABLE book (
    id BIGSERIAL CONSTRAINT pk_book_id PRIMARY KEY,
    name VARCHAR,
    release_date DATE
)
