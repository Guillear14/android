package com.example.sorteos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sorteos.modelo.CampoFecha
import com.example.sorteos.modelo.SorteoJugado
import com.example.sorteos.modelo.TipoSorteo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DateTimeException
import java.time.LocalDate
import kotlin.random.Random

// Cerebro que gestiona las apuestas, genera numeros aleatorios y valida fechas.
class SorteosViewModel : ViewModel() {

    // Lista de sorteos jugados durante la sesion que observa la pantalla.
    private val _jugados = MutableStateFlow<List<SorteoJugado>>(emptyList())
    val jugados: StateFlow<List<SorteoJugado>> = _jugados.asStateFlow()

    // Cuantos decimos de loteria quiere comprar el usuario.
    var numDecimos by mutableStateOf("")
        private set

    // Lista con los numeros de 5 cifras generados al azar.
    var decimos by mutableStateOf<List<String>>(emptyList())
        private set

    // Dia introducido para el sorteo de Tu Dia.
    var dia by mutableStateOf("1")
        private set

    // Mes introducido para el sorteo de Tu Dia.
    var mes by mutableStateOf("1")
        private set

    // Ano introducido para el sorteo de Tu Dia.
    var anio by mutableStateOf("1900")
        private set

    // Guarda que campo de la fecha fallo al validar para ponerlo en rojo.
    var campoErroneo by mutableStateOf<CampoFecha?>(null)
        private set

    // Indica si la fecha completa es correcta en el calendario.
    var fechaValida by mutableStateOf(true)
        private set

    // Recuerda que campo se toco el ultimo para orientar el foco.
    private var ultimoCampo = CampoFecha.ANIO

    // Guarda cuantos decimos se quieren comprar comprobando que sean solo digitos.
    fun actualizarNumDecimos(valor: String) {
        if (valor.all { it.isDigit() }) numDecimos = valor
    }

    // Genera boletos aleatorios de 5 cifras con ceros a la izquierda (ej: 04521).
    fun generarDecimos() {
        val cantidad = numDecimos.toIntOrNull() ?: return
        decimos = List(cantidad) { "%05d".format(Random.nextInt(0, 100000)) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Comprueba si el dia, mes y ano forman una fecha real de calendario.
    fun validarFecha() {
        val d = dia.toIntOrNull() ?: 1
        val m = mes.toIntOrNull() ?: 1
        val a = anio.toIntOrNull() ?: 1900
        try {
            LocalDate.of(a, m, d)
            fechaValida = true
            campoErroneo = null
        } catch (e: DateTimeException) {
            fechaValida = false
            campoErroneo = ultimoCampo
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Guarda el dia escrito y vuelve a comprobar la validez de la fecha.
    fun actualizarDia(valor: String) {
        ultimoCampo = CampoFecha.DIA
        dia = valor
        validarFecha()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Guarda el mes escrito y vuelve a comprobar la validez de la fecha.
    fun actualizarMes(valor: String) {
        ultimoCampo = CampoFecha.MES
        mes = valor
        validarFecha()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Guarda el ano escrito y vuelve a comprobar la validez de la fecha.
    fun actualizarAnio(valor: String) {
        ultimoCampo = CampoFecha.ANIO
        anio = valor
        validarFecha()
    }

    // Registra una apuesta jugada en el historial de la sesion.
    fun registrarJugada(sorteo: SorteoJugado) {
        _jugados.update { it + sorteo }
    }
}
