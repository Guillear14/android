package com.example.ud4_ejercicio.modelo

import com.example.ud4_ejercicio.R


// Representa una hamburguesa del pedido.
data class Hamburguesa(
    val tipoCarne: String = "",
    val isQueso: Boolean = false
): Producto(imagen = R.drawable.hamburguesa, nombre = "Hamburguesa")
