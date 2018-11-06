package com.apitest.repository;


import com.apitest.entity.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Vector;


public interface CaseRepository extends JpaRepository<Cases, Integer> {

    Vector<Cases> findByApiId(Integer id);

    @Transactional(rollbackFor = Exception.class)
    void deleteByApiId(Integer id);

}
