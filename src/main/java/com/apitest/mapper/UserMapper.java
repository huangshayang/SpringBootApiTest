package com.apitest.mapper;

import com.apitest.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username} or email = #{email}")
    User findUserByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Insert("insert into user(username, email, password, createTime, updateTime) values(#{username}, #{email}, #{password}, #{createTime}, #{updateTime})")
    void save(User user);

    @Update("update user set username=#{username}, email=#{email}, password=#{password}， createTime=#{createTime}, updateTime=#{updateTime} where id = #{id}")
    void update(User user, @Param("id") int id);
}
