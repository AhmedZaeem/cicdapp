package com.example.cicdapp.data

class ProductRepository {

    private val products = listOf(
        Product(1, "Wireless Headphones", 49.99, "Bluetooth over-ear headphones with noise cancellation"),
        Product(2, "Phone Case", 12.99, "Slim protective case for smartphones"),
        Product(3, "USB-C Cable", 8.49, "Fast charging braided cable 2m"),
        Product(4, "Laptop Stand", 34.99, "Adjustable aluminum laptop stand"),
        Product(5, "Wireless Mouse", 24.99, "Ergonomic wireless mouse with USB receiver"),
        Product(6, "Screen Protector", 6.99, "Tempered glass screen protector 2-pack"),
        Product(7, "Power Bank", 29.99, "10000mAh portable charger with dual USB"),
        Product(8, "Keyboard", 59.99, "Mechanical keyboard with RGB backlight")
    )

    fun getProducts(): List<Product> = products

    fun getProductById(id: Int): Product? = products.find { it.id == id }
}

