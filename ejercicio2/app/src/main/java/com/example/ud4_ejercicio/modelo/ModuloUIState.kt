package com.example.ud4_ejercicio.modelo

data class ModuloUIState(
    val listaModulo: List<modulo> = listOf(),
    val modulo: modulo = modulo("", false, "", "")
)
