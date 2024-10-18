package com.univesp.pi.pji240.g8.pi_2024_s2.repository;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Seguradora;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguradoraRepository extends CrudRepository<Seguradora, Long>, PagingAndSortingRepository<Seguradora, Long> {

    List<Seguradora> findAll();

    List<Seguradora> findByDataExclusaoIsNull();

    Optional<Seguradora> findByCnpj(String cnpj);
}
