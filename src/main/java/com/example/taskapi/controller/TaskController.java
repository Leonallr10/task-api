package com.example.taskapi.controller;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskExecution;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.service.KubernetesService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private KubernetesService kubernetesService;

    // GET tasks. If "id" is provided, return that task; otherwise, return all tasks.
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(value = "id", required = false) String id) {
        if (id != null) {
            Optional<Task> task = taskRepository.findById(id);
            // Return OK if found; otherwise, return a 404 with no content.
            return task.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    // PUT a task. Validate the command for safety.
    @PutMapping
    public ResponseEntity<?> createOrUpdateTask(@RequestBody Task task) {
        // Allow only letters, numbers, spaces, and a limited set of punctuation.
        Pattern safePattern = Pattern.compile("^[a-zA-Z0-9_\\-\\s!@#$%^&*()+=:;,.]+$");
        if (!safePattern.matcher(task.getCommand()).matches()) {
            return ResponseEntity.badRequest().body("Unsafe command detected");
        }
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    // DELETE a task by ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted");
    }

    // GET tasks by name (search).
    @GetMapping("/search")
    public ResponseEntity<?> searchTasksByName(@RequestParam("name") String name) {
        List<Task> tasks = taskRepository.findByNameContaining(name);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found");
        }
        return ResponseEntity.ok(tasks);
    }

    // PUT a TaskExecution (by task ID). This endpoint creates a new pod to execute the command.
    @PutMapping("/{id}/executions")
    public ResponseEntity<?> executeTask(@PathVariable String id) {
        Optional<Task> optTask = taskRepository.findById(id);
        if (!optTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
        Task task = optTask.get();
        TaskExecution exec = new TaskExecution();
        exec.setStartTime(new Date());
        try {
            // Run the command inside a Kubernetes pod
            String output = kubernetesService.runCommandInPod(task.getCommand());
            exec.setOutput(output);
        } catch (Exception e) {
            exec.setOutput("Error executing command: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exec);
        }
        exec.setEndTime(new Date());

        // Add execution record to the task and save it
        task.getTaskExecutions().add(exec);
        taskRepository.save(task);
        return ResponseEntity.ok(exec);
    }
}
