package com.example.ud4_ejerciciotarea.modelo

// Caja que guarda la lista de tareas y la tarea que se esta editando.
data class TareaUIState (
    // Lista completa de todas las tareas anadidas.
    val tareas: List<Tarea> = listOf(),
    // Tarea actual que el usuario tiene en el formulario.
    val tarea: Tarea = Tarea()
)
