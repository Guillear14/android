package com.example.ud4_ejercicio.modelo

// Ficha que junta el producto elegido, la cantidad pedida y la fecha.
data class Pedido(
    // Producto que se ha pedido (puede ser Hamburguesa o Pizza por polimorfismo).
    val producto: Producto,
    // Cuantas unidades se van a pedir.
    val cantidad: Int,
    // Fecha en la que se hace el pedido en formato texto.
    val fecha: String
)
