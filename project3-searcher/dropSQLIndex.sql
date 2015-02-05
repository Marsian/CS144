-- Yanxi Wang
-- 604249838

CALL drop_index_if_exists;
DROP TABLE IF EXISTS ItemGeo;
DROP TABLE IF EXISTS ItemCountry;
DROP TABLE IF EXISTS Geo;
DROP TABLE IF EXISTS Country;

DELIMITER $$

DROP PROCEDURE IF EXISTS drop_index_if_exists $$
CREATE PROCEDURE drop_index_if_exists()
BEGIN  
    DECLARE IndexIsThere INTEGER;
    
    SELECT COUNT(*) INTO IndexIsThere
        FROM information_schema.statistics 
        WHERE TABLE_SCHEMA = 'CS144' AND TABLE_NAME = 'ItemGeo' AND 
              INDEX_NAME = 'sp_index';
    IF IndexIsThere > 0 THEN
        DROP INDEX sp_index ON ItemGeo;
    END IF;

END $$ 

DELIMITER ;

