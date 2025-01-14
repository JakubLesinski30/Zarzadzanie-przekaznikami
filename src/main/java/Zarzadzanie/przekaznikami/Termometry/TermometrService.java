package Zarzadzanie.przekaznikami.Termometry;


import org.springframework.stereotype.Service;

import Zarzadzanie.przekaznikami.Termometry.TermometrTabela;
import Zarzadzanie.przekaznikami.Termometry.TermometrRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TermometrService {

    private final TermometrRepository thermometerRepository;

    public TermometrService(TermometrRepository thermometerRepository) {
        this.thermometerRepository = thermometerRepository;
    }

    public TermometrTabela createThermometer(TermometrTabela thermometer) {
        // Jeżeli chcesz najpierw sprawdzić, czy dany ID już nie istnieje, możesz zrobić:
        // if (thermometerRepository.existsById(thermometer.getId())) { ... }
        return thermometerRepository.save(thermometer);
    }

    public List<TermometrTabela> findAll() {
        return thermometerRepository.findAll();
    }

    public TermometrTabela findById(String id) {
        Optional<TermometrTabela> optional = thermometerRepository.findById(id);
        return optional.orElse(null);
    }

    public TermometrTabela updateThermometer(TermometrTabela thermometer) {
        // O ile zakładamy, że ID się nie zmienia (bo to unikat 1-Wire), 
        // to wystarczy:
        return thermometerRepository.save(thermometer);
    }

    public void deleteById(String id) {
        thermometerRepository.deleteById(id);
    }
}
