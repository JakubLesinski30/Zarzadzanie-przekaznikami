package Zarzadzanie.przekaznikami;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MeasurementScheduler {

    private final ThermometerService thermometerService;
    private final ExternalTemperatureService externalTemperatureService;

    public MeasurementScheduler(ThermometerService thermometerService,
                                ExternalTemperatureService externalTemperatureService) {
        this.thermometerService = thermometerService;
        this.externalTemperatureService = externalTemperatureService;
    }

    // Co 2 sekundy
    @Scheduled(fixedRate = 2000)
    public void pollAndStoreMeasurements() {
        // 1. Pobierz wszystkie odczyty z zewn. API (mapa: sensorId -> surowa temp)
        Map<String, Double> allExternalTemps = externalTemperatureService.fetchAllTemperatures();

        // 2. Przejrzyj wszystkie termometry z bazy
        List<ThermometerEntity> allThermometers = thermometerService.findAll();

        for (ThermometerEntity therm : allThermometers) {
            // Sprawdź, czy API dało nam temperaturę dla tego ID
            if (allExternalTemps.containsKey(therm.getId())) {
                double rawTemp = allExternalTemps.get(therm.getId());
                double correctedTemp = rawTemp + therm.getOffset(); 
                // ewentualnie minus, w zależności od interpretacji
                
                // 3. Zapisz skorygowaną temperaturę w bazie
                //    np. w polu lastMeasurement w encji
                therm.setLastMeasurement(correctedTemp);
                thermometerService.updateThermometer(therm);
            }
        }
    }
}