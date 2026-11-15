package com.example.ud4_ejemplo2.modelo

// Guarda los datos que muestra la calculadora.
data class OperacionUIState(
    // Primer numero de la operacion.
    val numero1: String = "",
    // Segundo numero de la operacion.
    val numero2: String = "",
    // Resultado de la operacion.
    val resultado: String = ""
)
