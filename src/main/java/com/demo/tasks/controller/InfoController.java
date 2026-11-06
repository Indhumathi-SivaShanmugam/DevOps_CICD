package com.demo.tasks.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {

    @Value("${app.version:1.0.0}")
    private String version;

    @GetMapping("/api/info")
    public Map<String, String> info() {
        String pod = System.getenv().getOrDefault("HOSTNAME", "local");
        return Map.of("app", "task-tracker", "version", version, "pod", pod);
    }
}
