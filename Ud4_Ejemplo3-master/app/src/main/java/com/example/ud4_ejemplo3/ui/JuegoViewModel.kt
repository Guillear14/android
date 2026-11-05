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

class JuegoViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(JuegoUIState())

    val uiState: StateFlow<JuegoUIState> = _uiState.asStateFlow()

    private lateinit var palabraActual: String
    private lateinit var palabraOculta: String
    private var palabrasUsadas: MutableSet<String> = mutableSetOf()

    var letraUsuario by mutableStateOf("")
        private set

    init {
        reiniciarJuego()
    }

    fun reiniciarJuego() {
        palabrasUsadas.clear()
        _uiState.value = JuegoUIState(
            palabraActual = elegirPalabraAleatoria()
        )
    }

    private fun elegirPalabraAleatoria(): String {
        palabraActual = listaPalabras.random()

        if(palabrasUsadas.contains(palabraActual)) {
            return elegirPalabraAleatoria()
        }
        else {
            palabrasUsadas.add(palabraActual)

            palabraOculta = "_".repeat(palabraActual.length)

            return palabraOculta
        }
    }

    fun actualizarLetraUsuario(letraRespuesta: String) {
        letraUsuario = letraRespuesta.uppercase()
    }

    fun comprobarLetraUsuario() {
        if(palabraActual.contains(letraUsuario[0], ignoreCase = true)){

            val guiones = palabraOculta.toCharArray()

            // Reemplaza las ocurrencias de la letra en la posición correspondiente
            for (i in palabraActual.indices) {
                if (palabraActual[i] == letraUsuario[0]) {
                    guiones[i] = letraUsuario[0]
                }
            }
            palabraOculta = String(guiones)

            palabraActual = palabraActual.replace(letraUsuario[0], '_')

            _uiState.update { estadoActual ->
                estadoActual.copy(
                    palabraActual = palabraOculta,
                    isLetraIncorrecta = false)
            }

            if(!palabraOculta.contains('_')) {
                val puntuacionActual = _uiState.value.puntos.plus(PUNTOS_POR_ACIERTO)

                actualizarEstadoJuego(puntuacionActual)
            }
        }
        else {
            _uiState.update { estadoActual ->
                estadoActual.copy(isLetraIncorrecta = true)
            }
        }

        actualizarLetraUsuario("")
    }

    fun actualizarEstadoJuego(puntuacionActual: Int) {
        if(palabrasUsadas.size == RONDAS){
            _uiState.update { estadoActual->
                estadoActual.copy(
                    isLetraIncorrecta = false,
                    puntos = puntuacionActual,
                    isFinJuego = true
                )
            }
        }
        else {
            _uiState.update { estadoactual ->
                estadoactual.copy(
                    palabraActual = elegirPalabraAleatoria(),
                    isLetraIncorrecta = false,
                    puntos = puntuacionActual,
                    ronda = estadoactual.ronda.inc()
                )
            }
        }
    }
}