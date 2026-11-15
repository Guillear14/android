package com.example.ud4_ejercicio.modelo

// Guarda los pedidos que muestra la pantalla.
data class RestauranteUIState(
    // Lista de pedidos realizados.
    val pedidos: List<Pedido> = listOf()
)
