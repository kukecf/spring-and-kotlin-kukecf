CREATE TABLE IF NOT EXISTS cars(
    carId BIGSERIAL PRIMARY KEY,
    ownerId BIGSERIAL,
    dateAdded DATE,
    manufacturerName TEXT,
    modelName TEXT,
    productionYear INTEGER,
    serialNumber TEXT UNIQUE
);
