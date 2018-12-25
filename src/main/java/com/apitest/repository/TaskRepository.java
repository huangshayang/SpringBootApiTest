package com.apitest.repository;

import com.apitest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findByName(String name);
}
