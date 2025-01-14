package Zarzadzanie.przekaznikami;

import Zarzadzanie.przekaznikami.RelayEntity;
import Zarzadzanie.przekaznikami.RelayDbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/db/relay")
public class RelayDbController {

	 private final RelayDbService relayDbService;
	    private final RelayService relayService; // <-- serwis do komunikacji z API

	    public RelayDbController(RelayDbService relayDbService, RelayService relayService) {
	        this.relayDbService = relayDbService;
	        this.relayService = relayService;
	    }

    /**
     * Formularz do dodawania nowego przekaźnika do bazy
     */
    @GetMapping("/new")
    public String showNewRelayForm(Model model) {
        model.addAttribute("relay", new RelayEntity());
        return "relay_new_form"; // thymeleaf template
    }

    /**
     * Obsługa zapisu nowego przekaźnika
     */
    @PostMapping("/new")
    public String createRelay(@ModelAttribute RelayEntity relay, Model model) {
        // relay ma wypełnione pola z formularza: relayNumber, name, description, state, location
        RelayEntity saved = relayDbService.createRelay(relay);
        model.addAttribute("savedRelay", saved);
        return "relay_new_result";
    }

    /**
     * Wyświetlenie listy wszystkich przekaźników
     */
    @GetMapping("/list")
    public String listAllRelays(Model model) {
        model.addAttribute("relays", relayDbService.findAll());
        return "relay_list";
    }

    /**
     * Prosty formularz do zmiany stanu w bazie
     */
    @GetMapping("/change_state")
    public String showChangeStateForm() {
        return "relay_change_state";
    }

    /**
     * Aktualizacja stanu przekaźnika w bazie
     */
    @PostMapping("/change_state")
    public String changeRelayState(@RequestParam("relayNumber") Integer relayNumber,
                                   @RequestParam("state") Integer newState,
                                   Model model) {
        // 1. Zaktualizuj stan w bazie
        RelayEntity updated = relayDbService.updateRelayState(relayNumber, newState);

        // 2. Jeśli się udało, wyślij zapytanie do API (POST)
        if (updated != null) {
            relayService.setRelayState(relayNumber, newState); 
            // w razie potrzeby obsłuż ewentualny wyjątek np. w razie braku komunikacji
        }

        model.addAttribute("updatedRelay", updated);
        return "relay_change_state"; // wracamy na ten sam widok
    }
    
    @GetMapping("/delete/{id}")
    public String deleteRelay(@PathVariable("id") Long id) {
        relayDbService.deleteRelayById(id);
        // po usunięciu przekieruj np. na listę
        return "redirect:/db/relay/list";
    }
}