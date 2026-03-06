package com.example.cicdapp.viewmodel

import com.example.cicdapp.data.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        viewModel = CartViewModel()
    }

    @Test
    fun productsLoadedOnInit() {
        val products = viewModel.products.value
        assertTrue(products.isNotEmpty())
        assertEquals(8, products.size)
    }

    @Test
    fun addProductToCart() {
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)
        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(product.id, viewModel.cartItems.value.first().product.id)
    }

    @Test
    fun addSameProductTwiceIncreasesQuantity() {
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)
        viewModel.addToCart(product)
        assertEquals(1, viewModel.cartItems.value.size)
        assertEquals(2, viewModel.cartItems.value.first().quantity)
    }

    @Test
    fun removeFromCart() {
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)
        viewModel.removeFromCart(product.id)
        assertTrue(viewModel.cartItems.value.isEmpty())
    }

    @Test
    fun updateQuantity() {
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)
        viewModel.updateQuantity(product.id, 5)
        assertEquals(5, viewModel.cartItems.value.first().quantity)
    }

    @Test
    fun updateQuantityToZeroRemovesItem() {
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)
        viewModel.updateQuantity(product.id, 0)
        assertTrue(viewModel.cartItems.value.isEmpty())
    }

    @Test
    fun getCartTotal() {
        val products = viewModel.products.value
        viewModel.addToCart(products[0])
        viewModel.addToCart(products[1])
        val expectedTotal = products[0].price + products[1].price
        assertEquals(expectedTotal, viewModel.getCartTotal(), 0.01)
    }

    @Test
    fun getCartTotalWithMultipleQuantity() {
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)
        viewModel.updateQuantity(product.id, 3)
        assertEquals(product.price * 3, viewModel.getCartTotal(), 0.01)
    }

    @Test
    fun getCartItemCount() {
        val products = viewModel.products.value
        viewModel.addToCart(products[0])
        viewModel.addToCart(products[1])
        viewModel.addToCart(products[0])
        assertEquals(3, viewModel.getCartItemCount())
    }

    @Test
    fun clearCart() {
        val products = viewModel.products.value
        viewModel.addToCart(products[0])
        viewModel.addToCart(products[1])
        viewModel.clearCart()
        assertTrue(viewModel.cartItems.value.isEmpty())
        assertEquals(0.0, viewModel.getCartTotal(), 0.01)
    }

    @Test
    fun getProductById() {
        val product = viewModel.getProductById(1)
        assertEquals("Wireless Headphones", product?.name)
    }

    @Test
    fun getProductByIdReturnsNullForInvalidId() {
        val product = viewModel.getProductById(999)
        assertEquals(null, product)
    }
}

