package com.example.examenpersonas.datos

import com.example.examenpersonas.modelo.Alumno
import com.example.examenpersonas.modelo.Persona
import com.example.examenpersonas.modelo.Profesor

val CURSOS = listOf("Primero", "Segundo")

// Alumno: última letra del nombre + dígitos del NIA en posiciones impares (1ª, 3ª, 5ª...)
fun generarCodigoAlumno(nombre: String, nia: String): String {
    val digitos = nia.filterIndexed { indice, _ -> indice % 2 == 0 }
    return nombre.last().lowercaseChar() + digitos
}

// Profesor: nombre invertido + SI o NO según sea tutor
fun generarCodigoProfesor(nombre: String, tutor: Boolean): String =
    nombre.reversed() + (if (tutor) "SI" else "NO")

// Crea los datos de un alumno.
fun crearAlumno(nombre: String, curso: String, nia: String) =
    Alumno(nombre, curso, nia, generarCodigoAlumno(nombre, nia))

// Crea los datos de un profesor.
fun crearProfesor(nombre: String, cursos: List<String>, tutor: Boolean) =
    Profesor(nombre, cursos, tutor, generarCodigoProfesor(nombre, tutor))

// 13 personas: 5 alumnos y 8 profesores (como en el diseño)
fun datosIniciales(): List<Persona> = listOf(
    crearAlumno("Pepe", "Primero", "12345"),
    crearProfesor("Juan", listOf("Primero", "Segundo"), false),
    crearProfesor("María", listOf("Primero"), true),
    crearAlumno("Lucía", "Primero", "23456"),
    crearAlumno("Manolo", "Segundo", "34567"),
    crearAlumno("David", "Segundo", "45678"),
    crearProfesor("Carlos", listOf("Segundo"), false),
    crearProfesor("Juana", listOf("Primero", "Segundo"), true),
    crearAlumno("Ana", "Primero", "56789"),
    crearProfesor("Luis", listOf("Segundo"), false),
    crearProfesor("Elena", listOf("Primero"), false),
    crearProfesor("Pablo", listOf("Primero", "Segundo"), false),
    crearProfesor("Marta", listOf("Segundo"), true)
)
