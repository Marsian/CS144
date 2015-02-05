#  Yanxi Wang 
#  604249838

Relations:
item(*ItemID, UserID, Name, Currently, BuyPrice, FirstBid, Started, Ends, 
     Location, Latitude, Longitude, Country, Description)
User(*UserID, Rating, Location, Country)
ItemCategory(ItemID, Category)
Bid(*BidID, UserID, ItemID, Time, Amount)

 * denotes the keys.
