package com.helpconnect.minhaListaDeCompras.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpconnect.minhaListaDeCompras.model.Produto;
import com.helpconnect.minhaListaDeCompras.model.Usuario;
import com.helpconnect.minhaListaDeCompras.model.UsuarioLogin;
import com.helpconnect.minhaListaDeCompras.repository.ProdutoRepository;
import com.helpconnect.minhaListaDeCompras.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	double a = 0;
	
	/* GERENCIA O ESTOQUE SEMPRE QUE UM NOVO PRODUTO E SELECIONADO */
	public Produto gerenciarEstoque(Produto produto) {
		Optional<Produto> produtoExistente = produtoRepository.findById(produto.getId());
		
		if(produto.getUsuarios() != null) {
			Optional<Usuario> usuarioExistente = usuarioRepository.findById(produto.getUsuarios().getId());
			
			/* POR MEIO DO ID E POSSIVEL RECUPERAR O ULTIMO VALOR DIGITADO EM MEMORIA, DESSA FORMA CONSEGUIMOS SUBTRAIR O VALOR EXATO, PARA QUE NAO HAJA SOBRAS NEM ERROS DE CALCULOS */
			/* DESSA FORMA E SUBTRAIDO O VALOR EXATO DO PRECO X A ULTIMA QNT DESSE ULTIMO VALOS, ASSIM CONSEGUIFIR FICAR SOMENTE COM OS OUTROS PRODUTOS QUE NAO ESTAO SENDO ALTERADOS */
			/* APOS ESSA SUBTRACAO, O VALOR DO PRODUTO E RECALCULADO NOVAMENTE DE ACORDO COM A QUANTIDADE DE PRODUTOS E INSERIDOS NOVAMNTE DENTRO DO ORCAMENTO */
			if(produtoExistente.get().getPreco() != produto.getPreco() || produtoExistente.get().getQtdProduto() != produto.getQtdProduto()) {
				/* SUBITRAI O ANTIGO VALOR */
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() - (produtoExistente.get().getPreco() * produtoExistente.get().getQtdProduto()));
				/* SALVA O NOVO VALOR */
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + (produto.getPreco() * produto.getQtdProduto()));
				
			}else {
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + (produto.getPreco() * produto.getQtdProduto()));
				
			}
			
			/* AJUSTA O VALOR DOS ORCAMENTO SEMPRE QUE FOR DEBITADO UM DADO PARA EVITAR RESTOS */
			a = usuarioExistente.get().getOrcamento();
			a = Math.floor(a * 100) / 100;
				usuarioExistente.get().setOrcamento(a);
			
			/* CASO A QTD PRODUTOS SEJA 0, O PRODUTO ALTOMATICAMENTE E RETIRADO DA LISTA DO USUARIO */
			if(produto.getQtdProduto() == 0) {
				produto.setUsuarios(null);
				
			}
			
			/* CORRIGE UM ERRO AO SE RETIRAR TODOS OS PRODUTOS DO ORCAMENTO, FICANDO NEGATIVOS */
			/* ========== */
			/* PROVISORIO */
			/* ========== */
			if(usuarioExistente.get().getOrcamento() < 0) {
				usuarioExistente.get().setOrcamento(0);
				
			}
			
			usuarioRepository.save(usuarioExistente.get()).getOrcamento();
			return produtoRepository.save(produto);
			
		}
		
		return null;
		
	}
	
	/* CRIAR UM PRODUTO E INSERE O USUARIO NA HORA DA GERACO DO PRODUTO */
	public Produto cadastrarProduto(Produto produto) {
		Produto novoProduto = new Produto();
		
		novoProduto.setCategorias(produto.getCategorias());
		novoProduto.setImg(produto.getImg());
		novoProduto.setMarcas(produto.getMarcas());
		novoProduto.setNome(produto.getNome());
		novoProduto.setPreco(produto.getPreco());
		novoProduto.setQtdProduto(produto.getQtdProduto());
		novoProduto.setUsuarios(produto.getUsuarios());
		
		produtoRepository.save(novoProduto);
		
		this.gerenciarEstoque(novoProduto);
		
		return produtoRepository.save(novoProduto);
		
	}
	
	/* VALIDACAO CADASTRO E LOGIN USUARIO */
	public Optional<Usuario> CadastrarUsuario(Usuario usuario) {	
		
		/* CONDICAO PARA INPEDIR A CRIACAO DE UM USUARIO DUPLICADO DENTRO DA APLICACAO */
		if(usuarioRepository.findByNome(usuario.getNome()).isPresent() && usuario.getId() == 0) {
			return null;
			
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);
		
		return Optional.of(usuarioRepository.save(usuario));
	}

	public Optional<UsuarioLogin> Logar(Optional<UsuarioLogin> user) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = usuarioRepository.findByNome(user.get().getNome());

		if (usuario.isPresent()) {
			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {

				String auth = user.get().getNome() + ":" + user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);

				user.get().setToken(authHeader);	
				user.get().setId(usuario.get().getId());
				user.get().setNome(usuario.get().getNome());
				user.get().setSenha(usuario.get().getSenha());

				return user;

			}
		}
		return null;
	}

}
