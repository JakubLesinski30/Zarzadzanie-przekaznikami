package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.stereotype.Service;

import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiTabela;
import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PrzekaznikiService {
    private final PrzekaznikiRepository relayRepository;

    public PrzekaznikiService(PrzekaznikiRepository relayRepository) {
        this.relayRepository = relayRepository;
    }

    public PrzekaznikiTabela createRelay(PrzekaznikiTabela relay) {
        return relayRepository.save(relay);
    }

    public PrzekaznikiTabela updateRelay(PrzekaznikiTabela relay) {
        return relayRepository.save(relay);
    }

    public PrzekaznikiTabela updateRelayState(Integer relayNumber, Integer newState) {
        PrzekaznikiTabela existing = relayRepository.findByRelayNumber(relayNumber);
        if (existing != null) {
            existing.setState(newState);
            return relayRepository.save(existing);
        }
        return null;
    }

    public List<PrzekaznikiTabela> findAll() {
        return relayRepository.findAll();
    }

    public PrzekaznikiTabela findById(Long id) {
        Optional<PrzekaznikiTabela> optional = relayRepository.findById(id);
        return optional.orElse(null);
    }

    public PrzekaznikiTabela findByRelayNumber(Integer relayNumber) {
        return relayRepository.findByRelayNumber(relayNumber);
    }
    
    public void deleteRelayById(Long id) {
        relayRepository.deleteById(id);
    }

}