package com.helpconnect.minhaListaDeCompras.controller;

import java.util.List;
import java.util.Optional;

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

import com.helpconnect.minhaListaDeCompras.model.Usuario;
import com.helpconnect.minhaListaDeCompras.model.UsuarioLogin;
import com.helpconnect.minhaListaDeCompras.repository.UsuarioRepository;
import com.helpconnect.minhaListaDeCompras.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<List<Usuario>> findAllUsuarios(){
		
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> findByIdUsuario(@PathVariable long id){
		
		return repository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> Autentication(@RequestBody Optional<UsuarioLogin> user) {
		return usuarioService.Logar(user).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> Post(@RequestBody Usuario usuario) {
		Optional<Usuario> user = usuarioService.CadastrarUsuario(usuario);
		
		try {
			return ResponseEntity.ok(user.get());
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
			
		}
		
	}
	
	@PutMapping
	public ResponseEntity<Usuario> putUsuario(@RequestBody Usuario usuario){
		
		return ResponseEntity.ok(repository.save(usuario));
	}
	
	@DeleteMapping("/{id}")
	public void deletarUsuario(@PathVariable long id) {
		
		repository.deleteById(id);
	}

}
