package com.example.ud4_ejercicio.modelo

// Reúne los datos que la pantalla necesita mostrar.
data class ModuloUIState(
    // Contiene todos los módulos añadidos.
    val listaModulo: List<modulo> = listOf(),
    // Guarda el último módulo creado.
    val modulo: modulo = modulo("", false, "", "")
)
