package com.example.ud4_ejercicio.modelo

import com.example.ud4_ejercicio.R

// Representa una pizza del pedido.
data class Pizza(
    val tipoPizza: String = "",
    val tamanyo: String = ""
): Producto(imagen = R.drawable.pizza, nombre = "Pizza")
