package br.com.fiap.postech.fastfood.produto.domain.usecase.produto

import br.com.fiap.postech.fastfood.produto.adapter.presenter.toProdutoResponse
import br.com.fiap.postech.fastfood.produto.domain.entity.Produto
import br.com.fiap.postech.fastfood.produto.domain.repository.ProdutoRepository
import br.com.fiap.postech.fastfood.produto.domain.valueObjets.Categoria
import br.com.fiap.postech.fastfood.produto.domain.valueObjets.Descricao
import br.com.fiap.postech.fastfood.produto.domain.valueObjets.Preco
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.math.BigDecimal
import java.util.*

class BuscarProdutoPorIdUseCaseTest {

    lateinit var buscarProdutoPorIdUseCase: BuscarProdutoPorIdUseCase

    lateinit var openMocks: AutoCloseable

    lateinit var idUUID: UUID

    @Mock
    lateinit var produtoRepository: ProdutoRepository

    @BeforeEach
    fun setUp() {
        idUUID = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002")
        openMocks = MockitoAnnotations.openMocks(this)
        buscarProdutoPorIdUseCase = BuscarProdutoPorIdUseCase(
            produtoRepository,
        )
    }

    @AfterEach
    @Throws(Exception::class)
    fun tearDown() {
        openMocks.close()
    }

    @Test
    fun devePermitirBuscarProdutoPorId() {

        Mockito.`when`(produtoRepository.existeProduto(any()))
            .thenReturn(true)

        Mockito.`when`(produtoRepository.buscaPorId(any()))
            .thenReturn(createProduto())

        var executa = buscarProdutoPorIdUseCase.executa(idUUID)

        Assertions.assertThat(executa).isNotNull()
        Assertions.assertThat(executa.toProdutoResponse().categoria).isEqualTo(Categoria("lanche").categoria)
        verify(produtoRepository, Mockito.times(1)).buscaPorId(any())
    }

    @Test
    fun deveLancarExcecaoQuandoBuscarProdutoPorId() {

        Mockito.`when`(produtoRepository.existeProduto(any())).thenReturn(false)

        val idUUID = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120003")

        assertThrows<IllegalArgumentException> {
            buscarProdutoPorIdUseCase.executa(idUUID)
        }

        verify(produtoRepository, Mockito.times(0)).buscaPorId(any())
    }

    fun createProduto(
        id: UUID? = null,
        descricao: String? = null,
        categoria: String? = null,
        preco: BigDecimal? = null
    ): Produto {
        return Produto(
            id = id ?: idUUID,
            descricao = Descricao(descricao ?: "X-Bacon"),
            categoria = Categoria(categoria ?: "lanche"),
            preco = Preco(preco ?: BigDecimal("50.00"))
        )
    }
}