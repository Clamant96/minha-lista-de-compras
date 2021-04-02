package com.helpconnect.minhaListaDeCompras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpconnect.minhaListaDeCompras.model.Marca;
import com.helpconnect.minhaListaDeCompras.repository.MarcaRepository;

@RestController
@RequestMapping("/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {
	
	@Autowired
	private MarcaRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Marca>> findAllMarcas(){
		
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Marca> findByIdMarca(@PathVariable long id){
		
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Marca>> findByNomeMarca(@PathVariable String nome){
		
		return ResponseEntity.ok(repository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@PostMapping
	public ResponseEntity<Marca> postMarca(@RequestBody Marca marca){
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(marca));
	}
	
	@PutMapping
	public ResponseEntity<Marca> putMarca(@RequestBody Marca marca){
		
		return ResponseEntity.ok(repository.save(marca));
	}
	
	@DeleteMapping("/{id}")
	public void deletarMarca(@PathVariable long id) {
		
		repository.deleteById(id);
	}

}
