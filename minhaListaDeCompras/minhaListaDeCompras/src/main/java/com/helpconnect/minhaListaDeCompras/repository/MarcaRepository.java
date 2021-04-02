package com.helpconnect.minhaListaDeCompras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpconnect.minhaListaDeCompras.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long>{
	
	public List<Marca> findAllByNomeContainingIgnoreCase(String nome);
	
}
