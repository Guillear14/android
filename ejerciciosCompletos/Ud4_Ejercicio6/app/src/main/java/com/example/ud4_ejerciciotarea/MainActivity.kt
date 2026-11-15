package com.example.ud4_ejerciciotarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejerciciotarea.datos.listaModulos
import com.example.ud4_ejerciciotarea.modelo.Tarea
import com.example.ud4_ejerciciotarea.modelo.TareaUIState
import com.example.ud4_ejerciciotarea.ui.TareaViewModel
import com.example.ud4_ejerciciotarea.ui.theme.Ud4_EjercicioTareaTheme
import kotlin.math.sin

// Abre la aplicacion y muestra su pantalla principal.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_EjercicioTareaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TareasApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Muestra las tareas y permite gestionarlas.
@Composable
fun TareasApp(
    modifier: Modifier = Modifier,
    tareaViewModel: TareaViewModel = viewModel()
) {
    val uIState by tareaViewModel.tareaUIState.collectAsState()

    // La lista de módulos se encuentra en el fichero Datos.kt
    var eleccModulo by remember { mutableStateOf(listaModulos[0])}

    // Variable para poder establecer el foco en el elemento deseado cuando se pulsa el botón (TextField de título)
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.app_tareas),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.inserta_nueva_tarea),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            // El título a mostrar será el que está almacenado en el uIState
            value = uIState.tarea.titulo,
            label = {Text (text = stringResource(R.string.titulo)) },
            onValueChange = {
                // Al escribir el título lo actualizamos en nuestro ViewModel
                tareaViewModel.actualizarTitulo(it)
            },
            singleLine = true,
            modifier = Modifier.focusRequester(focusRequester) // Establecemos el foco en este elemento
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            // La descripción a mostrar será el que está almacenado en el uIState
            value = uIState.tarea.descripcion,
            label = {Text (text = stringResource(R.string.descripcion)) },
            onValueChange = {
                // Al escribir el título lo actualizamos en nuestro ViewModel
                tareaViewModel.actualizarDescripcion(it)
            },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.modulo_asociado),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            listaModulos.forEach { modulo ->
                Row(
                    modifier = Modifier.selectable(
                        selected = (modulo == eleccModulo),
                        onClick = {
                            eleccModulo = modulo
                            // Al seleccionar el módulo lo actualizamos en nuestro ViewModel
                            tareaViewModel.actualizarModulo(modulo)
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (modulo == eleccModulo),
                        onClick = {
                            eleccModulo = modulo
                            // Al seleccionar el módulo lo actualizamos en nuestro ViewModel
                            tareaViewModel.actualizarModulo(modulo)
                        })
                    Text(text = modulo)
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.urgente),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp)
            )
            Switch(
                // El valor de urgente será el que está almacenado en el uIState
                checked = uIState.tarea.urgente,
                onCheckedChange = {
                    // Al inidicar o no si es urgente lo actualizamos en nuestro ViewModel
                    tareaViewModel.actualizarUrgente(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                tareaViewModel.insertarTarea()
                focusRequester.requestFocus() // Una vez insertada la tarea asignamos el foco al TextField de título.
            },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.insertar),
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(modifier = Modifier.padding(5.dp))

        LazyColumn(modifier = Modifier) {
            items(uIState.listaTareas) { tarea ->
                ListItem(
                    modifier = Modifier
                        .padding(10.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RectangleShape
                        ),
                    leadingContent = {
                        Text(
                            text = tarea.modulo,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    headlineContent = {
                        Text(
                            text = tarea.titulo,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = tarea.descripcion,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    trailingContent = {
                        if(tarea.urgente) {
                            Image(
                                painter = painterResource(R.drawable.urgente),
                                contentDescription = stringResource(R.string.urgente),
                                Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(10.dp)
                            )
                        }
                    }
                )

            }
        }
    }
}