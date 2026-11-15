package com.example.examenpersonas.modelo

// Indica el tipo de persona.
enum class Rol { ALUMNO, PROFESOR }

// Define los datos comunes de una persona.
sealed interface Persona {
    val nombre: String
    val codigo: String
}

// Guarda los datos de un alumno.
data class Alumno(
    override val nombre: String,
    val curso: String,
    val nia: String,
    override val codigo: String
) : Persona

// Guarda los datos de un profesor.
data class Profesor(
    override val nombre: String,
    val cursos: List<String>,
    val tutor: Boolean,
    override val codigo: String
) : Persona
