package com.example.ud3_ejemplo3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ud3_ejemplo3.datos.Datos
import com.example.ud3_ejemplo3.modelo.Bandera
import com.example.ud3_ejemplo3.ui.theme.Ud3_Ejemplo3Theme

// Abre la aplicacion de banderas con tarjetas desplegables.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud3_Ejemplo3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud3Ejemplo3App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Carga las banderas y llama a la lista que las dibuja.
@Composable
fun Ud3Ejemplo3App(modifier: Modifier = Modifier) {
    ListaBanderas(
        lista = Datos().cargarBanderas(),
        modifier = modifier
    )
}

// Lista vertical que genera una tarjeta desplegable por cada pais.
@Composable
fun ListaBanderas(lista: List<Bandera>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(lista) { bandera ->
            TarjetaBandera(
                bandera = bandera,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

// Tarjeta que se abre y se cierra al pulsar el icono de la flecha.
@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {
    // Guarda si esta tarjeta concreta esta abierta o cerrada ahora mismo.
    var abierto by remember { mutableStateOf(false) }

    Card(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen de la bandera del pais.
                Image(
                    painter = painterResource(bandera.imagenResourceId),
                    contentDescription = stringResource(bandera.stringResourceId)
                )
                // Nombre del pais.
                Text(
                    text = stringResource(bandera.stringResourceId),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                // Empuja el boton de la flecha hacia el extremo derecho de la tarjeta.
                Spacer(modifier = Modifier.weight(1F))
                // Boton con icono para alternar entre desplegado y colapsado.
                IconButton(onClick = { abierto = !abierto }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Desplegar informacion"
                    )
                }
            }
            // Si abierto es verdadero, dibuja el texto con la informacion extra del pais.
            if (abierto) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Text(
                    text = stringResource(bandera.descripcionResourceId),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
