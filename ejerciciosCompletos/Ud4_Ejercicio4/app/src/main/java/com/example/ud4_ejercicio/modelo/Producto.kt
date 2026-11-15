package com.example.ud4_ejercicio.modelo

import androidx.annotation.DrawableRes

// Describe un producto que se puede pedir.
open class Producto(
    @DrawableRes val imagen: Int = 0,
    val nombre: String = ""
)
