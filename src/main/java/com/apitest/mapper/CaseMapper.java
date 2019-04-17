package com.apitest.mapper;

import com.apitest.entity.Cases;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CaseMapper {

    @Select("select * from cases")
    List<Cases> findAllCase();

    @Select("select * from cases where id = #{id}")
    Optional<Cases> findById(@Param("id") int id);

    @Select("select * from cases where api_id = #{apiId}")
    List<Cases> findByApiId(@Param("apiId") int apiId);

    @Insert("insert into cases(json_data, params_data, api_id, name, available, expect_result, create_time, update_time) values(#{jsonData}, #{paramsData}, #{apiId}, #{name}, #{available}, #{expectResult}, #{createTime}, #{updateTime})")
    void save(Cases cases);

    @Update("update cases set json_data=#{cases.jsonData}, params_data=#{cases.paramsData}, api_id=#{cases.apiId}, name=#{cases.name}, available=#{cases.available}, expect_result=#{cases.expectResult}, update_time=#{cases.updateTime} where id = #{id}")
    void update(Cases cases, @Param("id") int id);

    @Delete("delete from cases where id = #{id}")
    void deleteById(@Param("id") int id);

    @Select("select * from cases where name = #{name} and api_id = #{apiId}")
    Cases findByNameAndApiId(@Param("name") String name, @Param("apiId") int apiId);

    @Select("select count(*) from cases where name = #{name} and api_id = #{apiId}")
    @ResultType(boolean.class)
    boolean existsByNameAndApiId(@Param("name") String name, @Param("apiId") int apiId);
}
