package com.example.ud4_ejemplo3.modelo

// Caja de datos que la pantalla del ahorcado necesita para dibujarse.
data class JuegoUIState(
    // La palabra secreta elegida al azar para adivinar.
    val palabraActual: String = "",
    // La palabra tapada con guiones bajos que ve el jugador.
    val palabraOculta: String = "",
    // Puntos acumulados en la partida actual.
    val puntos: Int = 0,
    // En que numero de ronda estamos jugando.
    val ronda: Int = 1,
    // Se pone a true si el jugador mete una letra que no esta.
    val isLetraIncorrecta: Boolean = false,
    // Se pone a true cuando se juegan todas las rondas para abrir el dialogo final.
    val isFinJuego: Boolean = false
)
