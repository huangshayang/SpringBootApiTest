package com.apitest.repository;

import com.apitest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author huangshayang
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 通过username找到user
     * @param username
     * @return
     */
    User findByUsername(Object username);

    /**
     * 通过
     * @param username
     * @return
     */
    boolean existsByUsername(Object username);
}
