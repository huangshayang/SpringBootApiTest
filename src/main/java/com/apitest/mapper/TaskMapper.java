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

    @Insert("insert into task(name, task_time, api_id_list, create_time, update_time) values(#{name}, #{taskTime}, #{apiIdList}, #{createTime}, #{updateTime})")
    void save(Task task);

    @Update("update task set name=#{task.name}, api_id_list=#{task.apiIdList}, task_time=#{task.taskTime}, update_time=#{task.updateTime} where id = #{id}")
    void update(Task task, @Param("id") int id);

    @Delete("delete from task where id = #{id}")
    void deleteById(@Param("id") int id);
}
