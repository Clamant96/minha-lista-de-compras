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
	
	//int i = 0;
	
	/*public void adicionarProdutoLista(Produto produto) {
		
		Optional<Produto> produtoExistente = produtoRepository.findById(produto.getId());
		Optional<Usuario> usuarioExistente = usuarioRepository.findById(produto.getUsuarios().getId());
		
		double valor;
		
		if(produto.getQtdProduto() != produtoExistente.get().getQtdProduto()) {
			// ALTERA O VALOR DO ORCAMENTO
			produto.getUsuarios().setNome(usuarioExistente.get().getNome());
			produto.getUsuarios().setOrcamento(produto.getUsuarios().getOrcamento() + produto.calcularSubTotal(produto.getPreco(), produto.getQtdProduto()));
			
			usuarioRepository.save(produto.getUsuarios());
			
		}else if(produto.getQtdProduto() == 0) {
			System.out.println("ID usuario: "+ produto.getUsuarios().getId());
			
		}else {
			// CASO REPITA A MESMA QUANTIDADE DO MESMO PRODUTO, O ORCAMENTO CONTINUA INALTERADO
			int valorMemoria;
			
			if(produto.getQtdProduto() == produtoExistente.get().getQtdProduto() && i < 1) {
				valorMemoria = produto.getQtdProduto();
				
				i++;
				
			}else {
				valorMemoria = 0;
				
			}
			
			valor = (produtoExistente.get().getPreco() * valorMemoria);
			usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + valor);
			
			usuarioRepository.save(usuarioExistente.get());
			
		}
		
	}*/
	
	/* GERENCIA O ESTOQUE SEMPRE QUE UM NOVO PRODUTO E SELECIONADO */
	public Produto gerenciarEstoque(Produto produto) {
		Optional<Produto> produtoExistente = produtoRepository.findById(produto.getId());
		
		if(produto.getUsuarios() != null) {
			Optional<Usuario> usuarioExistente = usuarioRepository.findById(produto.getUsuarios().getId());
			
			/* AJUSTA O VALOR DOS ORCAMENTO SEMPRE QUE FOR DEBITADO UM DADO PARA EVITAR RESTOS */
			double a = usuarioExistente.get().getOrcamento();
				a = Math.floor(a*100) / 100;
				usuarioExistente.get().setOrcamento(a);
			
			/* RECUPERA MEU ULTIMO VALOR DE ORCAMENTO CRIADO, LIMPA ELE E ATUALIZA COM UM NOVO VALOR */
			/* POR MEIO DO ID DE MEU PRODUTO, RETIRA O VALOR INSERIDO ATE O MOMENTO DO PRODUTO E RECALCULA, INSERINDO NOVAMENTE O VALOR NO ORCAMENTO  */
			usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() - (produtoExistente.get().getPreco() * produtoExistente.get().getQtdProduto()));
			usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + (produto.getPreco() * produto.getQtdProduto()));
			
			/*if(produto.getId() == produtoExistente.get().getId() && produto.getPreco() != produtoExistente.get().getPreco() || produto.getQtdProduto() != produtoExistente.get().getQtdProduto()) {
				
				double a = usuarioExistente.get().getOrcamento();
				
				a = Math.floor(a*100) / 100;
				usuarioExistente.get().setOrcamento(a);
				
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() - (produtoExistente.get().getPreco() * produtoExistente.get().getQtdProduto()));
				
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + (produto.getPreco() * produto.getQtdProduto()));
				
			}else {
				usuarioExistente.get().setOrcamento(usuarioExistente.get().getOrcamento() + (produto.getPreco() * produto.getQtdProduto()));
				
			}*/
			
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
