package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class RelayController {

    @Autowired
    private RelayService relayService;

    @GetMapping("/relay/set")
    public String showSetRelayForm() {
        return "relay_set_form"; // thymeleaf template
    }

    /**
     * Odbiór danych z formularza i ustawienie stanu przekaźnika
     */
    @PostMapping("/relay/set")
    public String setRelay(
            @RequestParam("relayNumber") int relayNumber,
            @RequestParam("state") int state,
            Model model
    ) {
        Map<String, Object> result = relayService.setRelayState(relayNumber, state);
        model.addAttribute("relayNumber", relayNumber);
        model.addAttribute("state", state);
        model.addAttribute("result", result);
        return "relay_set_form"; // wracamy na ten sam widok lub inny
    }

    /**
     * Formularz do pobrania stanu wybranego przekaźnika
     */
    @GetMapping("/relay/state")
    public String showRelayStateForm() {
        return "relay_state_form";
    }

    /**
     * Pobranie stanu wybranego przekaźnika i wyświetlenie
     */
    @GetMapping("/relay/state/result")
    public String getRelayState(
            @RequestParam("relayNumber") int relayNumber,
            Model model
    ) {
        Map<String, Object> relayState = relayService.getRelayState(relayNumber);
        model.addAttribute("relayState", relayState);
        return "relay_state_form";
    }

    /**
     * Pobranie stanu wszystkich przekaźników
     */
    @GetMapping("/relay/all")
    public String getAllRelays(Model model) {
        Map<String, Object> allRelayStates = relayService.getAllRelaysState();
        // "relays" jest listą map
        model.addAttribute("allRelayStates", allRelayStates);
        return "relay_states_all";
    }

}