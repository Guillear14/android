package com.example.ud4_ejercicio.modelo

import com.example.ud4_ejercicio.R

// Representa una hamburguesa heredando de Producto y anadiendo carne y queso.
data class Hamburguesa(
    // Tipo de carne elegida: Pollo o Ternera.
    val tipoCarne: String = "",
    // Indica si el usuario quiere anadir queso extra.
    val isQueso: Boolean = false
): Producto(imagen = R.drawable.hamburguesa, nombre = "Hamburguesa")
