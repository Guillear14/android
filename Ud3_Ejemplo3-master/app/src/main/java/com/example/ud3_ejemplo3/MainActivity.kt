package com.example.ud3_ejemplo3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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

@Composable
fun Ud3Ejemplo3App(modifier: Modifier = Modifier) {
    ListaBanderas(
        lista = Datos().cargarBanderas(),
        modifier = modifier
    )
}

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

@Composable
fun TarjetaBandera(bandera: Bandera, modifier: Modifier = Modifier) {

    var abierto by remember { mutableStateOf(false) }

    Card(modifier = modifier) {
        Column{
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(bandera.imagenResourceId),
                    contentDescription = stringResource(bandera.stringResourceId)
                )
                Text(
                    text = stringResource(bandera.stringResourceId),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                TarjetaBoton(
                    abierto = abierto,
                    onClick = { abierto = !abierto}
                )
            }
            if (abierto) {
                InformacionBandera(
                    bandera.capital,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun TarjetaBoton(
    abierto: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = if (abierto) painterResource(R.drawable.expand_circle_down) else painterResource(R.drawable.expand_circle_up),
            contentDescription = if (abierto) stringResource(R.string.abrir) else stringResource(R.string.cerrar)
        )

    }

}

@Composable
fun InformacionBandera(
    @StringRes info: Int,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    )  {
        Text(
            text = stringResource(R.string.capital),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(info),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}