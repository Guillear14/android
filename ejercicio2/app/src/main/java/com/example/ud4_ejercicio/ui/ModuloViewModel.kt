package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.ModuloUIState
import com.example.ud4_ejercicio.modelo.modulo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModuloViewModel: ViewModel() {
    private val _modulosUIState = MutableStateFlow(ModuloUIState())
    val modulosUIState: StateFlow<ModuloUIState> = _modulosUIState.asStateFlow()

    fun updatelistaModulo(modulo: modulo){
        _modulosUIState.update {EstadoActual ->
            EstadoActual.copy(
                listaModulo = EstadoActual.listaModulo + modulo
            )

        }
    }

    fun updateModulo(nombre: String, urgente: Boolean,  descripcion: String, titulo: String) {
        _modulosUIState.update { EstadoActual ->
            EstadoActual.copy(
                modulo = modulo(nombre, urgente, descripcion, titulo)
            )
        }
        updatelistaModulo(_modulosUIState.value.modulo)
    }
}