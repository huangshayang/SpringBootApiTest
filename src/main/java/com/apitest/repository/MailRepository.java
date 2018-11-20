package com.apitest.repository;

import com.apitest.entity.Mails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mails, Integer> {
}
