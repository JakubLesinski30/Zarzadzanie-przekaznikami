package Zarzadzanie.przekaznikami;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiService;
import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiEntity;
import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiApiRaspberryService;

import java.util.List;

@Component
public class InicjalizacjaPrzekaznikow implements CommandLineRunner {

    private final PrzekaznikiService relayDbService;
    private final PrzekaznikiApiRaspberryService relayService;

    public InicjalizacjaPrzekaznikow(PrzekaznikiService relayDbService, PrzekaznikiApiRaspberryService relayService) {
        this.relayDbService = relayDbService;
        this.relayService = relayService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<PrzekaznikiEntity> allRelays = relayDbService.findAll();

        for (PrzekaznikiEntity relay : allRelays) {
            int relayNumber = relay.getRelayNumber();
            int state = relay.getState();

            relayService.setRelayState(relayNumber, state);

            System.out.println("Ustawiono na starcie relayNumber=" + relayNumber + " na stan= " + state);
        }
    }
}