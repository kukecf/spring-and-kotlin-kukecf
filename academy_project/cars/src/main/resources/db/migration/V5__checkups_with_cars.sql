CREATE TABLE IF NOT EXISTS cars(
    id BIGSERIAL PRIMARY KEY,
    ownerId BIGINT NOT NULL,
    dateAdded DATE,
    manufacturerName TEXT NOT NULL,
    modelName TEXT NOT NULL,
    productionYear INTEGER NOT NULL,
    serialNumber TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS checkups(
    id BIGSERIAL PRIMARY KEY,
    datePerformed TIMESTAMP,
    workerName TEXT NOT NULL,
    price FLOAT NOT NULL,
    carId BIGINT NOT NULL,
	carOwnerId BIGINT NOT NULL,
    carDateAdded DATE,
    carManufacturerName TEXT NOT NULL,
    carModelName TEXT NOT NULL,
    carProductionYear INTEGER NOT NULL,
    carSerialNumber TEXT UNIQUE NOT NULL
);