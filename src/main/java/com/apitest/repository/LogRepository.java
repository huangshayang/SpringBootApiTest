package com.apitest.repository;

import com.apitest.entity.Logs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface LogRepository extends JpaRepository<Logs, Integer> {

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from Logs where apiId=?1")
    void deleteByApiId(Integer id);

    Page<Logs> findAllByApiId(Integer id, Pageable pageable);

}
