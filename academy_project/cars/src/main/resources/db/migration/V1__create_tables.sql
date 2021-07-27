CREATE TABLE IF NOT EXISTS cars(
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    date_added DATE,
    manufacturer_name TEXT NOT NULL,
    model_name TEXT NOT NULL,
    production_year INTEGER NOT NULL,
    serial_number TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS checkups(
    id BIGSERIAL PRIMARY KEY,
    date_performed TIMESTAMP,
    worker_name TEXT NOT NULL,
    price FLOAT NOT NULL,
    car_id BIGINT NOT NULL,
    CONSTRAINT fk_cars
        FOREIGN KEY(car_id)
            REFERENCES cars(id)
);

CREATE SEQUENCE IF NOT EXISTS CAR_SEQ;

CREATE SEQUENCE IF NOT EXISTS CHECKUP_SEQ;