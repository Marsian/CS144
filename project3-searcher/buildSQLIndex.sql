-- Yanxi Wang
-- 604249838

-- Create table for spatial search
CREATE TABLE IF NOT EXISTS ItemGeo (
    ItemID int(11) NOT NULL,
    g POINT NOT NULL

) ENGINE=MyISAM;

-- Generate ItemGeo
INSERT INTO ItemGeo (ItemID, g) 
    SELECT ItemID, POINT(Latitude, Longitude) 
        FROM Item;

-- Create spatial index
CALL create_index_if_not_exists();

-- Funtion to create spatial index if not exist
DELIMITER $$

DROP PROCEDURE IF EXISTS create_index_if_not_exists $$
CREATE PROCEDURE create_index_if_not_exists()
BEGIN  
    DECLARE IndexIsThere INTEGER;
    
    SELECT COUNT(1) INTO IndexIsThere
        FROM information_schema.statistics 
        WHERE TABLE_SCHEMA = 'CS144' AND TABLE_NAME = 'ItemGeo' AND 
              INDEX_NAME = 'sp_index';
    IF IndexIsThere = 0 THEN
        CREATE SPATIAL INDEX sp_index ON ItemGeo (g);
    END IF;

END $$ 

DELIMITER ;
