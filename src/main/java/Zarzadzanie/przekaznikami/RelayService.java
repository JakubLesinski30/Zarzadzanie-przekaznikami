package Zarzadzanie.przekaznikami;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
public class RelayService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RelayService(
            @Value("${relay.server.host}") String baseUrl
    ) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    /**
     * Ustawia stan przekaźnika (1..128) na żądany state (0 lub 1).
     */
    public Map<String, Object> setRelayState(int relayNumber, int state) {
        // Budujemy URL: POST /relay/set_number?relay_number=X&state=Y
        String url = baseUrl + "/relay/set_number?relay_number=" + relayNumber + "&state=" + state;
        
        // Możemy wywołać POST bez body, bo parametry są w query string
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        return response.getBody();
    }

    /**
     * Pobiera stan wybranego przekaźnika (1..128).
     */
    public Map<String, Object> getRelayState(int relayNumber) {
        // GET /relay/state_number?relay_number=X
        String url = baseUrl + "/relay/state_number?relay_number=" + relayNumber;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }

    /**
     * Pobiera stany wszystkich przekaźników.
     */
    public Map<String, Object> getAllRelaysState() {
        String url = baseUrl + "/relay/number_state_all";

        // W tym przypadku "root" jest obiektem JSON
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return response; // response będzie zawierać klucze: "relays" (lista) i "status" (string)
    }

    /**
     * Pobiera temperatury z czujników DS18B20.
     */
    public Map<String, Object> getTemperatures() {
        // GET /temperature
        String url = baseUrl + "/temperature";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }
}