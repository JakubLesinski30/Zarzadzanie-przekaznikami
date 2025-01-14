package Zarzadzanie.przekaznikami;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class GlownyKontroller {


    @GetMapping("/")
    public String index() {
        return "index";
    }

}