package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.ModuloUIState
import com.example.ud4_ejercicio.modelo.modulo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Gestiona los datos y las acciones de los modulos.
class ModuloViewModel: ViewModel() {
    private val _modulosUIState = MutableStateFlow(ModuloUIState())
    val modulosUIState: StateFlow<ModuloUIState> = _modulosUIState.asStateFlow()

    // Anade un modulo al final de la lista.
    fun updatelistaModulo(modulo: modulo){
        _modulosUIState.update {EstadoActual ->
            EstadoActual.copy(
                listaModulo = EstadoActual.listaModulo + modulo
            )

        }
    }

    // Crea un modulo y lo anade a la lista.
    fun updateModulo(nombre: String, urgente: Boolean,  descripcion: String, titulo: String) {
        _modulosUIState.update { EstadoActual ->
            EstadoActual.copy(
                modulo = modulo(nombre, urgente, descripcion, titulo)
            )
        }
        updatelistaModulo(_modulosUIState.value.modulo)
    }
}