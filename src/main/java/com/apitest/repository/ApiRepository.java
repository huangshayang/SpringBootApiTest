package com.apitest.repository;

import com.apitest.entity.Apis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<Apis, Integer> {

    /**
     * 通过url和method找到api
     * @param url
     * @param method
     * @return
     */
    boolean existsByUrlAndMethod(String url, String method);

    Apis findByUrlAndMethod(String url, String method);
}
