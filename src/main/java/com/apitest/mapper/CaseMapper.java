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

    @Select("select * from cases where apiId = #{apiId}")
    List<Cases> findByApiId(@Param("apiId") int apiId);

    @Insert("insert into cases(jsonData, paramsData, apiId, name, available, createTime, updateTime) values(#{jsonData}, #{paramsData}, #{expectResult}, #{apiId}, #{name}, #{apiId}, #{available}, #{createTime}, #{updateTime})")
    void save(Cases cases);

    @Update("update cases set jsonData=#{jsonData}, paramsData=#{paramsData}, apiId=#{apiId}, name=#{name}, available=#{available}, createTime=#{createTime}, updateTime=#{updateTime} where id = #{id}")
    void update(Cases cases, @Param("id") int id);

    @Delete("delete from cases where id = #{id}")
    void deleteById(@Param("id") int id);
}
