package tn.esprit.ruya.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from TestController!");
        response.put("status", "success");
        return response;
    }

    @GetMapping("/check-user")
    public Map<String, Object> checkUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("email", email);
        response.put("message", "Test endpoint working!");
        response.put("usernameExists", false);
        response.put("emailExists", false);
        response.put("exists", false);
        return response;
    }
}