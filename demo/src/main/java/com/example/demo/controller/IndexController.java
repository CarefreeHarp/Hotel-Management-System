package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "index";
    }

    @GetMapping("/suites")
    public String suites() {
        return "redirect:/#rooms-section";
    }

    @GetMapping("/book-now")
    public String bookNow() {
        return "redirect:/#book-section";
    }

    @GetMapping("/experiences")
    public String experiences() {
        return "redirect:/#experiences-section";
    }

    @GetMapping("/getting-here")
    public String gettingHere() {
        return "redirect:/#book-section";
    }

    @GetMapping("/explore")
    public String explore() {
        return "redirect:/#video-section";
    }

    @GetMapping("/restaurant")
    public String restaurant() {
        return "redirect:/#services-title";
    }

    @GetMapping("/awards")
    public String awards() {
        return "redirect:/#hero";
    }
}

