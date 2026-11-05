package com.example.ud4_ejercicio

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Switch
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejercicio.modelo.Hamburguesa
import com.example.ud4_ejercicio.modelo.Pedido
import com.example.ud4_ejercicio.modelo.Pizza
import com.example.ud4_ejercicio.ui.RestauranteViewModel
import com.example.ud4_ejercicio.ui.theme.Ud4_EjercicioTheme
import java.time.LocalDate

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioPedido(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Restaurante DAM",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Formulario(modifier)

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Formulario(
    modifier: Modifier = Modifier,
    restauranteViewModel: RestauranteViewModel = viewModel()
){
    val uIState by restauranteViewModel.restauranteUIState.collectAsState()

    // Variables Producto
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }
    var cantidad by remember { mutableStateOf("") }
    val listaProductos = listOf("Hamburguesa", "Pizza")
    var (eleccProducto, onOpcionProducto) = remember { mutableStateOf(listaProductos[0])}

    // Variables Hamburguesa
    var queso by remember { mutableStateOf(false) }
    val listaCarne = listOf("Pollo", "Ternera")
    var (eleccCarne, onOpcionCarne) = remember { mutableStateOf(listaCarne[0]) }

    // Variables Pizza
    var tipo_pizza by remember { mutableStateOf(false) }
    val listaTam = listOf("Pequeña", "Mediana", "Grande")
    var (elecTam, onOpcionTam) = remember { mutableStateOf(listaTam[0]) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Elige el producto",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Card(
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x5C3F51B5),
            )
        ) {
            Row(
                Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                listaProductos.forEach { opcion ->
                    val tipo = when (opcion) {
                        "Hamburguesa" -> R.drawable.hamburguesa
                        "Pizza" -> R.drawable.pizza
                        else -> R.drawable.pizza
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(tipo),
                            contentDescription = opcion,
                            modifier = Modifier.size(100.dp)
                                .selectable(
                                    selected = (opcion == eleccProducto),
                                    onClick = {
                                        eleccProducto = opcion
                                        onOpcionProducto(opcion)
                                    }
                                )
                        )
                        Row(
                            modifier = Modifier.selectable(
                                selected = (opcion == eleccProducto),
                                onClick = {
                                    eleccProducto = opcion
                                    onOpcionProducto(opcion)
                                }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (opcion == eleccProducto),
                                onClick = {
                                    eleccProducto = opcion
                                    onOpcionProducto(opcion)
                                })
                            Text(text = opcion)
                        }
                    }
                }
            }
        }

        when (eleccProducto) {
            "Hamburguesa" -> {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Queso",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Checkbox(
                            checked = queso,
                            onCheckedChange = { queso = it }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Carne",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Column(Modifier.selectableGroup()) {
                            listaCarne.forEach { opcion ->
                                Row(
                                    modifier = Modifier.selectable(
                                        selected = (opcion == eleccCarne),
                                        onClick = {
                                            eleccCarne = opcion
                                            onOpcionCarne(opcion)
                                        }
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (opcion == eleccCarne),
                                        onClick = {
                                            eleccCarne = opcion
                                            onOpcionCarne(opcion)
                                        })
                                    Text(text = opcion)
                                }
                            }
                        }
                    }
                }
            }

            "Pizza" -> {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Margarita/Romana",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Switch(
                            checked = tipo_pizza,
                            onCheckedChange = {
                                tipo_pizza = it
                            }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tamaño",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Column(Modifier.selectableGroup()) {
                            listaTam.forEach { opcion ->
                                Row(
                                    modifier = Modifier.selectable(
                                        selected = (opcion == elecTam),
                                        onClick = {
                                            elecTam = opcion
                                            onOpcionTam(opcion)
                                        }
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (opcion == elecTam),
                                        onClick = {
                                            elecTam = opcion
                                            onOpcionTam(opcion)
                                        })
                                    Text(text = opcion)
                                }
                            }
                        }
                    }
                }
            }
            else -> {
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(0.3f)
            )

            Spacer(modifier = Modifier.weight(0.1f))

            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                label = { Text("Fecha") },
                enabled = false,
                modifier = Modifier.weight(0.6f)
            )
        }
        Row (modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    val producto =
                    if (eleccProducto == "Hamburguesa") {
                        Hamburguesa(eleccCarne, queso)
                    } else {
                        Pizza(elecTam, if(tipo_pizza) "Romana" else "Margarita")
                    }
                    restauranteViewModel.insertarProducto(
                        Pedido(
                            producto,
                            cantidad.toInt(),
                            fecha
                        )
                    )
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

        HorizontalDivider(modifier = Modifier.padding(5.dp))
        Text(
            text = "${uIState.pedidos}"
        )
    }
}