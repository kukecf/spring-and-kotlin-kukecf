CREATE TABLE IF NOT EXISTS checkups(
    checkUpId BIGSERIAL PRIMARY KEY,
    datePerformed TIMESTAMP,
    workerName TEXT,
    price FLOAT,
    carId BIGSERIAL
);

