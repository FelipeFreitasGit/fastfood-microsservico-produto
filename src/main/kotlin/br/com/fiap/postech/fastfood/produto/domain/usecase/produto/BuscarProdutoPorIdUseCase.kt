package br.com.fiap.postech.fastfood.produto.domain.usecase.produto

import br.com.fiap.postech.fastfood.produto.domain.entity.Produto
import br.com.fiap.postech.fastfood.produto.domain.repository.ProdutoRepository
import br.com.fiap.postech.fastfood.produto.domain.valueObjets.Categoria
import br.com.fiap.postech.fastfood.produto.domain.valueObjets.CategoriaProduto
import java.util.*

class BuscarProdutoPorIdUseCase(
    private val produtoRepository: ProdutoRepository
) {

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun executa(id: UUID): Produto {

        logger.info("Buscando produto por id: $id")

        if(produtoRepository.existeProduto(id)) {
            return produtoRepository.buscaPorId(id)!!
        } else {
            logger.error("Produto $id não encontrado")
            throw IllegalArgumentException("Produto $id não encontrado")
        }
    }
}