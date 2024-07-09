-- liquibase formatted sql

-- changeset antipin:1720459574992-1
CREATE SEQUENCE IF NOT EXISTS author_seq START WITH 1 INCREMENT BY 1;

-- changeset antipin:1720459574992-2
CREATE SEQUENCE IF NOT EXISTS book_seq START WITH 1 INCREMENT BY 1;

-- changeset antipin:1720459574992-3
CREATE TABLE authors
(
    id        BIGINT NOT NULL DEFAULT nextval('author_seq'),
    firstname VARCHAR(255),
    surname   VARCHAR(255),
    CONSTRAINT pk_authors PRIMARY KEY (id)
);

-- changeset antipin:1720459574992-4
CREATE TABLE authors_books
(
    author_id BIGINT NOT NULL,
    book_id   BIGINT NOT NULL,
    CONSTRAINT pk_authors_books PRIMARY KEY (author_id, book_id)
);

-- changeset antipin:1720459574992-5
CREATE TABLE books
(
    id    BIGINT NOT NULL DEFAULT nextval('book_seq'),
    title VARCHAR(255),
    publication_year  INTEGER,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

-- changeset antipin:1720459574992-6
ALTER TABLE authors_books
    ADD CONSTRAINT fk_autboo_on_author FOREIGN KEY (author_id) REFERENCES authors (id);

-- changeset antipin:1720459574992-7
ALTER TABLE authors_books
    ADD CONSTRAINT fk_autboo_on_book FOREIGN KEY (book_id) REFERENCES books (id);

-- changeset antipin:1720459574992-8
INSERT INTO authors (id, firstname, surname)
VALUES (1, 'name1', 'surname1'),
       (2, 'name2', 'surname2');

INSERT INTO books (id, title, publication_year)
VALUES (1, 'Book Title 1', 2020),
       (2, 'Book Title 2', 2021),
       (3, 'Book Title 3', 2019),
       (4, 'Book Title 4', 2022),
       (5, 'Book Title 5', 2024);

INSERT INTO authors_books (author_id, book_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (1, 5),
       (2, 5);

