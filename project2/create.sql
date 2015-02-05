-- Yanxi Wang 
-- 604249838 

CREATE TABLE IF NOT EXISTS Item (
    ItemID      INT(11) NOT NULL,
    UserID      VARCHAR(100) NOT NULL,
    Name        VARCHAR(100) NOT NULL,
    Currently   DECIMAL(8,2) NOT NULL,
    BuyPrice    DECIMAL(8,2) NOT NULL,
    FirstBid    DECIMAL(8,2) NOT NULL,
    Started     TIMESTAMP NOT NULL,
    Ends        TIMESTAMP NOT NULL,
    Location    VARCHAR(100) NOT NULL,
    Latitude    FLOAT NOT NULL,
    Longitude   FlOAT NOT NULL,
    Country     VARCHAR(100) NOT NULL,
    Description VARCHAR(4000) NOT NULL,
          
    PRIMARY KEY(ItemID)
);
            
CREATE TABLE IF NOT EXISTS User (
    UserID     VARCHAR(100) NOT NULL,
    Rating     INT(11) NOT NULL,
    Location   VARCHAR(100) NOT NULL,
    Country    VARCHAR(100) NOT NULL,
                 
    PRIMARY KEY(UserID)
);

CREATE TABLE IF NOT EXISTS ItemCategory (
    ItemID   INT(11) NOT NULL,
    Category VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Bid
(
    BidID     INT(11) NOT NULL AUTO_INCREMENT,
    UserID    VARCHAR(100) NOT NULL,
    ItemID    INT(11) NOT NULL,
    Time      TIMESTAMP NOT NULL,
    Amount    DECIMAL(8,2) NOT NULL,
                          
    PRIMARY KEY(BidID)
);
