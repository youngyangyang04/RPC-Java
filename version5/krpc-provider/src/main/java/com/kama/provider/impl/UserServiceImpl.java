package com.kama.provider.impl;

import com.kama.pojo.User;
import com.kama.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;

/**
 * @ClassName UserServiceImpl
 * @Description 接口实现类
 * @Author Tong
 * @LastChangeDate 2024-12-04 23:55
 * @Version v5.0
 */
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id) {
        log.info("客户端查询了ID={}的用户", id);
        // 模拟从数据库中取用户的行为
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())  // 使用随机生成的用户名
                .id(id)
                .gender(random.nextBoolean())  // 随机生成性别
                .build();
        log.info("返回用户信息: {}", user);
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        log.info("插入数据成功，用户名={}", user.getUserName());
        // 假设插入数据返回用户ID
        return user.getId();
    }
}

