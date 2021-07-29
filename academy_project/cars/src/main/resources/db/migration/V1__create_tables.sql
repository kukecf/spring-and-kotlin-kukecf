CREATE TABLE IF NOT EXISTS carmodels(
    manufacturer_name TEXT,
    model_name TEXT,
    is_common BOOLEAN,
    PRIMARY KEY (manufacturer_name,model_name)
);

CREATE TABLE IF NOT EXISTS cars(
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    date_added DATE,
    production_year INTEGER NOT NULL,
    serial_number TEXT UNIQUE NOT NULL,
    manufacturer_name TEXT NOT NULL,
    model_name TEXT NOT NULL,
    CONSTRAINT fk_info
            FOREIGN KEY(manufacturer_name,model_name)
                REFERENCES carmodels(manufacturer_name,model_name)
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

