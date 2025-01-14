package Zarzadzanie.przekaznikami.Termometry;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DodatkowyTermometrService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public DodatkowyTermometrService(@Value("${relay.server.host}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl; 
    }

    public Map<String, Double> fetchAllTemperatures() {
        String url = baseUrl + "/temperature";
        // Zakładamy format:
        // {
        //   "sensors": [
        //       { "id": "28-0000001", "temperature": 15.0 },
        //       { "id": "28-0000002", "temperature": 14.5 }
        //   ]
        // }
        Map response = restTemplate.getForObject(url, Map.class);
        if (response == null) {
            return Collections.emptyMap();
        }

        List<Map<String, Object>> sensors = (List<Map<String, Object>>) response.get("sensors");
        if (sensors == null) {
            return Collections.emptyMap();
        }

        Map<String, Double> result = new HashMap<>();
        for (Map<String, Object> sensor : sensors) {
            String sensorId = (String) sensor.get("id");
            Double temp = ((Number) sensor.get("temperature")).doubleValue();
            result.put(sensorId, temp);
        }
        return result;
    }
}