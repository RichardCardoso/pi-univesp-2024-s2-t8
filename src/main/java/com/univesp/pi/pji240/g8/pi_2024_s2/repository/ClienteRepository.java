package com.univesp.pi.pji240.g8.pi_2024_s2.repository;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long>, PagingAndSortingRepository<Cliente, Long> {

    List<Cliente> findAll();

    List<Cliente> findByDataExclusaoIsNull();

    Optional<Cliente> findByCpf(String cpf);
}
