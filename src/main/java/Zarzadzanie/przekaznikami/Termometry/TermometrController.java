package Zarzadzanie.przekaznikami.Termometry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Zarzadzanie.przekaznikami.Termometry.TermometrTabela;
import Zarzadzanie.przekaznikami.Termometry.TermometrService;

@Controller
@RequestMapping("termometry")
public class TermometrController {

    private final TermometrService thermometerService;

    public TermometrController(TermometrService thermometerService) {
        this.thermometerService = thermometerService;
    }

    @GetMapping("/lista")
    public String listAll(Model model) {
        model.addAttribute("thermometers", thermometerService.findAll());
        return "termometry/termometry-lista";    }

    @GetMapping("/nowy")
    public String showNewForm(Model model) {
        model.addAttribute("thermometer", new TermometrTabela());
        return "termometry/termometry-nowy-form";
    }

    @PostMapping("/nowy")
    public String createThermometer(@ModelAttribute TermometrTabela thermometer, Model model) {
        TermometrTabela saved = thermometerService.createThermometer(thermometer);
        model.addAttribute("savedThermometer", saved);
        return "termometry/termometry-nowy-wynik";    }

    @GetMapping("/edytuj/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        TermometrTabela thermometer = thermometerService.findById(id);
        if (thermometer == null) {
            return "redirect:/termometry/lista";
        }
        model.addAttribute("thermometer", thermometer);
        return "termometry/termometry-edytuj-form";    }

    @PostMapping("/edytuj")
    public String updateThermometer(@ModelAttribute TermometrTabela thermometer) {

        thermometerService.updateThermometer(thermometer);
        return "redirect:/termometry/lista";
    }

    @GetMapping("/usun/{id}")
    public String deleteThermometer(@PathVariable("id") String id) {
        thermometerService.deleteById(id);
        return "redirect:/termometry/lista";
    }
}