package com.ternurines.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/adoptar")
    public String adoptar() {
        return "adoptar";
    }

    @GetMapping("/ag-cita-usuario")
    public String agCitaUsuario() {
        return "ag_cita_usuario";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @GetMapping("/booking")
    public String booking() {
        return "booking";
    }

    @GetMapping("/catalogo")
    public String catalogo() {
        return "catalogo";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/price")
    public String price() {
        return "price";
    }

    @GetMapping("/service")
    public String service() {
        return "service";
    }

    @GetMapping("/single")
    public String single() {
        return "single";
    }

    @GetMapping("/usuario")
    public String usuario() {
        return "usuario";
    }
}