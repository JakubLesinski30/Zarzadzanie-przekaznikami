package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class PrzekaznikiApiRaspberryService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PrzekaznikiApiRaspberryService(
            @Value("${relay.server.host}") String baseUrl
    ) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> setRelayState(int relayNumber, int state) {
        String url = baseUrl + "/relay/set_number?relay_number=" + relayNumber + "&state=" + state;
        
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        return response.getBody();
    }

    public Map<String, Object> getRelayState(int relayNumber) {
        String url = baseUrl + "/relay/state_number?relay_number=" + relayNumber;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }

    public Map<String, Object> getAllRelaysState() {
        String url = baseUrl + "/relay/number_state_all";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return response;
    }

    public Map<String, Object> getTemperatures() {
        String url = baseUrl + "/temperature";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }
    
    public Map<String, Object> getTemperaturesVirtual() {
        String url = baseUrl + "/temperature/virtual";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }
    
    public Map<String, Object> getTemperaturesAll() {
        String url = baseUrl + "/temperature-all";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }
}