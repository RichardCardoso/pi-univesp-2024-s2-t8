package com.univesp.pi.pji240.g8.pi_2024_s2.repository;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Servico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends CrudRepository<Servico, Long>, PagingAndSortingRepository<Servico, Long> {

    List<Servico> findAll();

    List<Servico> findByDataExclusaoIsNull();

    Optional<Servico> findByTipoServicoAndDescricao(String tipoServico, String descricao);
}
