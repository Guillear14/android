package com.example.ud3_ejemplo2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ud3_ejemplo2.datos.Datos
import com.example.ud3_ejemplo2.modelo.Bandera
import com.example.ud3_ejemplo2.ui.theme.Ud3_Ejemplo2Theme

// Abre la aplicacion y muestra la lista de banderas con deteccion de toques.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud3_Ejemplo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud3Ejemplo2App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Carga las banderas del almacen y llama a la lista para dibujarlas.
@Composable
fun Ud3Ejemplo2App(modifier: Modifier = Modifier) {
    ListaBanderas(
        lista = Datos().cargarBanderas(),
        modifier = modifier
    )
}

// Crea una lista vertical eficiente que solo dibuja las tarjetas que se ven en pantalla.
@Composable
fun ListaBanderas(lista: List<Bandera>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(lista) { bandera ->
            ItemBandera(
                bandera = bandera,
                onItemClick = { banderaElegida ->
                    // Escribe en Logcat que bandera ha tocado el usuario.
                    Log.v("Bandera pulsada: ", banderaElegida.stringResourceId.toString())
                }
            )
        }
    }
}

// Tarjeta interactiva que eleva el evento de clic al padre mediante una funcion lambda.
@Composable
fun ItemBandera(bandera: Bandera, onItemClick: (Bandera) -> Unit) {
    TarjetaBandera(
        bandera = bandera,
        modifier = Modifier
            // Al hacer clic sobre la tarjeta ejecuta la accion que le pasamos.
            .clickable { onItemClick(bandera) }
            .padding(8.dp)
    )
}

// Dibuja el aspecto visual de la tarjeta con la foto y el nombre del pais.
@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Carga la foto de la bandera.
            Image(
                painter = painterResource(bandera.imagenResourceId),
                contentDescription = stringResource(bandera.stringResourceId)
            )
            // Escribe el nombre del pais a la derecha de la imagen.
            Text(
                text = stringResource(bandera.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
