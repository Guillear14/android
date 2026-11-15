package com.example.ud4_ejercicio.modelo

// Guarda los datos de un pedido.
data class Pedido(
    val producto: Producto,
    val cantidad: Int,
    val fecha: String
)
