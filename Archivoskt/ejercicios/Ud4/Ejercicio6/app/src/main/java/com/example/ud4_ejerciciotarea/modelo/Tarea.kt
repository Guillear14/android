package com.example.ud4_ejerciciotarea.modelo

// Ficha de datos que describe una tarea individual.
data class Tarea(
    // Titulo descriptivo de la tarea.
    var titulo: String = "",
    // Explicacion detallada de lo que hay que hacer.
    var descripcion: String = "",
    // Asignatura o modulo DAM al que pertenece.
    var modulo: String = "",
    // Verdadero si la tarea es prioritaria y no puede esperar.
    var urgente: Boolean = false
)
