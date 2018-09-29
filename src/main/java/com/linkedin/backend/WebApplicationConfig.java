package com.linkedin.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO: Enable when using Spring as the app server.
 @Controller
class WebApplicationConfig {

    // Match everything without a suffix (so not a static resource)
    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/";
    }
}