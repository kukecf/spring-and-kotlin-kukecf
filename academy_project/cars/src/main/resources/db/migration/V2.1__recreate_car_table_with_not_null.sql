CREATE TABLE IF NOT EXISTS cars(
    carId BIGSERIAL PRIMARY KEY,
    ownerId BIGSERIAL NOT NULL,
    dateAdded DATE,
    manufacturerName TEXT NOT NULL,
    modelName TEXT NOT NULL,
    productionYear INTEGER NOT NULL,
    serialNumber TEXT UNIQUE NOT NULL
);