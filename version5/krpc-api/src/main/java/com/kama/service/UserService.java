package com.kama.service;


import com.kama.pojo.User;

/**
 * @InterfaceName UserService
 * @Description 接口
 * @Author Tong
 * @LastChangeDate 2024-12-05 0:52
 * @Version v1.0
 */

public interface UserService {
    // 查询
    User getUserByUserId(Integer id);
    // 新增
    Integer insertUserId(User user);
}
