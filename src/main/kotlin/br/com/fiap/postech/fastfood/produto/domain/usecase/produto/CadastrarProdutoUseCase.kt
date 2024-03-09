package br.com.fiap.postech.fastfood.produto.domain.usecase.produto

import br.com.fiap.postech.fastfood.produto.domain.entity.Produto
import br.com.fiap.postech.fastfood.produto.domain.repository.ProdutoRepository
import org.slf4j.LoggerFactory

class CadastrarProdutoUseCase(
    private val produtoRepository: ProdutoRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun executa(produto: Produto): Produto {

        logger.info("Cadastrando produto: $produto")

        return produtoRepository.cadastrar(produto)
    }
}