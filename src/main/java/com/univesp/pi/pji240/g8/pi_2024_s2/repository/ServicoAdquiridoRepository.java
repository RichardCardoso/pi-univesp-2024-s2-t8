package com.univesp.pi.pji240.g8.pi_2024_s2.repository;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.ServicoAdquirido;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoAdquiridoRepository extends CrudRepository<ServicoAdquirido, Long>, PagingAndSortingRepository<ServicoAdquirido, Long> {

    List<ServicoAdquirido> findAll();

    List<ServicoAdquirido> findByDataExclusaoIsNull();

    Optional<ServicoAdquirido> findByIdAndDataExclusaoIsNull(Long id);
}
