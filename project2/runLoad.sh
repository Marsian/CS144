# Yanxi Wang
# 604249838

mysql CS144 < drop.sql
mysql CS144 < create.sql

ant
#ant run
ant run-all

sort -u item.dat > nodupItem.dat
sort -u user.dat > nodupUser.dat
sort -u category.dat > nodupItemCategory.dat
sort -u bid.dat  > nodupBid.dat

mysql CS144 < load.sql

#mysql CS144 <  queries.sql

rm *.dat
