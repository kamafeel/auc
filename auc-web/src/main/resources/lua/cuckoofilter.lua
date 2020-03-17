--see https://github.com/kristoff-it/redis-cuckoofilter
--redis version 4 不支持module
local cuckoo_name = ARGV[1]
local cuckoo_size = ARGV[2]
-- Load the module if you haven't done so already
redis.call("MODULE LOAD", "/lua/libredis-cuckoofilter-1.1.0.so")
-- Create a filter
redis.call("cf.init", cuckoo_name, cuckoo_size)