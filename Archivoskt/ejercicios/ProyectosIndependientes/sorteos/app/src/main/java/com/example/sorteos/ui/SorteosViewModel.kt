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

// Gestiona los datos y las acciones de los sorteos.
class SorteosViewModel : ViewModel() {

    // ---- Sorteos jugados (durante la sesión) ----
    private val _jugados = MutableStateFlow<List<SorteoJugado>>(emptyList())
    val jugados: StateFlow<List<SorteoJugado>> = _jugados.asStateFlow()

    // ---- Estado del Décimo ----
    var numDecimos by mutableStateOf("")
        private set
    var decimos by mutableStateOf<List<String>>(emptyList())
        private set

    // ---- Estado de Tu Día ----
    var dia by mutableStateOf("1")
        private set
    var mes by mutableStateOf("1")
        private set
    var anio by mutableStateOf("1900")
        private set
    var campoErroneo by mutableStateOf<CampoFecha?>(null)
        private set
    var fechaValida by mutableStateOf(true)
        private set

    private var ultimoCampo = CampoFecha.ANIO

    // ---------- Décimo ----------

    fun actualizarNumDecimos(valor: String) {
        if (valor.all { it.isDigit() }) numDecimos = valor
    }

    // Actualiza los datos que muestra la pantalla.
    fun generarDecimos() {
        val cantidad = numDecimos.toIntOrNull() ?: return
        // Números aleatorios de 5 cifras (00000 - 99999) con ceros a la izquierda
        decimos = List(cantidad) { "%05d".format(Random.nextInt(0, 100000)) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Actualiza los datos que muestra la pantalla.
    fun jugarDecimo() {
        if (decimos.isEmpty()) return
        val coste = decimos.size * TipoSorteo.DECIMO.precio
        registrar(SorteoJugado(TipoSorteo.DECIMO, fechaHoy(), decimos.joinToString(", "), coste))
    }

    // ---------- Tu Día ----------

    @RequiresApi(Build.VERSION_CODES.O)
    // Actualiza los datos que muestra la pantalla.
    fun actualizarCampoFecha(campo: CampoFecha, valor: String) {
        if (valor.length > 4 || !valor.all { it.isDigit() }) return
        when (campo) {
            CampoFecha.DIA -> dia = valor
            CampoFecha.MES -> mes = valor
            CampoFecha.ANIO -> anio = valor
        }
        ultimoCampo = campo
        validarFecha()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Actualiza los datos que muestra la pantalla.
    fun jugarTuDia() {
        if (!fechaValida) return
        val fecha = fechaElegida() ?: return
        registrar(SorteoJugado(TipoSorteo.TU_DIA, fechaHoy(), textoFecha(fecha), TipoSorteo.TU_DIA.precio))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validarFecha() {
        val fecha = fechaElegida()
        val minima = LocalDate.of(1900, 1, 1)
        fechaValida = fecha != null && !fecha.isBefore(minima) && !fecha.isAfter(LocalDate.now())
        // Si la fecha no es válida, se marca en rojo el último campo introducido
        campoErroneo = if (fechaValida) null else ultimoCampo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fechaElegida(): LocalDate? {
        val d = dia.toIntOrNull() ?: return null
        val m = mes.toIntOrNull() ?: return null
        val a = anio.toIntOrNull() ?: return null
        return try {
            LocalDate.of(a, m, d)
        } catch (e: DateTimeException) {
            null // Por ejemplo, 31 de abril o 13 de mes
        }
    }

    // ---------- Utilidades ----------

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fechaHoy(): String = textoFecha(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    private fun textoFecha(fecha: LocalDate): String =
        "%02d/%02d/%04d".format(fecha.dayOfMonth, fecha.monthValue, fecha.year)

    private fun registrar(sorteo: SorteoJugado) {
        _jugados.update { it + sorteo }
    }

    // Se llama al salir de la pantalla del sorteo
    fun reiniciarEstado() {
        numDecimos = ""
        decimos = emptyList()
        dia = "1"
        mes = "1"
        anio = "1900"
        campoErroneo = null
        fechaValida = true
        ultimoCampo = CampoFecha.ANIO
    }
}
