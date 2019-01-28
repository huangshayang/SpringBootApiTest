package com.apitest.repository;

import com.apitest.entity.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author huangshayang
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 通过username或者email找到user
     * @param username
     * @param email
     * @return
     */
    @Cacheable(cacheNames = "user")
    User findUserByUsernameOrEmail(String username, String email);
}
