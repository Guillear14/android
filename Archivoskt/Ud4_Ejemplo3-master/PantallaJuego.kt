package com.example.ud4_ejemplo3

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejemplo3.datos.RONDAS
import com.example.ud4_ejemplo3.ui.JuegoViewModel

// Pantalla principal que dibuja el juego del ahorcado y conecta con el ViewModel.
@Composable
fun PantallaJuego(
    modifier: Modifier = Modifier,
    juegoViewModel: JuegoViewModel = viewModel()
){
    // Escucha los cambios del estado (puntos, palabra, fin de partida) en tiempo real.
    val juegoUIState by juegoViewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titulo del juego.
        Text(
            text = stringResource(R.string.ahorcado),
            style = MaterialTheme.typography.headlineLarge
        )

        // Fila superior que muestra el numero de rondas y la puntuacion acumulada.
        Row {
            NumeroRondasJuego(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1F)
            )
            PuntuacionJuego(
                puntos = juegoUIState.puntos,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1F)
            )
        }

        // Zona central donde se ve la palabra y el campo para escribir letras.
        LayoutJuego(
            onLetraUsuarioChanged = { juegoViewModel.actualizarLetraUsuario(it) },
            onKeyboardDone = { juegoViewModel.comprobarLetraUsuario() },
            palabraActual = juegoUIState.palabraActual,
            letraUsuario = juegoViewModel.letraUsuario,
            isLetraIncorrecta = juegoUIState.isLetraIncorrecta,
            ronda = juegoUIState.ronda,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        )

        // Boton para enviar la letra y comprobar si es correcta.
        Button(
            onClick = {
                if(!juegoViewModel.letraUsuario.isEmpty())
                    juegoViewModel.comprobarLetraUsuario()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Aceptar",
                fontSize = 16.sp
            )
        }

        // Si se terminaron las rondas abre el cartel modal de fin de juego.
        if(juegoUIState.isFinJuego){
            DialogoFinJuego(
                puntos = juegoUIState.puntos,
                onJugarOtraVez = { juegoViewModel.reiniciarJuego() })
        }
    }
}

// Tarjeta que ensena los puntos que lleva ganados el usuario.
@Composable
fun PuntuacionJuego(
    puntos: Int,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Text (
            text = stringResource(R.string.puntuacion, puntos),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

// Tarjeta que ensena el numero total de rondas del juego.
@Composable
fun NumeroRondasJuego(
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Text (
            text = stringResource(R.string.rondas, RONDAS),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

// Bloque central con la palabra a adivinar y el campo de texto.
@Composable
fun LayoutJuego(
    onLetraUsuarioChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    palabraActual: String,
    letraUsuario: String,
    isLetraIncorrecta: Boolean,
    ronda: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Muestra en que ronda estamos.
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = stringResource(R.string.contador_palabras, ronda),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            // Ensenia la palabra secreta.
            Text(
                text = palabraActual,
                fontSize = 40.sp,
                style = MaterialTheme.typography.headlineMedium
            )
            // Instruccion para el usuario.
            Text(
                text = stringResource(R.string.instrucciones),
                style = MaterialTheme.typography.titleMedium
            )
            // Campo de texto donde el usuario mete la letra o palabra.
            OutlinedTextField(
                value = letraUsuario,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onLetraUsuarioChanged,
                label = {
                    if (isLetraIncorrecta) {
                        Text(stringResource(R.string.letra_incorrecta))
                    } else {
                        Text(stringResource(R.string.introduce_tu_letra))
                    }
                },
                isError = isLetraIncorrecta,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                )
            )
        }
    }
}

// Ventana emergente que sale al terminar la partida para jugar otra vez o salir.
@Composable
fun DialogoFinJuego(
    puntos: Int,
    onJugarOtraVez: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(R.string.fin_juego)) },
        text = { Text(text = stringResource(R.string.puntos, puntos)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity?.finish()
                }
            ) {
                Text(text = stringResource(R.string.salir))
            }
        },
        confirmButton = {
            TextButton(onClick = onJugarOtraVez) {
                Text(text = stringResource(R.string.jugar_otra_vez))
            }
        }
    )
}
