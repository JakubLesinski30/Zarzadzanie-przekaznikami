package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrzekaznikiRepository extends JpaRepository<PrzekaznikiEntity, Long> {

    PrzekaznikiEntity findByRelayNumber(Integer relayNumber);

}