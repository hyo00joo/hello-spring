package hello.hello_spring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "Hello!!!");
        return "hello";
    }
    @GetMapping("hello-mvc")
    public String helloM(@RequestParam("name") String s, Model model) {
        model.addAttribute("n", s);
        return "hello-template";
    }
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String s) {
        return "hello " + s;
    }
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String s) {
        Hello hello = new Hello();
        hello.setName(s);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name + "33";
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
