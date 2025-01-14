package Zarzadzanie.przekaznikami;

import Zarzadzanie.przekaznikami.RelayEntity;
import Zarzadzanie.przekaznikami.RelayDbService;
import Zarzadzanie.przekaznikami.RelayService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Klasa, która po starcie aplikacji ustawia fizyczne przekaźniki 
 * w stanach zgodnych z bazą danych.
 */
@Component
public class RelayStartupInitializer implements CommandLineRunner {

    private final RelayDbService relayDbService;
    private final RelayService relayService;

    public RelayStartupInitializer(RelayDbService relayDbService, RelayService relayService) {
        this.relayDbService = relayDbService;
        this.relayService = relayService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Pobierz wszystkie przekaźniki z bazy
        List<RelayEntity> allRelays = relayDbService.findAll();

        // 2. Dla każdego ustaw fizycznie stan przez API
        for (RelayEntity relay : allRelays) {
            int relayNumber = relay.getRelayNumber();
            int state = relay.getState();

            // Wywołanie zewnętrznego API np. 192.168.1.99:5000/relay/set_number
            relayService.setRelayState(relayNumber, state);

            // Można dodać logowanie, np.:
            System.out.println("Ustawiono relayNumber=" + relayNumber + " na stan=" + state);
        }
    }
}