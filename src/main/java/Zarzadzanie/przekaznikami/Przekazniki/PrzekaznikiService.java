package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.stereotype.Service;

import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiEntity;
import Zarzadzanie.przekaznikami.Przekazniki.PrzekaznikiRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PrzekaznikiService {
    private final PrzekaznikiRepository relayRepository;

    public PrzekaznikiService(PrzekaznikiRepository relayRepository) {
        this.relayRepository = relayRepository;
    }

    public PrzekaznikiEntity createRelay(PrzekaznikiEntity relay) {
        return relayRepository.save(relay);
    }

    public PrzekaznikiEntity updateRelay(PrzekaznikiEntity relay) {
        return relayRepository.save(relay);
    }

    public PrzekaznikiEntity updateRelayState(Integer relayNumber, Integer newState) {
        PrzekaznikiEntity existing = relayRepository.findByRelayNumber(relayNumber);
        if (existing != null) {
            existing.setState(newState);
            return relayRepository.save(existing);
        }
        return null;
    }

    public List<PrzekaznikiEntity> findAll() {
        return relayRepository.findAll();
    }

    public PrzekaznikiEntity findById(Long id) {
        Optional<PrzekaznikiEntity> optional = relayRepository.findById(id);
        return optional.orElse(null);
    }

    public PrzekaznikiEntity findByRelayNumber(Integer relayNumber) {
        return relayRepository.findByRelayNumber(relayNumber);
    }
    
    public void deleteRelayById(Long id) {
        relayRepository.deleteById(id);
    }

}