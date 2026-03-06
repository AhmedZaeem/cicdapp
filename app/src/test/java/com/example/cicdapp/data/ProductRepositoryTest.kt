package com.example.cicdapp.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryTest {

    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        repository = ProductRepository()
    }

    @Test
    fun getProductsReturnsNonEmptyList() {
        val products = repository.getProducts()
        assertTrue(products.isNotEmpty())
    }

    @Test
    fun getProductsReturnsCorrectCount() {
        val products = repository.getProducts()
        assertEquals(8, products.size)
    }

    @Test
    fun allProductsHaveUniqueIds() {
        val products = repository.getProducts()
        val ids = products.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun allProductsHavePositivePrice() {
        val products = repository.getProducts()
        products.forEach { product ->
            assertTrue("${product.name} has non-positive price", product.price > 0)
        }
    }

    @Test
    fun allProductsHaveNonEmptyNames() {
        val products = repository.getProducts()
        products.forEach { product ->
            assertTrue(product.name.isNotBlank())
        }
    }

    @Test
    fun getProductByIdReturnsCorrectProduct() {
        val product = repository.getProductById(1)
        assertNotNull(product)
        assertEquals("Wireless Headphones", product?.name)
        assertEquals(49.99, product?.price ?: 0.0, 0.01)
    }

    @Test
    fun getProductByIdReturnsNullForInvalidId() {
        val product = repository.getProductById(999)
        assertNull(product)
    }

    @Test
    fun getProductByIdReturnsNullForNegativeId() {
        val product = repository.getProductById(-1)
        assertNull(product)
    }
}

