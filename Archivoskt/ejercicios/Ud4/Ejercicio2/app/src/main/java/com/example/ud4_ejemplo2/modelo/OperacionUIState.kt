package com.example.ud4_ejemplo2.modelo

// Caja que guarda los datos que la calculadora necesita pintar en pantalla.
data class OperacionUIState(
    // Primer numero que escribe el usuario.
    val numero1: String = "",
    // Segundo numero que escribe el usuario.
    val numero2: String = "",
    val numero3: String = "",
    val numero4: String = "",
    val numero5: String = "",
    val numero6: String = "",
    // Resultado calculado de la suma.
    val resultado: String = "0",
    val resultadoResta: String = "0"
)
