package Zarzadzanie.przekaznikami.Termometry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Zarzadzanie.przekaznikami.Przekazniki.RelayService;

import java.util.List;
import java.util.Map;

@Controller
public class TermometrApiRaspberryKontroller {

    @Autowired
    private RelayService relayService;

    @GetMapping("/temperatura-z-api")
    public String getTemperatureData(Model model) {
        Map<String, Object> temperatureData = relayService.getTemperatures();
        model.addAttribute("temperatureData", temperatureData);
        return "raspberry-api/temperatura-z-api";
    }
}