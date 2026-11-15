package com.example.ud4_ejerciciotarea.modelo

// Guarda los datos que muestra la pantalla de tareas.
data class TareaUIState(
    var tarea: Tarea = Tarea(),
    var listaTareas: List<Tarea> = listOf()
)
