package com.example.ud4_ejemplo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ud4_ejemplo2.ui.OperacionViewModel
import com.example.ud4_ejemplo2.ui.theme.Ud4_Ejemplo2Theme

// Abre la aplicacion de la calculadora y carga su tema.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ud4_Ejemplo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaOperacion(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Dibuja la pantalla con los dos campos para escribir numeros y ver el resultado.
@Composable
fun PantallaOperacion(
    modifier: Modifier = Modifier,
    operacionViewModel: OperacionViewModel = viewModel()
) {
    // Escucha el estado del ViewModel y redibuja la pantalla cuando cambie el resultado.
    val operacionUIState by operacionViewModel.operacionUIState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Titulo grande de la calculadora.
        Text(
            text = "Calculadora Reactiva",
            style = MaterialTheme.typography.headlineMedium
        )

        // Campo para escribir el primer numero.
        OutlinedTextField(
            value = operacionViewModel.respuestaNumero1,
            onValueChange = { operacionViewModel.actualizarNumero1(it) },
            label = { Text("Numero 1") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )

        // Campo para escribir el segundo numero.
        OutlinedTextField(
            value = operacionViewModel.respuestaNumero2,
            onValueChange = { operacionViewModel.actualizarNumero2(it) },
            label = { Text("Numero 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(top = 16.dp)
        )

        // Muestra el resultado de la suma calculado reactivamente.
        Text(
            text = "Resultado: ${operacionUIState.resultado}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
