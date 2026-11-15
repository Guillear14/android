package com.example.ud4_ejemplo3

import com.example.ud4_ejemplo3.datos.MAX_ERRORES
import com.example.ud4_ejemplo3.datos.RONDAS
import com.example.ud4_ejemplo3.ui.JuegoViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun fiveFailuresAdvanceToTheNextWordAndResetTheCounter() {
        val viewModel = JuegoViewModel()
        val wrongLetters = listOf("!", "?", "#", "$", "%")

        wrongLetters.take(MAX_ERRORES).forEach { letra ->
            viewModel.actualizarLetraUsuario(letra)
            viewModel.comprobarLetraUsuario()
        }

        assertEquals(2, viewModel.uiState.value.ronda)
        assertEquals(0, viewModel.uiState.value.errores)
    }

    @Test
    fun failuresAreLimitedToFiveForEachWord() {
        val viewModel = JuegoViewModel()
        val wrongLetters = listOf("!", "?", "#", "$", "%")

        repeat(RONDAS) {
            wrongLetters.forEach { letra ->
                viewModel.actualizarLetraUsuario(letra)
                viewModel.comprobarLetraUsuario()
            }
        }

        assertTrue(viewModel.uiState.value.isFinJuego)
        assertEquals(MAX_ERRORES, viewModel.uiState.value.errores)
    }

    @Test
    fun emptyAnswerDoesNotCountAsAFailure() {
        val viewModel = JuegoViewModel()

        viewModel.comprobarLetraUsuario()

        assertEquals(0, viewModel.uiState.value.errores)
    }

    @Test
    fun incorrectLetterIsListedAndRepeatingItDoesNotAddAnotherFailure() {
        val viewModel = JuegoViewModel()

        viewModel.actualizarLetraUsuario("!")
        viewModel.comprobarLetraUsuario()
        viewModel.actualizarLetraUsuario("!")
        viewModel.comprobarLetraUsuario()

        assertEquals(1, viewModel.uiState.value.errores)
        assertEquals(listOf('!'), viewModel.uiState.value.letrasUsadas)
        assertEquals(listOf('!'), viewModel.uiState.value.letrasFalladas)
        assertTrue(viewModel.uiState.value.isLetraRepetida)
    }
}