package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.stereotype.Service;

import Zarzadzanie.przekaznikami.Przekazniki.RelayEntity;
import Zarzadzanie.przekaznikami.Przekazniki.RelayRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RelayDbService {
    private final RelayRepository relayRepository;

    public RelayDbService(RelayRepository relayRepository) {
        this.relayRepository = relayRepository;
    }

    public RelayEntity createRelay(RelayEntity relay) {
        // Tutaj możesz dorzucić np. sprawdzenie czy dany relayNumber już istnieje
        return relayRepository.save(relay);
    }

    public RelayEntity updateRelay(RelayEntity relay) {
        // Wersja prosta: po prostu save
        return relayRepository.save(relay);
    }

    public RelayEntity updateRelayState(Integer relayNumber, Integer newState) {
        RelayEntity existing = relayRepository.findByRelayNumber(relayNumber);
        if (existing != null) {
            existing.setState(newState);
            return relayRepository.save(existing);
        }
        return null; // lub rzuć wyjątek, jeśli nie znaleziono
    }

    public List<RelayEntity> findAll() {
        return relayRepository.findAll();
    }

    public RelayEntity findById(Long id) {
        Optional<RelayEntity> optional = relayRepository.findById(id);
        return optional.orElse(null);
    }

    public RelayEntity findByRelayNumber(Integer relayNumber) {
        return relayRepository.findByRelayNumber(relayNumber);
    }
    
    public void deleteRelayById(Long id) {
        relayRepository.deleteById(id);
    }

}