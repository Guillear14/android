package com.example.ud4_ejercicio.modelo

// Caja que guarda la lista de pedidos para que la pantalla los pueda mostrar.
data class RestauranteUIState(
    // Lista acumulada con todos los pedidos que el usuario ha anadido.
    val pedidos: List<Pedido> = listOf()
)
