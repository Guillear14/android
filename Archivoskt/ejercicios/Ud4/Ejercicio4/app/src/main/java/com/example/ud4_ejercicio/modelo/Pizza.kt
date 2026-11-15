package com.example.ud4_ejercicio.modelo

import com.example.ud4_ejercicio.R

// Representa una pizza heredando de Producto y anadiendo tamano y masa.
data class Pizza(
    // Tamano elegido: Pequena, Mediana o Grande.
    val tamano: String = "",
    // Tipo de pizza: Romana o Margarita.
    val tipo: String = ""
): Producto(imagen = R.drawable.pizza, nombre = "Pizza")
