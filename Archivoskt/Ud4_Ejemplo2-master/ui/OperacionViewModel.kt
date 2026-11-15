package com.example.ud4_ejemplo2.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejemplo2.modelo.OperacionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Cerebro de la calculadora que hace las cuentas y guarda los numeros sin borrarse al girar.
class OperacionViewModel : ViewModel() {
    // Caja privada donde el ViewModel cambia los datos internamente.
    private val _operacionUItate = MutableStateFlow(OperacionUIState())

    // Ventana publica para que la pantalla solo pueda leer los datos sin tocarlos.
    val operacionUIState: StateFlow<OperacionUIState> = _operacionUItate.asStateFlow()

    // Variable local para recordar el texto del primer numero mientras se teclea.
    var respuestaNumero1 by mutableStateOf("")
        private set

    // Variable local para recordar el texto del segundo numero mientras se teclea.
    var respuestaNumero2 by mutableStateOf("")
        private set

    // Guarda el primer numero y recalcula el resultado de la suma.
    fun actualizarNumero1(num: String) {
        respuestaNumero1 = num

        // Modifica el estado creando una copia nueva con el numero1 cambiado.
        _operacionUItate.update { estadoActual ->
            estadoActual.copy(
                numero1 = respuestaNumero1
            )
        }

        calcular()
    }

    // Guarda el segundo numero y recalcula el resultado de la suma.
    fun actualizarNumero2(num: String) {
        respuestaNumero2 = num

        // Modifica el estado creando una copia nueva con el numero2 cambiado.
        _operacionUItate.update { estadoActual ->
            estadoActual.copy (
                numero2 = respuestaNumero2
            )
        }

        calcular()
    }

    // Hace la suma convirtiendo el texto a entero seguro sin romperse si esta vacio.
    private fun calcular() {
        _operacionUItate.update { estadoActual ->
            // Si el texto no es un numero valido le asigna 0 para evitar fallos.
            val num1 = estadoActual.numero1.toIntOrNull() ?: 0
            val num2 = estadoActual.numero2.toIntOrNull() ?: 0

            // Guarda el resultado como texto dentro del estado.
            estadoActual.copy(
                resultado = (num1 + num2).toString()
            )
        }
    }
}
