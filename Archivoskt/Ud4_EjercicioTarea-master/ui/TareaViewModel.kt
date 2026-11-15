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

// Cerebro del gestor de tareas que guarda la lista y procesa el formulario.
class TareaViewModel: ViewModel() {
    // Caja interna privada donde guardamos el estado de las tareas.
    private val _tareaUItate = MutableStateFlow(TareaUIState())
    // Ventana publica de solo lectura para que la pantalla la observe.
    val tareaUIState: StateFlow<TareaUIState> = _tareaUItate.asStateFlow()

    // Variable local para el texto del titulo que escribe el usuario.
    var titulo by mutableStateOf("")
        private set

    // Variable local para la descripcion que escribe el usuario.
    var descripcion by mutableStateOf("")
        private set

    // Modulo seleccionado en el desplegable.
    var modulo by mutableStateOf("")
        private set

    // Casilla de urgencia marcada o no.
    var urgente by mutableStateOf(false)
        private set

    // Actualiza el titulo en el estado de la tarea.
    fun actualizarTitulo(titulo_ui: String) {
        titulo = titulo_ui
        val tarea = _tareaUItate.value.tarea.copy()
        tarea.titulo = titulo

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(tarea = tarea)
        }
    }

    // Actualiza la descripcion en el estado de la tarea.
    fun actualizarDescripcion(desc_ui: String) {
        descripcion = desc_ui
        val tarea = _tareaUItate.value.tarea.copy()
        tarea.descripcion = descripcion

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(tarea = tarea)
        }
    }

    // Actualiza el modulo seleccionado en la tarea.
    fun actualizarModulo(mod_ui: String) {
        modulo = mod_ui
        val tarea = _tareaUItate.value.tarea.copy()
        tarea.modulo = modulo

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(tarea = tarea)
        }
    }

    // Marca o desmarca si la tarea es urgente.
    fun actualizarUrgente(urg_ui: Boolean) {
        urgente = urg_ui
        val tarea = _tareaUItate.value.tarea.copy()
        tarea.urgente = urgente

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(tarea = tarea)
        }
    }

    // Anade la tarea a la lista y limpia los campos del formulario.
    fun insertarTarea() {
        val tarea = _tareaUItate.value.tarea.copy()

        _tareaUItate.update { estadoActual ->
            estadoActual.copy(
                tareas = estadoActual.tareas + tarea,
                tarea = Tarea()
            )
        }

        // Resetea los campos de entrada de la interfaz.
        titulo = ""
        descripcion = ""
        modulo = ""
        urgente = false
    }
}
