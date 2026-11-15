package com.example.ud4_ejerciciotarea.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ud4_ejerciciotarea.datos.listaModulos
import com.example.ud4_ejerciciotarea.modelo.Tarea
import com.example.ud4_ejerciciotarea.modelo.TareaUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Gestiona los datos y las acciones de las tareas.
class TareaViewModel: ViewModel() {
    // Atributos realcionados con el uiState
    private val _tareaUItate = MutableStateFlow(TareaUIState())
    val tareaUIState: StateFlow<TareaUIState> = _tareaUItate.asStateFlow()

    // Atributos relacionados con la UI
    var titulo by mutableStateOf("")
        private set

    var descripcion by mutableStateOf("")
        private set

    var modulo by mutableStateOf("")
        private set

    var urgente by mutableStateOf(false)
        private set

    // Actualiza los datos que muestra la pantalla.
    fun actualizarTitulo(titulo_ui: String) {
        titulo = titulo_ui

//        val tarea = Tarea(
//            titulo,
//            _tareaUItate.value.tarea.descripcion,
//            _tareaUItate.value.tarea.modulo,
//            _tareaUItate.value.tarea.urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.titulo = titulo

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarDescripcion(desc_ui: String) {
        descripcion = desc_ui

//        val tarea = Tarea(
//            _tareaUItate.value.tarea.titulo,
//            descripcion,
//            _tareaUItate.value.tarea.modulo,
//            _tareaUItate.value.tarea.urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.descripcion = descripcion

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarModulo(modulo_ui: String) {
        modulo = modulo_ui

//        val tarea = Tarea(
//            _tareaUItate.value.tarea.titulo,
//            _tareaUItate.value.tarea.descripcion,
//            modulo,
//            _tareaUItate.value.tarea.urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.modulo = modulo

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarUrgente(urgente_ui: Boolean) {
        urgente = urgente_ui

//        val tarea = Tarea(
//            _tareaUItate.value.tarea.titulo,
//            _tareaUItate.value.tarea.descripcion,
//            _tareaUItate.value.tarea.modulo,
//            urgente)

        val tarea = _tareaUItate.value.tarea.copy()
        tarea.urgente = urgente

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = tarea
            )
        }
    }

    // Actualiza los datos que muestra la pantalla.
    fun insertarTarea() {
        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tarea = Tarea(),
                listaTareas = estadoActual.listaTareas + estadoActual.tarea
            )
        }
    }
}