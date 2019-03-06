package com.apitest.mapper;

import com.apitest.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {

    @Select("select * from task")
    List<Task> findAllTask();

    @Select("select * from task where id = #{id}")
    Optional<Task> findById(@Param("id") int id);

    @Select("select * from task where name = #{name}")
    Task findByName(@Param("name") String name);

    @Insert("insert into task(name, taskTime, createTime, updateTime) values(#{name}, #{taskTime}, #{createTime}, #{updateTime})")
    void save(Task task);

    @Update("update task set name=#{name}, taskTime=#{taskTime}, updateTime=#{updateTime} where id = #{id}")
    void update(Task task, @Param("id") int id);

    @Delete("delete from task where id = #{id}")
    void deleteById(@Param("id") int id);

    @Select("select * from task where id = #{id}")
    String findApiIdList(@Param("id") int id);
}
