package com.example.taskapi.repository;

import com.example.taskapi.model.Task;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByNameContaining(String name);
}
