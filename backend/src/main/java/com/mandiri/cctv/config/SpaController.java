package com.mandiri.cctv.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Forwards all non-API, non-file requests to index.html so Angular's
 * client-side router can handle them (deep-link support).
 */
@Controller
public class SpaController {

    // Matches single-segment paths without a dot (e.g. /incident-monitoring)
    @GetMapping("/{path:[^\\.]*}")
    public String spa() {
        return "forward:/index.html";
    }

    // Matches multi-segment paths without a dot in the final segment (e.g. /admin/users)
    @GetMapping("/{path:[^\\.]*}/**")
    public String spaDeep() {
        return "forward:/index.html";
    }
}
