package com.apitest.mapper;

import com.apitest.entity.Logs;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LogMapper {

    @Select("select * from logs")
    List<Logs> findAllLog();

    @Select("select * from logs where id = #{id}")
    Optional<Logs> findById(@Param("id") int id);

    @Delete("delete from logs where id = #{id}")
    void deleteById(@Param("id") int id);

    @Insert("insert into logs(request_time, response_body, response_header, code, api_id, case_name, check_boolean, error_msg) values(#{requestTime}, #{responseBody}, #{responseHeader}, #{code}, #{apiId}, #{caseName}, #{checkBoolean}, #{errorMsg})")
    void save(Logs logs);
}
