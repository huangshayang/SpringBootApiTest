package com.apitest.repository;

import com.apitest.entity.Logs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface LogRepository extends JpaRepository<Logs, Integer> {

    @Transactional(rollbackFor = Exception.class)
    void deleteByApiId(Integer id);

    Page<Logs> findAllByApiId(Integer id, Pageable pageable);

}
