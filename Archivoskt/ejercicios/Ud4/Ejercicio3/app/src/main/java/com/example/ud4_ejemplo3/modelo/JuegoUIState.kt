package com.example.ud4_ejemplo3.modelo

// Guarda los datos que muestra el juego.
data class JuegoUIState(
    // Palabra que hay que adivinar.
    val palabraActual: String = "",
    // Indica si la ultima letra fue incorrecta.
    val isLetraIncorrecta: Boolean = false,
    // Puntos conseguidos en la partida.
    val puntos: Int = 0,
    // Numero de la ronda actual.
    val ronda: Int = 1,
    // Indica si la partida ha terminado.
    val isFinJuego: Boolean = false,
    // Numero de errores cometidos al intentar adivinar la palabra actual.
    val errores: Int = 0,
    // Letras introducidas y letras que no aparecen en la palabra actual.
    val letrasUsadas: List<Char> = emptyList(),
    val letrasFalladas: List<Char> = emptyList(),
    // Se pone a true cuando el jugador repite una letra ya utilizada.
    val isLetraRepetida: Boolean = false
)
