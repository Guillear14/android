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

@Composable
fun PantallaJuego(
    modifier: Modifier = Modifier,
    juegoViewModel: JuegoViewModel = viewModel()
){
    val juegoUIState by juegoViewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.ahorcado),
            style = MaterialTheme.typography.headlineLarge
        )

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

        LayoutJuego(
            onLetraUsuarioChanged = {juegoViewModel.actualizarLetraUsuario(it)},
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

        Button(
            onClick = {
                if(!juegoViewModel.letraUsuario.isEmpty())
                    juegoViewModel.comprobarLetraUsuario() // Al pulsar el botón llamamos al ViewModel y comprobamos la respuesta del usuario.
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Aceptar",
                fontSize = 16.sp
            )
        }

        if(juegoUIState.isFinJuego){
            DialogoFinJuego(
                puntos = juegoUIState.puntos,
                onJugarOtraVez = { juegoViewModel.reiniciarJuego() })
        }
    }
}

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

@Composable
fun LayoutJuego(
    onLetraUsuarioChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    palabraActual: String,
    letraUsuario: String,
    isLetraIncorrecta: Boolean,
    ronda: Int,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) // Le damos un poco de relieve.
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium) // Aplicamos un clip al texto
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                text = stringResource(R.string.ronda, ronda), // En el clip mostramos el número de ronda que toca.
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = palabraActual,
                letterSpacing = 5.sp,
                style = MaterialTheme.typography.displayMedium,

                )
            OutlinedTextField(
                value = letraUsuario.uppercase(), // Asignamos como valor al TextField la respuesta pasada desde el ViewModel
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                onValueChange = onLetraUsuarioChanged,
                label = {
                    Text(text = stringResource(R.string.introduce_una_letra))
                },
                isError = isLetraIncorrecta,
                keyboardOptions = KeyboardOptions.Default.copy( // El usuario ha introducido un valor y se lanza un trigger
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions ( // Acción al lanzarse el trigger del TextField
                    onDone = { onKeyboardDone() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun DialogoFinJuego (
    puntos: Int,
    onJugarOtraVez: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actividad = LocalActivity.current

    AlertDialog(
        onDismissRequest = {
            // Controla la acción si se pulsa fuera del dialogo.
        },
        title = { Text(text = stringResource(R.string.enhorabuena))},
        text = { Text(text = stringResource(R.string.puntos, puntos))},
        dismissButton = {
            TextButton(
                onClick = {
                    actividad?.finish()
                }) {
                Text(text = stringResource(R.string.salir))
            }
        },
        confirmButton = {
            TextButton(onClick = onJugarOtraVez ) {
                Text(text = stringResource(R.string.jugar_otra_vez))
            }
        },
        modifier = modifier)
}