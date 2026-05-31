package com.ternurines.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VistaController {
    @GetMapping("/{pagina}.html")
    public String renderHtmlPage(@PathVariable String pagina) {
        return pagina;
    }
}