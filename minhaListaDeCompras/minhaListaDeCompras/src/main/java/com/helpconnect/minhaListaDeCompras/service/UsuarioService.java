package com.helpconnect.minhaListaDeCompras.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpconnect.minhaListaDeCompras.model.Produto;
import com.helpconnect.minhaListaDeCompras.model.Usuario;
import com.helpconnect.minhaListaDeCompras.repository.ProdutoRepository;
import com.helpconnect.minhaListaDeCompras.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	/* GERENCIA O ESTOQUE SEMPRE QUE UM NOVO PRODUTO E SELECIONADO */
	public Produto gerenciarEstoque(Produto produto) {
		Optional<Produto> produtoExistente = produtoRepository.findById(produto.getId());
		
		if(produto.getUsuarios() != null) {
			Optional<Usuario> usuarioExistente = usuarioRepository.findById(produto.getUsuarios().getId());
			
			/* AJUSTA O VALOR DOS ORCAMENTO SEMPRE QUE FOR DEBITADO UM DADO PARA EVITAR RESTOS */
			double a = usuarioExistente.get().getOrcamento();
				a = Math.floor(a * 100) / 100;
				usuarioExistente.get().setOrcamento(a);
			
			/* POR MEIO DO ID E POSSIVEL RECUPERAR O ULTIMO VALOR DIGITADO EM MEMORIA, DESSA FORMA CONSEGUIMOS SUBTRAIR O VALOR EXATO, PARA QUE NAO HAJA SOBRAS NEM ERROS DE CALCULOS */
			/* DESSA FORMA E SUBTRAIDO O VALOR EXATO DO PRECO X A ULTIMA QNT DESSE ULTIMO VALOS, ASSIM CONSEGUIFIR FICAR SOMENTE COM OS OUTROS PRODUTOS QUE NAO ESTAO SENDO ALTERADOS */
			/* APOS ESSA SUBTRACAO, O VALOR DO PRODUTO E RECALCULADO NOVAMENTE DE ACORDO COM A QUANTIDADE DE PRODUTOS E INSERIDOS NOVAMNTE DENTRO DO ORCAMENTO */
			if(produtoExistente.get().getPreco() != produto.getPreco() || produtoExistente.get().getQtdProduto() != produto.getQtdProduto()) {
				/* SUBITRAI O ANTIGO VALOR */
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() - (produtoExistente.get().getPreco() * produtoExistente.get().getQtdProduto()));
				/* SALVA O NOVO VALOR */
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + (produto.getPreco() * produto.getQtdProduto()));
				
			}
			
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

}
