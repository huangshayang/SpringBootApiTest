package com.apitest.mapper;

import com.apitest.entity.Apis;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ApiMapper {

    @Select("select * from apis")
    List<Apis> findAllApi();

    @Select("select * from apis where id = #{id}")
    Optional<Apis> findById(@Param("id") int id);

    @Insert("insert into apis(url, method, env_id, cookie, name, create_time, update_time) values(#{url}, #{method}, #{envId}, #{cookie}, #{name}, #{createTime}, #{updateTime})")
    void save(Apis apis);

    @Update("update apis set url=#{apis.url}, method=#{apis.method}, env_id=#{apis.envId}, cookie=#{apis.cookie}, name=#{apis.name}, update_time=#{apis.updateTime} where id = #{id}")
    void update(Apis apis, @Param("id") int id);

    @Delete("delete from apis where id = #{id}")
    void deleteById(@Param("id") int id);

    @Select("select count(*) from apis where url = #{url} and env_id = #{envId} and method = #{method}")
    @ResultType(boolean.class)
    boolean existsByEnvIdAndUrlAndMethod(@Param("url") String url, @Param("method") String method, @Param("envId") int envId);

    @Select("select * from apis where url = #{url} and method = #{method} and env_id = #{envId}")
    Apis findByUrlAndMethodAndEnvId(@Param("url") String url, @Param("method") String method, @Param("envId") int envId);

    @Select("select * from apis where id in (${ids})")
    List<Apis> findApiByIds(@Param("ids") String ids);
}
