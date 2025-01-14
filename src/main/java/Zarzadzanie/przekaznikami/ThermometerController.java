package Zarzadzanie.przekaznikami;

import Zarzadzanie.przekaznikami.ThermometerEntity;
import Zarzadzanie.przekaznikami.ThermometerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/db/thermometers")
public class ThermometerController {

    private final ThermometerService thermometerService;

    public ThermometerController(ThermometerService thermometerService) {
        this.thermometerService = thermometerService;
    }

    /**
     * Wyświetla listę wszystkich termometrów w bazie
     */
    @GetMapping("/list")
    public String listAll(Model model) {
        model.addAttribute("thermometers", thermometerService.findAll());
        return "thermometer_list";
    }

    /**
     * Formularz do dodawania nowego termometru
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("thermometer", new ThermometerEntity());
        return "thermometer_new_form";
    }

    /**
     * Zapis nowego termometru w bazie
     */
    @PostMapping("/new")
    public String createThermometer(@ModelAttribute ThermometerEntity thermometer, Model model) {
        ThermometerEntity saved = thermometerService.createThermometer(thermometer);
        model.addAttribute("savedThermometer", saved);
        return "thermometer_new_result";
    }

    /**
     * Formularz do edycji istniejącego termometru
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        ThermometerEntity thermometer = thermometerService.findById(id);
        if (thermometer == null) {
            // Obsłuż błąd (np. przekierowanie albo 404)
            return "redirect:/db/thermometers/list";
        }
        model.addAttribute("thermometer", thermometer);
        return "thermometer_edit_form";
    }

    /**
     * Aktualizacja danych termometru w bazie
     */
    @PostMapping("/edit")
    public String updateThermometer(@ModelAttribute ThermometerEntity thermometer) {
        // thermometer już ma wypełnione ID (z hidden input w formularzu), 
        // name, description, location
        thermometerService.updateThermometer(thermometer);
        return "redirect:/db/thermometers/list";
    }

    /**
     * Usuwanie termometru z bazy
     */
    @GetMapping("/delete/{id}")
    public String deleteThermometer(@PathVariable("id") String id) {
        thermometerService.deleteById(id);
        return "redirect:/db/thermometers/list";
    }
}