local key = "auc:pw:error:" .. KEYS[1]
local limit = tonumber(ARGV[1])
local expire_time = ARGV[2]

local is_exists = redis.call("EXISTS", key)
if is_exists == 1 then
    if redis.call("INCR", key) >= limit then
        -- 如果超过最大次数,每次都更新失效时间,增强限制
        redis.call("EXPIRE", key, expire_time)
        return 0
    else
        return 1
    end
else
    redis.call("SET", key, 1)
    redis.call("EXPIRE", key, expire_time)
    return 1
end