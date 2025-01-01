package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//static 보다 우선순위
@Controller
public class HomeController {

    @GetMapping("/") //dome의 첫번째 (localhost:8080)
    public String home() {
        return "home";
    }
}
