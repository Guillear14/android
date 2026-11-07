package com.example.ud4_ejercicio

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejercicio.modelo.modulo
import com.example.ud4_ejercicio.ui.ModuloViewModel
import com.example.ud4_ejercicio.ui.theme.Ud4_EjercicioTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_EjercicioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioPedido(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FormularioPedido(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Modulos DAM",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Formulario(modifier)

    }
}


@Composable
fun Formulario(
    modifier: Modifier = Modifier,
    moduloViewModel: ModuloViewModel = viewModel()
){
    val uIState by moduloViewModel.modulosUIState.collectAsState()

    var descripcion by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    val listaAsignaturas = listOf("AD", "DI", "PMDM")
    var (eleccAsignatura, onOpcionAsignatura) = remember { mutableStateOf(listaAsignaturas[0])}
    var urgencia by remember { mutableStateOf(false) }





    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Elige el producto",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
            Card(
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp).fillMaxWidth(),

            ) {
                Row(
                    Modifier
                        .selectableGroup()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listaAsignaturas.forEach { opcion ->

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.selectable(
                                    selected = (opcion == eleccAsignatura),
                                    onClick = {
                                        eleccAsignatura = opcion
                                        onOpcionAsignatura(opcion)
                                    }
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (opcion == eleccAsignatura),
                                    onClick = {
                                        eleccAsignatura = opcion
                                        onOpcionAsignatura(opcion)
                                    })
                                Text(text = opcion)
                            }
                        }
                    }
                }
            }
                Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Es urgente",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Checkbox(
                        checked = urgencia,
                        onCheckedChange = { urgencia = it }
                    )
                }



            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.6f),
                    value = titulo,
                    singleLine = true,
                    onValueChange = { titulo = it },
                    label = { Text("titulo") }
                )
                Spacer(modifier = Modifier.weight(0.1f),)
                OutlinedTextField(
                    modifier = Modifier.weight(0.3f),
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("descripcion") }

                )
            }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                        moduloViewModel.updateModulo(eleccAsignatura, urgencia, descripcion, titulo)
                },
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Text(
                    text = "Añadir",
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Lista de Módulos",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Gray
        )
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
        items(uIState.listaModulo) { modulo ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (!modulo.urgente) {
                    Text(text = "${modulo.nombre} - ${modulo.titulo} - ${modulo.descripcion}")
                }else{
                    Text(text = "${modulo.nombre} - ${modulo.titulo} - ${modulo.descripcion}", color = Color.Red)
                }
            }
        }
        }
        }
    }
