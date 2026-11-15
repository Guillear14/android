package com.example.ud4_ejercicio.modelo

// Guarda los modulos que muestra la pantalla.
data class ModuloUIState(
    // Lista de modulos anadidos.
    val listaModulo: List<modulo> = listOf(),
    // Ultimo modulo creado.
    val modulo: modulo = modulo("", false, "", "")
)
