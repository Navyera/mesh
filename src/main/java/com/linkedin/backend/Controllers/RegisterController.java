package com.linkedin.backend.Controllers;

import org.springframework.web.bind.annotation.*;


@RestController
public class RegisterController {

    @RequestMapping("/api/register")
    public String greeting() {
        return "HELLO";
    }
}
