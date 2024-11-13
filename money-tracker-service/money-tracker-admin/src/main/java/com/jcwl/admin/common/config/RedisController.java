package com.jcwl.admin.common.config;

import com.jcwl.common.annotation.Anonymous;
import com.jcwl.common.core.domain.R;
import com.jcwl.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis查询
 *
 * @author jcwl
 * @date 2023-09-05
 */
@RestController
@RequestMapping
public class RedisController {

    @Autowired
    private RedisCache redisCache;

    @Anonymous
    @GetMapping("/redis/{key}")
    public R<Object> redis(@PathVariable String key) {
        Object cacheObject = redisCache.getCacheObject(key);
        return R.ok(cacheObject);
    }
}
