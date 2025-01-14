package Zarzadzanie.przekaznikami;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import Zarzadzanie.przekaznikami.Termometry.DodatkowyTermometrService;
import Zarzadzanie.przekaznikami.Termometry.TermometrTabela;
import Zarzadzanie.przekaznikami.Termometry.TermometrService;

@Service
public class SkanTempPoprawkaZapisDoBazy {

    private final TermometrService thermometerService;
    private final DodatkowyTermometrService externalTemperatureService;

    public SkanTempPoprawkaZapisDoBazy(TermometrService thermometerService,
                                DodatkowyTermometrService externalTemperatureService) {
        this.thermometerService = thermometerService;
        this.externalTemperatureService = externalTemperatureService;
    }

    @Scheduled(fixedRate = 2000)
    public void pollAndStoreMeasurements() {
        Map<String, Double> allExternalTemps = externalTemperatureService.fetchAllTemperatures();

        List<TermometrTabela> allThermometers = thermometerService.findAll();

        for (TermometrTabela therm : allThermometers) {
            if (allExternalTemps.containsKey(therm.getId())) {
                double rawTemp = allExternalTemps.get(therm.getId());
                double correctedTemp = rawTemp + therm.getOffset(); 
                
                therm.setLastMeasurement(correctedTemp);
                thermometerService.updateThermometer(therm);
            }
        }
    }
}