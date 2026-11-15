package com.example.sorteos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sorteos.modelo.CampoFecha
import com.example.sorteos.modelo.SorteoJugado
import com.example.sorteos.modelo.TipoSorteo

// ---------- PANTALLA INICIAL ----------

// Muestra la pantalla inicial de sorteos.
@Composable
fun PantallaInicial(
    jugados: List<SorteoJugado>,
    onSorteoClick: (TipoSorteo) -> Unit,
    onJugadosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarEstadisticas by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Sorteos",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Solo aparecen los sorteos disponibles; en el futuro habrá más
        TipoSorteo.entries.forEach { tipo ->
            Button(
                onClick = { onSorteoClick(tipo) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(tipo.nombre, fontWeight = FontWeight.Bold)
                    Text("Premio: ${"%,d".format(tipo.premio)} €  ·  Precio: ${"%.2f".format(tipo.precio)} €")
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onJugadosClick) {
                Text("Sorteos jugados")
            }
            OutlinedButton(onClick = { mostrarEstadisticas = true }) {
                Text("Estadísticas")
            }
        }
    }

    if (mostrarEstadisticas) {
        AlertDialog(
            onDismissRequest = { mostrarEstadisticas = false },
            title = { Text("Estadísticas") },
            text = {
                Text(
                    "Sorteos jugados: ${jugados.size}\n" +
                            "Dinero gastado: ${"%.2f".format(jugados.sumOf { it.coste })} €"
                )
            },
            confirmButton = {
                TextButton(onClick = { mostrarEstadisticas = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

// ---------- PANTALLA SORTEO ----------

@RequiresApi(Build.VERSION_CODES.O)
// Muestra los detalles del sorteo elegido.
@Composable
fun PantallaSorteo(
    tipo: TipoSorteo,
    vm: SorteosViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Al salir de la pantalla se reinicia el formulario
    DisposableEffect(tipo) {
        onDispose { vm.reiniciarEstado() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = onVolver, modifier = Modifier.align(Alignment.Start)) {
            Text("← Sorteo")
        }

        when (tipo) {
            TipoSorteo.DECIMO -> {
                Text("¡Juega al Décimo!", style = MaterialTheme.typography.headlineMedium)
                FormularioDecimo(
                    vm = vm,
                    onJuega = {
                        vm.jugarDecimo()
                        onVolver()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            TipoSorteo.TU_DIA -> {
                Text("¡Juega a Tu Día!", style = MaterialTheme.typography.headlineMedium)
                FormularioTuDia(
                    vm = vm,
                    onJuega = {
                        vm.jugarTuDia()
                        onVolver()
                    }
                )
            }
        }
    }
}

// ---------- FORMULARIO DÉCIMO ----------

@RequiresApi(Build.VERSION_CODES.O)
// Permite introducir los datos de un decimo.
@Composable
fun FormularioDecimo(
    vm: SorteosViewModel,
    onJuega: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = vm.numDecimos,
                onValueChange = vm::actualizarNumDecimos,
                label = { Text("Número de décimos") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = vm::generarDecimos) {
                Text("Aceptar")
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            gridItems(vm.decimos) { numero ->
                Text(
                    text = numero,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(12.dp)
                )
            }
        }

        // Segundo botón "Juega": solo aparece cuando ya hay décimos
        if (vm.decimos.isNotEmpty()) {
            Button(onClick = onJuega) {
                Text("Juega", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ---------- FORMULARIO TU DÍA ----------

@RequiresApi(Build.VERSION_CODES.O)
// Permite introducir los datos de tu dia.
@Composable
fun FormularioTuDia(
    vm: SorteosViewModel,
    onJuega: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CampoFechaTexto(
                etiqueta = "Día",
                valor = vm.dia,
                error = vm.campoErroneo == CampoFecha.DIA,
                onCambio = { vm.actualizarCampoFecha(CampoFecha.DIA, it) }
            )
            CampoFechaTexto(
                etiqueta = "Mes",
                valor = vm.mes,
                error = vm.campoErroneo == CampoFecha.MES,
                onCambio = { vm.actualizarCampoFecha(CampoFecha.MES, it) }
            )
            CampoFechaTexto(
                etiqueta = "Año",
                valor = vm.anio,
                error = vm.campoErroneo == CampoFecha.ANIO,
                onCambio = { vm.actualizarCampoFecha(CampoFecha.ANIO, it) },
                modifier = Modifier.width(120.dp)
            )
        }

        // El botón "Juega" solo aparece si la fecha es correcta
        if (vm.fechaValida) {
            Button(onClick = onJuega) {
                Text("Juega", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Permite escribir una parte de la fecha.
@Composable
fun CampoFechaTexto(
    etiqueta: String,
    valor: String,
    error: Boolean,
    onCambio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        isError = error,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.width(100.dp)
    )
}

// ---------- PANTALLA SORTEOS JUGADOS ----------

// Muestra los sorteos que ya se han jugado.
@Composable
fun PantallaSorteosJugados(
    jugados: List<SorteoJugado>,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver")
        }
        Text("Sorteos jugados", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        if (jugados.isEmpty()) {
            Text("Todavía no has jugado a ningún sorteo.")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(jugados) { jugado ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(12.dp)
                ) {
                    Text(jugado.tipo.nombre, fontWeight = FontWeight.Bold)
                    Text("Fecha de juego: ${jugado.fechaJuego}")
                    Text(
                        if (jugado.tipo == TipoSorteo.DECIMO) "Décimos: ${jugado.detalle}"
                        else "Fecha elegida: ${jugado.detalle}"
                    )
                    Text("Coste: ${"%.2f".format(jugado.coste)} €")
                }
            }
        }
    }
}
