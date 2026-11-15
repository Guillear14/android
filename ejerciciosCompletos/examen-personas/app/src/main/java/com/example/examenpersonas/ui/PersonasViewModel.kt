package com.example.examenpersonas.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.examenpersonas.datos.CURSOS
import com.example.examenpersonas.datos.crearAlumno
import com.example.examenpersonas.datos.crearProfesor
import com.example.examenpersonas.datos.datosIniciales
import com.example.examenpersonas.datos.generarCodigoAlumno
import com.example.examenpersonas.datos.generarCodigoProfesor
import com.example.examenpersonas.modelo.Persona
import com.example.examenpersonas.modelo.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Gestiona los datos y las acciones de las personas.
class PersonasViewModel : ViewModel() {

    // Listado de personas (estado observado por la pantalla de listado)
    private val _personas = MutableStateFlow<List<Persona>>(datosIniciales())
    val personas: StateFlow<List<Persona>> = _personas.asStateFlow()

    // Estado del formulario "Añadir persona"
    var rol by mutableStateOf(Rol.ALUMNO)
        private set
    var nombre by mutableStateOf("")
        private set
    var nia by mutableStateOf("")
        private set
    var curso by mutableStateOf(CURSOS[0])
        private set
    var tutor by mutableStateOf(false)
        private set
    var cursosProfesor by mutableStateOf(setOf<String>())
        private set

    // Validaciones
    val niaErroneo: Boolean get() = nia == "00000"

    private val alumnoValido: Boolean
        get() = nombre.isNotBlank() && nia.length == 5 && !niaErroneo

    private val profesorValido: Boolean
        get() = nombre.isNotBlank() && cursosProfesor.isNotEmpty()

    val puedeAñadir: Boolean
        get() = when (rol) {
            Rol.ALUMNO -> alumnoValido
            Rol.PROFESOR -> profesorValido
        }

    // El código solo aparece cuando los datos son correctos
    val codigo: String
        get() = when {
            rol == Rol.ALUMNO && alumnoValido -> generarCodigoAlumno(nombre, nia)
            rol == Rol.PROFESOR && profesorValido -> generarCodigoProfesor(nombre, tutor)
            else -> ""
        }

    // Acciones del formulario
    fun cambiarRol(nuevo: Rol) {
        rol = nuevo
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarNombre(valor: String) {
        nombre = valor
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarNia(valor: String) {
        // Solo dígitos y máximo 5 cifras
        if (valor.length <= 5 && valor.all { it.isDigit() }) nia = valor
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarCurso(valor: String) {
        curso = valor
    }

    // Actualiza los datos que muestra la pantalla.
    fun actualizarTutor(valor: Boolean) {
        tutor = valor
    }

    // Actualiza los datos que muestra la pantalla.
    fun alternarCursoProfesor(valor: String) {
        cursosProfesor = if (valor in cursosProfesor) cursosProfesor - valor else cursosProfesor + valor
    }

    // Actualiza los datos que muestra la pantalla.
    fun añadirPersona(): Boolean {
        if (!puedeAñadir) return false

        val nueva: Persona = when (rol) {
            Rol.ALUMNO -> crearAlumno(nombre, curso, nia)
            Rol.PROFESOR -> crearProfesor(nombre, CURSOS.filter { it in cursosProfesor }, tutor)
        }
        _personas.update { it + nueva }
        limpiarFormulario()
        return true
    }

    private fun limpiarFormulario() {
        nombre = ""
        nia = ""
        curso = CURSOS[0]
        tutor = false
        cursosProfesor = emptySet()
    }
}
