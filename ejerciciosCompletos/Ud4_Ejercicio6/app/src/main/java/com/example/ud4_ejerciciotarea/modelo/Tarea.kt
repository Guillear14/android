package com.example.ud4_ejerciciotarea.modelo

// Guarda los datos de una tarea.
data class Tarea(
    var titulo: String = "",
    var descripcion: String = "",
    var modulo: String = "",
    var urgente: Boolean = false
)
