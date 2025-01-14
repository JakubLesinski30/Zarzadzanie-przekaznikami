package Zarzadzanie.przekaznikami;

import Zarzadzanie.przekaznikami.ThermometerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThermometerRepository extends JpaRepository<ThermometerEntity, String> {

    // Możesz dodać metody wyszukiwania po "location" albo "name" jeśli potrzebujesz
    // List<ThermometerEntity> findByLocation(String location);

}