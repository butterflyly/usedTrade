package com.example.usedTrade;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
// @ResponseBody
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("msg", "Gradle + JSP 세팅 성공!");
        return "redirect:/item-posts/1";
    }

    @GetMapping("/item-posts")
    public String redirect()
    {
        return "redirect:/item-posts/1";
    }

    @GetMapping("/admin/report")
    public String redirec()
    {
        return "redirect:/admin/report/SCAM";
    }
}