package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.ModuloUIState
import com.example.ud4_ejercicio.modelo.modulo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Mantiene los datos de los módulos mientras se usa la pantalla.
class ModuloViewModel: ViewModel() {
    // Guarda el estado actual de la pantalla.
    private val _modulosUIState = MutableStateFlow(ModuloUIState())
    // Permite que la pantalla lea el estado, pero no lo cambie directamente.
    val modulosUIState: StateFlow<ModuloUIState> = _modulosUIState.asStateFlow()

    // Añade un módulo nuevo al final de la lista.
    fun updatelistaModulo(modulo: modulo){
        _modulosUIState.update {EstadoActual ->
            EstadoActual.copy(
                listaModulo = EstadoActual.listaModulo + modulo
            )

        }
    }

    // Crea un módulo y lo guarda en la lista.
    fun updateModulo(nombre: String, urgente: Boolean,  descripcion: String, titulo: String) {
        _modulosUIState.update { EstadoActual ->
            EstadoActual.copy(
                modulo = modulo(nombre, urgente, descripcion, titulo)
            )
        }
        updatelistaModulo(_modulosUIState.value.modulo)
    }
}