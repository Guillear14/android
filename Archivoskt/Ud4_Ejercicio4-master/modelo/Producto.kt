package com.example.ud4_ejercicio.modelo

import androidx.annotation.DrawableRes

// Clase base abierta de la que heredan la hamburguesa y la pizza.
open class Producto(
    // Imagen del producto guardada en la carpeta drawable.
    @DrawableRes val imagen: Int = 0,
    // Nombre del producto que vera el usuario.
    val nombre: String = ""
)
