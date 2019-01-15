package com.apitest.repository;


import com.apitest.entity.Cases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CaseRepository extends JpaRepository<Cases, Integer> {

    List<Cases> findByApiId(Integer id);

    Page<Cases> findCasesByApiId(Integer id, Pageable pageable);

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from Cases where apiId=?1")
    void deleteByApiId(Integer id);

}
