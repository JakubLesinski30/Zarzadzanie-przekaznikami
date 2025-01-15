package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class PrzekaznikiApiRaspberryController {

    @Autowired
    private PrzekaznikiApiRaspberryService relayService;

    @GetMapping("/przekazniki-z-api/ustaw")
    public String showSetRelayForm() {
        return "raspberry-api/przekazniki-z-api-ustaw-form"; 
    }

    @PostMapping("/przekazniki-z-api/ustaw")
    public String setRelay(
            @RequestParam("relayNumber") int relayNumber,
            @RequestParam("state") int state,
            Model model
    ) {
        Map<String, Object> result = relayService.setRelayState(relayNumber, state);
        model.addAttribute("relayNumber", relayNumber);
        model.addAttribute("state", state);
        model.addAttribute("result", result);
        return "raspberry-api/przekazniki-z-api-ustaw-form";
    }


    @GetMapping("/przekazniki-z-api/stan")
    public String showRelayStateForm() {
        return "raspberry-api/przekazniki-z-api-stan";
    }

    @GetMapping("/przekazniki-z-api/stan/wynik")
    public String getRelayState(
            @RequestParam("relayNumber") int relayNumber,
            Model model
    ) {
        Map<String, Object> relayState = relayService.getRelayState(relayNumber);
        model.addAttribute("relayState", relayState);
        return "raspberry-api/przekazniki-z-api-stan";
    }


    @GetMapping("/przekazniki-z-api/wszystkie")
    public String getAllRelays(Model model) {
        Map<String, Object> allRelayStates = relayService.getAllRelaysState();
        model.addAttribute("allRelayStates", allRelayStates);
        return "raspberry-api/przekazniki-z-api-wszystkie";
    }

}