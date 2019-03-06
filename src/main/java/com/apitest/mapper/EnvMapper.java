package com.apitest.mapper;

import com.apitest.entity.Enviroment;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EnvMapper {

    @Select("select * from enviroment")
    List<Enviroment> findAllEnv();

    @Select("select * from enviroment where id = #{id}")
    Optional<Enviroment> findById(@Param("id") int id);

    @Select("select * from enviroment where name = #{name}")
    Enviroment findByName(@Param("name") String name);

    @Insert("insert into enviroment(name, username, password, domain, create_time, update_time) values(#{name}, #{username}, #{password}, #{domain}, #{createTime}, #{updateTime})")
    void save(Enviroment enviroment);

    @Update("update enviroment set name=#{name}, username=#{username}, password=#{password}, domain=#{domain}, update_time=#{updateTime} where id = #{id}")
    void update(Enviroment enviroment, @Param("id") int id);

    @Delete("delete from enviroment where id = #{id}")
    void deleteById(@Param("id") int id);
}
