package com.example.cicdapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cicdapp.ui.screens.CartScreen
import com.example.cicdapp.ui.screens.ProductListScreen
import com.example.cicdapp.ui.theme.CicdappTheme
import com.example.cicdapp.viewmodel.CartViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.cicdapp", appContext.packageName)
    }

    @Test
    fun productListDisplaysProducts() {
        composeTestRule.setContent {
            CicdappTheme {
                ProductListScreen(
                    viewModel = CartViewModel(),
                    onProductClick = {},
                    onCartClick = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Wireless Headphones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Phone Case").assertIsDisplayed()
    }

    @Test
    fun productListShowsCartButton() {
        composeTestRule.setContent {
            CicdappTheme {
                ProductListScreen(
                    viewModel = CartViewModel(),
                    onProductClick = {},
                    onCartClick = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("cart_button").assertIsDisplayed()
    }

    @Test
    fun addToCartUpdatesCount() {
        val viewModel = CartViewModel()
        composeTestRule.setContent {
            CicdappTheme {
                ProductListScreen(
                    viewModel = viewModel,
                    onProductClick = {},
                    onCartClick = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("add_to_cart_1").performClick()
        composeTestRule.onNodeWithText("Cart (1)").assertIsDisplayed()
    }

    @Test
    fun emptyCartShowsMessage() {
        composeTestRule.setContent {
            CicdappTheme {
                CartScreen(
                    viewModel = CartViewModel(),
                    onBack = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Your cart is empty").assertIsDisplayed()
    }

    @Test
    fun cartShowsAddedItems() {
        val viewModel = CartViewModel()
        val product = viewModel.products.value.first()
        viewModel.addToCart(product)

        composeTestRule.setContent {
            CicdappTheme {
                CartScreen(
                    viewModel = viewModel,
                    onBack = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Wireless Headphones").assertIsDisplayed()
    }
}