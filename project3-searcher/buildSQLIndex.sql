-- Yanxi Wang
-- 604249838

-- Create four tables for spatial search
-- Only the ItemGeo is for use
CREATE TABLE IF NOT EXISTS Country (
    country VARCHAR(100) NOT NULL,
    x float(11) NOT NULL,
    y float(11) NOT NULL,

    PRIMARY KEY(country)
);

CREATE TABLE IF NOT EXISTS Geo (
    country VARCHAR(100) NOT NULL,
    g POINT NOT NULL,

    PRIMARY KEY(country)
) ENGINE=MyISAM;

CREATE TABLE IF NOT EXISTS ItemCountry (
    ItemID int(11) NOT NULL,
    country VARCHAR(100) NOT NULL,

    PRIMARY KEY(ItemID)
);

CREATE TABLE IF NOT EXISTS ItemGeo (
    ItemID int(11) NOT NULL,
    g POINT NOT NULL,

    PRIMARY KEY(ItemID)
) ENGINE=MyISAM;

-- Load geometry information from geo.dat
LOAD DATA LOCAL INFILE 'geo.dat' INTO TABLE Country
FIELDS TERMINATED BY '#';

-- Generate ItemGeo
INSERT INTO Geo (country, g) 
    SELECT country, POINT(x, y) 
        FROM Country;

INSERT INTO ItemCountry (ItemID, country) 
    SELECT ItemID,Country 
        FROM Item INNER JOIN User 
        WHERE User.UserID=Item.UserID;

INSERT INTO ItemGeo (ItemID, g) 
    SELECT ItemID,g 
        FROM ItemCountry INNER JOIN Geo
        WHERE ItemCountry.country=Geo.country;
