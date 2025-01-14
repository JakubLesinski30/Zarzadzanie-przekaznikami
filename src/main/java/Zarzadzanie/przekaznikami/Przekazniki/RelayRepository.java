package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RelayRepository extends JpaRepository<RelayEntity, Long> {

    // Możesz dodać metody wyszukiwania po numerze przekaźnika, np.:
    RelayEntity findByRelayNumber(Integer relayNumber);

}