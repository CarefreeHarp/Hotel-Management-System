package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // Full URL: http://localhost:8080/, http://localhost:8080/index, http://localhost:8080/home
    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "landing-page";
    }

    // Full URL: http://localhost:8080/suites
    @GetMapping("/suites")
    public String suites() {
        return "redirect:/#rooms-section";
    }

    // Full URL: http://localhost:8080/book-now
    @GetMapping("/book-now")
    public String bookNow() {
        return "redirect:/#book-section";
    }

    // Full URL: http://localhost:8080/experiences
    @GetMapping("/experiences")
    public String experiences() {
        return "redirect:/#experiences-section";
    }

    // Full URL: http://localhost:8080/getting-here
    @GetMapping("/getting-here")
    public String gettingHere() {
        return "redirect:/#book-section";
    }

    // Full URL: http://localhost:8080/explore
    @GetMapping("/explore")
    public String explore() {
        return "redirect:/#video-section";
    }

    // Full URL: http://localhost:8080/restaurant
    @GetMapping("/restaurant")
    public String restaurant() {
        return "redirect:/#services-title";
    }

    // Full URL: http://localhost:8080/awards
    @GetMapping("/awards")
    public String awards() {
        return "redirect:/#hero";
    }
}
