package com.example.ud4_ejemplo3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejemplo3.datos.MAX_ERRORES
import com.example.ud4_ejemplo3.datos.PUNTOS_POR_ACIERTO
import com.example.ud4_ejemplo3.datos.RONDAS
import com.example.ud4_ejemplo3.datos.listaPalabras
import com.example.ud4_ejemplo3.modelo.JuegoUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Gestiona los datos y las acciones del juego.
class JuegoViewModel : ViewModel(){

    private val _uiState = MutableStateFlow(JuegoUIState())

    val uiState: StateFlow<JuegoUIState> = _uiState.asStateFlow()

    private lateinit var palabraActual: String
    private lateinit var palabraOculta: String
    private val palabrasUsadas = mutableSetOf<String>()

    var letraUsuario by mutableStateOf("")
        private set

    init {
        reiniciarJuego()
    }

    // Actualiza los datos que muestra la pantalla.
    fun reiniciarJuego() {
        palabrasUsadas.clear()
        actualizarLetraUsuario("")
        _uiState.value = JuegoUIState(
            palabraActual = elegirPalabraAleatoria()
        )
    }

    private fun elegirPalabraAleatoria(): String {
        palabraActual = listaPalabras.random()

        if (palabrasUsadas.contains(palabraActual)) {
            return elegirPalabraAleatoria()
        } else {
            palabrasUsadas.add(palabraActual)
            palabraOculta = "_".repeat(palabraActual.length)
            return palabraOculta
        }
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarLetraUsuario(letraRespuesta: String) {
        letraUsuario = letraRespuesta.uppercase()
    }

    // Actualiza los datos que muestra la pantalla.
    fun comprobarLetraUsuario() {
        if (letraUsuario.isEmpty() || _uiState.value.isFinJuego) {
            actualizarLetraUsuario("")
            return
        }

        val letra = letraUsuario[0]
        if (letra in _uiState.value.letrasUsadas) {
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    isLetraIncorrecta = false,
                    isLetraRepetida = true
                )
            }
        } else if (palabraActual.contains(letra, ignoreCase = true)) {
            val guiones = palabraOculta.toCharArray()
            for (i in palabraActual.indices) {
                if (palabraActual[i] == letra) {
                    guiones[i] = letra
                }
            }
            palabraOculta = String(guiones)
            palabraActual = palabraActual.replace(letra, '_')
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    palabraActual = palabraOculta,
                    letrasUsadas = estadoActual.letrasUsadas + letra,
                    isLetraIncorrecta = false,
                    isLetraRepetida = false
                )
            }

            if (!palabraOculta.contains('_')) {
                val puntuacionActual = _uiState.value.puntos + PUNTOS_POR_ACIERTO
                actualizarEstadoJuego(puntuacionActual)
            }
        } else {
            registrarError(letra)
        }

        actualizarLetraUsuario("")
    }

    private fun registrarError(letraFallada: Char) {
        val estadoActual = _uiState.value
        val errores = estadoActual.errores + 1
        _uiState.value = estadoActual.copy(
            errores = errores,
            letrasUsadas = estadoActual.letrasUsadas + letraFallada,
            letrasFalladas = estadoActual.letrasFalladas + letraFallada,
            isLetraIncorrecta = true,
            isLetraRepetida = false
        )

        if (errores >= MAX_ERRORES) {
            avanzarPalabra(estadoActual.puntos, errores)
        }
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarEstadoJuego(puntuacionActual: Int) {
        avanzarPalabra(puntuacionActual, errores = 0)
    }

    private fun avanzarPalabra(puntos: Int, errores: Int) {
        if (palabrasUsadas.size == RONDAS) {
            _uiState.update { estado ->
                estado.copy(
                    puntos = puntos,
                    errores = errores,
                    isLetraIncorrecta = false,
                    isLetraRepetida = false,
                    isFinJuego = true
                )
            }
        } else {
            val siguientePalabra = elegirPalabraAleatoria()
            _uiState.update { estado ->
                estado.copy(
                    palabraActual = siguientePalabra,
                    puntos = puntos,
                    ronda = estado.ronda.inc(),
                    errores = 0,
                    letrasUsadas = emptyList(),
                    letrasFalladas = emptyList(),
                    isLetraIncorrecta = false,
                    isLetraRepetida = false
                )
            }
        }
    }
}