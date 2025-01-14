package Zarzadzanie.przekaznikami.Termometry;

import org.springframework.data.jpa.repository.JpaRepository;

import Zarzadzanie.przekaznikami.Termometry.TermometrTabela;

public interface TermometrRepository extends JpaRepository<TermometrTabela, String> {

}