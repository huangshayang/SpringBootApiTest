package com.apitest.mapper;

import com.apitest.entity.Mails;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {

    @Insert("insert into mails(content, subject, createTime) values(#{content}, #{subject}, #{createTime})")
    void save(Mails mails);
}
