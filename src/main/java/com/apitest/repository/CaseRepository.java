package com.apitest.repository;


import com.apitest.entity.Cases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CaseRepository extends JpaRepository<Cases, Integer> {

    List<Cases> findByApiId(Integer id);

    Page<Cases> findCasesByApiId(Integer id, Pageable pageable);

    @Transactional(rollbackFor = Exception.class)
    void deleteByApiId(Integer id);

}
