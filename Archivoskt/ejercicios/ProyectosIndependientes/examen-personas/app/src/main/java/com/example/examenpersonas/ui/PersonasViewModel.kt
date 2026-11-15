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

// Cerebro que gestiona la lista de alumnos y profesores y valida el formulario.
class PersonasViewModel : ViewModel() {

    // Lista reactiva de personas observada por la pantalla principal.
    private val _personas = MutableStateFlow<List<Persona>>(datosIniciales())
    val personas: StateFlow<List<Persona>> = _personas.asStateFlow()

    // Rol seleccionado en el formulario: Alumno o Profesor.
    var rol by mutableStateOf(Rol.ALUMNO)
        private set

    // Nombre tecleado por el usuario en el campo de texto.
    var nombre by mutableStateOf("")
        private set

    // Codigo NIA tecleado para el alumno.
    var nia by mutableStateOf("")
        private set

    // Curso seleccionado para el alumno en la lista de opciones.
    var curso by mutableStateOf(CURSOS[0])
        private set

    // Indica si el profesor es tutor de algun grupo.
    var tutor by mutableStateOf(false)
        private set

    // Conjunto de cursos que imparte el profesor marcado con casillas.
    var cursosProfesor by mutableStateOf(setOf<String>())
        private set

    // Comprueba si el NIA es el valor prohibido 00000 para avisar del error.
    val niaErroneo: Boolean get() = nia == "00000"

    // Valida que el alumno tenga nombre relleno, 5 cifras en el NIA y no sea 00000.
    private val alumnoValido: Boolean
        get() = nombre.isNotBlank() && nia.length == 5 && !niaErroneo

    // Valida que el profesor tenga nombre y al menos un curso marcado.
    private val profesorValido: Boolean
        get() = nombre.isNotBlank() && cursosProfesor.isNotEmpty()

    // Permite activar el boton de anadir solo si todos los datos obligatorios estan bien.
    val puedeAñadir: Boolean
        get() = when (rol) {
            Rol.ALUMNO -> alumnoValido
            Rol.PROFESOR -> profesorValido
        }

    // Calcula el codigo unico oficial solo cuando todos los campos son correctos.
    val codigo: String
        get() = when {
            rol == Rol.ALUMNO && alumnoValido -> generarCodigoAlumno(nombre, nia)
            rol == Rol.PROFESOR && profesorValido -> generarCodigoProfesor(nombre, tutor)
            else -> ""
        }

    // Cambia el formulario entre modo alumno y modo profesor.
    fun cambiarRol(nuevo: Rol) {
        rol = nuevo
    }

    // Guarda el nombre que escribe el usuario.
    fun actualizarNombre(valor: String) {
        nombre = valor
    }

    // Guarda el NIA comprobando que solo tenga numeros y como maximo 5 digitos.
    fun actualizarNia(valor: String) {
        if (valor.length <= 5 && valor.all { it.isDigit() }) nia = valor
    }

    // Guarda el curso elegido para el alumno.
    fun actualizarCurso(valor: String) {
        curso = valor
    }

    // Marca o desmarca si el profesor es tutor.
    fun actualizarTutor(valor: Boolean) {
        tutor = valor
    }

    // Anade o quita un curso a la lista de materias que da el profesor.
    fun alternarCursoProfesor(valor: String) {
        cursosProfesor = if (valor in cursosProfesor) cursosProfesor - valor else cursosProfesor + valor
    }

    // Crea la persona con sus datos validados y la suma a la lista reactiva.
    fun añadirPersona(): Boolean {
        if (!puedeAñadir) return false

        val nueva: Persona = when (rol) {
            Rol.ALUMNO -> crearAlumno(nombre, curso, nia)
            Rol.PROFESOR -> crearProfesor(nombre, CURSOS.filter { it in cursosProfesor }, tutor)
        }
        // Anade inmutablemente la persona a la lista del StateFlow.
        _personas.update { it + nueva }
        limpiarFormulario()
        return true
    }

    // Borra todos los campos del formulario para dejarlos listos para la siguiente alta.
    private fun limpiarFormulario() {
        nombre = ""
        nia = ""
        curso = CURSOS[0]
        tutor = false
        cursosProfesor = emptySet()
    }
}
