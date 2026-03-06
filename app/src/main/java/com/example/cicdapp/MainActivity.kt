package com.example.cicdapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cicdapp.ui.screens.CartScreen
import com.example.cicdapp.ui.screens.ProductDetailScreen
import com.example.cicdapp.ui.screens.ProductListScreen
import com.example.cicdapp.ui.theme.CicdappTheme
import com.example.cicdapp.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CicdappTheme {
                ShoppingApp()
            }
        }
    }
}

sealed class Screen {
    data object ProductList : Screen()
    data class ProductDetail(val productId: Int) : Screen()
    data object Cart : Screen()
}

@Composable
fun ShoppingApp() {
    val viewModel = remember { CartViewModel() }
    var currentScreen: Screen by remember { mutableStateOf(Screen.ProductList) }

    when (val screen = currentScreen) {
        is Screen.ProductList -> {
            ProductListScreen(
                viewModel = viewModel,
                onProductClick = { productId -> currentScreen = Screen.ProductDetail(productId) },
                onCartClick = { currentScreen = Screen.Cart }
            )
        }
        is Screen.ProductDetail -> {
            ProductDetailScreen(
                product = viewModel.getProductById(screen.productId),
                onAddToCart = { product ->
                    viewModel.addToCart(product)
                    currentScreen = Screen.ProductList
                },
                onBack = { currentScreen = Screen.ProductList }
            )
        }
        is Screen.Cart -> {
            CartScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.ProductList }
            )
        }
    }
}