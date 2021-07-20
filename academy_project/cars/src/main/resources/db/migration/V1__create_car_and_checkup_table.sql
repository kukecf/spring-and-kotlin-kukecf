CREATE TABLE IF NOT EXISTS cars(
    carId BIGSERIAL PRIMARY KEY,
    ownerId BIGSERIAL,
    dateAdded: DATE,
    manufacturerName TEXT,
    modelName TEXT,
    productionYear INTEGER,
    serialNumber UNIQUE TEXT
);

CREATE TABLE IF NOT EXISTS checkups(
    checkUpId BIGSERIAL PRIMARY KEY,
    datePerformed TIMESTAMP,
    workerName TEXT,
    price FLOAT,
    carId BIGSERIAL
);