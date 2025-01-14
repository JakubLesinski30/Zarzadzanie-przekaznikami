package Zarzadzanie.przekaznikami;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import Zarzadzanie.przekaznikami.Przekazniki.RelayDbService;
import Zarzadzanie.przekaznikami.Przekazniki.RelayEntity;
import Zarzadzanie.przekaznikami.Przekazniki.RelayService;

import java.util.List;

@Component
public class InicjalizacjaPrzekaznikow implements CommandLineRunner {

    private final RelayDbService relayDbService;
    private final RelayService relayService;

    public InicjalizacjaPrzekaznikow(RelayDbService relayDbService, RelayService relayService) {
        this.relayDbService = relayDbService;
        this.relayService = relayService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<RelayEntity> allRelays = relayDbService.findAll();

        for (RelayEntity relay : allRelays) {
            int relayNumber = relay.getRelayNumber();
            int state = relay.getState();

            relayService.setRelayState(relayNumber, state);

            System.out.println("Ustawiono na starcie relayNumber=" + relayNumber + " na stan= " + state);
        }
    }
}