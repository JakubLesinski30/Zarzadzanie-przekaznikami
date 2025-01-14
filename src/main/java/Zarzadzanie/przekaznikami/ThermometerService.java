package Zarzadzanie.przekaznikami;


import Zarzadzanie.przekaznikami.ThermometerEntity;
import Zarzadzanie.przekaznikami.ThermometerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThermometerService {

    private final ThermometerRepository thermometerRepository;

    public ThermometerService(ThermometerRepository thermometerRepository) {
        this.thermometerRepository = thermometerRepository;
    }

    public ThermometerEntity createThermometer(ThermometerEntity thermometer) {
        // Jeżeli chcesz najpierw sprawdzić, czy dany ID już nie istnieje, możesz zrobić:
        // if (thermometerRepository.existsById(thermometer.getId())) { ... }
        return thermometerRepository.save(thermometer);
    }

    public List<ThermometerEntity> findAll() {
        return thermometerRepository.findAll();
    }

    public ThermometerEntity findById(String id) {
        Optional<ThermometerEntity> optional = thermometerRepository.findById(id);
        return optional.orElse(null);
    }

    public ThermometerEntity updateThermometer(ThermometerEntity thermometer) {
        // O ile zakładamy, że ID się nie zmienia (bo to unikat 1-Wire), 
        // to wystarczy:
        return thermometerRepository.save(thermometer);
    }

    public void deleteById(String id) {
        thermometerRepository.deleteById(id);
    }
}
