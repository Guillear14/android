package com.example.ud4_ejemplo3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejemplo3.datos.PUNTOS_POR_ACIERTO
import com.example.ud4_ejemplo3.datos.RONDAS
import com.example.ud4_ejemplo3.datos.listaPalabras
import com.example.ud4_ejemplo3.modelo.JuegoUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Cerebro del juego del ahorcado que controla palabras, letras, puntos y rondas.
class JuegoViewModel : ViewModel(){

    // Estado privado donde se guardan los datos del juego.
    private val _uiState = MutableStateFlow(JuegoUIState())

    // Estado publico que lee la pantalla para redibujarse.
    val uiState: StateFlow<JuegoUIState> = _uiState.asStateFlow()

    // Palabra secreta que hay que adivinar (privada para no hacer trampas).
    private lateinit var palabraActual: String
    // Palabra con guiones bajos que se va destapando.
    private lateinit var palabraOculta: String
    // Registro de palabras que ya salieron para no repetirlas.
    private var palabrasUsadas: MutableSet<String> = mutableSetOf()

    // Letra que el usuario tiene escrita en el campo de texto ahora mismo.
    var letraUsuario by mutableStateOf("")
        private set

    // Al crear el ViewModel arranca una partida nueva limpia.
    init {
        reiniciarJuego()
    }

    // Pone los marcadores a cero y empieza el juego desde el principio.
    fun reiniciarJuego() {
        palabrasUsadas.clear()
        _uiState.value = JuegoUIState(
            palabraActual = elegirPalabraAleatoria()
        )
    }

    // Elige una palabra al azar que no haya salido antes.
    private fun elegirPalabraAleatoria(): String {
        palabraActual = listaPalabras.random()

        if(palabrasUsadas.contains(palabraActual)) {
            return elegirPalabraAleatoria()
        }
        else {
            palabrasUsadas.add(palabraActual)
            return palabraActual
        }
    }

    // Actualiza la letra que escribe el usuario en la pantalla.
    fun actualizarLetraUsuario(letra: String){
        letraUsuario = letra
    }

    // Comprueba si la letra escrita esta dentro de la palabra secreta.
    fun comprobarLetraUsuario() {
        if (letraUsuario.equals(palabraActual, ignoreCase = true)) {
            // Si acierta la palabra entera suma puntos.
            val puntosActualizados = _uiState.value.puntos.plus(PUNTOS_POR_ACIERTO)
            actualizarEstadoJuego(puntosActualizados)
        } else {
            // Si falla la letra marca fallo para poner el texto en rojo.
            _uiState.update { estadoActual ->
                estadoActual.copy(isLetraIncorrecta = true)
            }
        }
        actualizarLetraUsuario("")
    }

    // Avanza de ronda o avisa de que el juego ha terminado.
    private fun actualizarEstadoJuego(puntosActualizados: Int) {
        if (palabrasUsadas.size == RONDAS){
            // Si ya jugamos todas las rondas activamos la bandera de fin de juego.
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    isFinJuego = true,
                    puntos = puntosActualizados,
                    isLetraIncorrecta = false
                )
            }
        } else {
            // Si quedan rondas pasa a la siguiente palabra.
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    isLetraIncorrecta = false,
                    palabraActual = elegirPalabraAleatoria(),
                    puntos = puntosActualizados,
                    ronda = estadoActual.ronda.inc()
                )
            }
        }
    }
}
