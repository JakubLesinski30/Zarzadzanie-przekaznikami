package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiService;
import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiTabela;


@Controller
@RequestMapping("/przekazniki")
public class PrzekaznikiController {

	 private final PrzekaznikiService relayDbService;
	    private final PrzekaznikiApiRaspberryService relayService;

	    public PrzekaznikiController(PrzekaznikiService relayDbService, PrzekaznikiApiRaspberryService relayService) {
	        this.relayDbService = relayDbService;
	        this.relayService = relayService;
	    }

    @GetMapping("/nowy")
    public String showNewRelayForm(Model model) {
        model.addAttribute("relay", new PrzekaznikiTabela());
        return "przekazniki/przekazniki-nowy-form";
    }

    @PostMapping("/nowy")
    public String createRelay(@ModelAttribute PrzekaznikiTabela relay, Model model) {
        PrzekaznikiTabela saved = relayDbService.createRelay(relay);
        model.addAttribute("savedRelay", saved);
        return "przekazniki/przekazniki-nowy-wynik";
    }

    @GetMapping("/lista")
    public String listAllRelays(Model model) {
        model.addAttribute("relays", relayDbService.findAll());
        return "przekazniki/przekazniki-lista";
    }

    @GetMapping("/zmiana-stanu")
    public String showChangeStateForm() {
        return "przekazniki/przekazniki-zmiana-stanu";
    }

    @PostMapping("/zmiana-stanu")
    public String changeRelayState(@RequestParam("relayNumber") Integer relayNumber,
                                   @RequestParam("state") Integer newState,
                                   Model model) {
        PrzekaznikiTabela updated = relayDbService.updateRelayState(relayNumber, newState);

        if (updated != null) {
            relayService.setRelayState(relayNumber, newState); 
        }

        model.addAttribute("updatedRelay", updated);
        return "przekazniki/przekazniki-zmiana-stanu";
    }
    
    @GetMapping("/usun/{id}")
    public String deleteRelay(@PathVariable("id") Long id) {
        relayDbService.deleteRelayById(id);
        return "redirect:/przekazniki/lista";
    }
}