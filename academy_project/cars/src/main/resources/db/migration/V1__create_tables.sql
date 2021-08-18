CREATE TABLE IF NOT EXISTS carmodels
(
    manufacturer
    TEXT,
    model_name
    TEXT,
    is_common
    BOOLEAN,
    PRIMARY
    KEY
(
    manufacturer,
    model_name
)
    );

CREATE TABLE IF NOT EXISTS cars
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    owner_id
    BIGINT
    NOT
    NULL,
    date_added
    DATE,
    production_year
    INTEGER
    NOT
    NULL,
    serial_number
    TEXT
    UNIQUE
    NOT
    NULL,
    info_manufacturer
    TEXT
    NOT
    NULL,
    info_model_name
    TEXT
    NOT
    NULL,
    CONSTRAINT
    fk_info
    FOREIGN
    KEY
(
    info_manufacturer,
    info_model_name
)
    REFERENCES carmodels
(
    manufacturer,
    model_name
)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS checkups
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    date_performed
    TIMESTAMP,
    worker_name
    TEXT
    NOT
    NULL,
    price
    FLOAT
    NOT
    NULL,
    car_id
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    fk_cars
    FOREIGN
    KEY
(
    car_id
)
    REFERENCES cars
(
    id
)
    ON DELETE CASCADE
    );

CREATE SEQUENCE IF NOT EXISTS CAR_SEQ;

CREATE SEQUENCE IF NOT EXISTS CHECKUP_SEQ;

