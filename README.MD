The goal of this library is to too create a time serie database that can handle

This library must : 
 - Be fast (millions of record in less than a 1.000s and 100+ requests per second more than 99% of time
 - Be cached (the cache must be adaptive regarding of wich database is used the most)
 - Time sorted
 - Use mapped byte buffer
 - Memory effiiciant
 - Only structure can be stored (object with a fixed byte size)