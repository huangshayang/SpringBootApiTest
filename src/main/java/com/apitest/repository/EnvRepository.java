package com.apitest.repository;

import com.apitest.entity.Enviroment;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author huangshayang
 */
public interface EnvRepository extends JpaRepository<Enviroment, Integer> {
    Enviroment findByName(String name);

}
