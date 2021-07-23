CREATE TABLE IF NOT EXISTS checkups(
    checkUpId BIGSERIAL PRIMARY KEY,
    datePerformed TIMESTAMP,
    workerName TEXT NOT NULL,
    price FLOAT NOT NULL,
    carId BIGSERIAL NOT NULL
);