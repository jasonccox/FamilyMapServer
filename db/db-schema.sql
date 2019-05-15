/* These statements describe the tables in the FamilyMapServer database */

CREATE TABLE person (
    id          VARCHAR(255) NOT NULL PRIMARY KEY,
    descendant  VARCHAR(255),   /* confused about this field - ask prof or TA */
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    gender      CHAR(1) NOT NULL,
    father      VARCHAR(255),
    mother      VARCHAR(255),
    spouse      VARCHAR(255),
    CHECK (gender IN ('f', 'm')),
    FOREIGN KEY (father) REFERENCES person(id),
    FOREIGN KEY (mother) REFERENCES person(id),
    FOREIGN KEY (spouse) REFERENCES person(id)
);

CREATE TABLE user (
    username    VARCHAR(255) NOT NULL PRIMARY KEY,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    gender      CHAR(1) NOT NULL,
    person_id   VARCHAR(255) NOT NULL,
    CHECK (gender IN ('f', 'm')),
    FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE auth_token (
    token       VARCHAR(255) NOT NULL PRIMARY KEY,
    username    VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES user(username)
);

CREATE TABLE event (
    id          VARCHAR(255) NOT NULL PRIMARY KEY,
    descendant  VARCHAR(255),   /* confused about this field - ask prof or TA */
    person_id   VARCHAR(255) NOT NULL,
    latitude    FLOAT NOT NULL,
    longitude   FLOAT  NOT NULL,
    country     VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    type        VARCHAR(255) NOT NULL,
    year        INTEGER NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person(id)
);