package Zarzadzanie.przekaznikami.Przekazniki;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrzekaznikiRepository extends JpaRepository<PrzekaznikiTabela, Long> {

    PrzekaznikiTabela findByRelayNumber(Integer relayNumber);

}