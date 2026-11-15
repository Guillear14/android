package com.example.ud2_ejercicio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ud2_ejercicio1.ui.theme.Ud2_Ejercicio1Theme
import java.text.NumberFormat

// Punto de entrada de la app que instala el tema y lanza la pantalla.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ocupa toda la pantalla del movil respetando la barra superior e inferior.
        enableEdgeToEdge()
        setContent {
            Ud2_Ejercicio1Theme {
                // Marco base que entrega el espacio seguro para no pisar el reloj ni la bateria.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ud2Ejercicio2App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Pantalla principal que calcula el IVA del precio introducido.
@Composable
fun Ud2Ejercicio2App(modifier: Modifier = Modifier) {
    // Recuerda el texto escrito en el campo de precio para no perderlo al redibujar.
    var precioInput by remember { mutableStateOf("") }

    // Lista fija con los tres tipos de IVA permitidos.
    val listaIVA = listOf(21, 10, 3)

    // Recuerda que porcentaje de IVA ha marcado el usuario.
    var (eleccion, onOpcionElegida) = remember { mutableStateOf(listaIVA[0])}

    // Guarda el valor numerico del IVA seleccionado para la operacion matematica.
    var ivaRadio by remember { mutableStateOf(eleccion.toDouble()) }

    // Convierte el texto a numero decimal; si esta vacio o con letras pone 0.0 para no fallar.
    val precio = precioInput.toDoubleOrNull() ?: 0.0

    // Calcula el total con el IVA sumado y lo formatea a euros.
    val iva = calcularIva(precio, ivaRadio)

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto con el titulo de la aplicacion.
        Text(
            text = stringResource(R.string.calcular_precio_con_iva),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        // Campo de texto para teclear el precio base.
        CampoEdicionPrecio(
            value = precioInput,
            onValueChange = { precioInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        // Grupo de botones de opcion excluyentes para elegir el porcentaje de IVA.
        Column (Modifier.selectableGroup()) {
            listaIVA.forEach { opcion ->
                Row(
                    modifier = Modifier.selectable(
                        selected = (opcion == eleccion),
                        onClick = {
                            eleccion = opcion
                            onOpcionElegida(opcion)
                            ivaRadio = eleccion.toDouble()
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Boton circular de seleccion.
                    RadioButton(
                        selected = (opcion == eleccion),
                        onClick = {
                            eleccion = opcion
                            onOpcionElegida(opcion)
                            ivaRadio = eleccion.toDouble()
                        }
                    )
                    // Texto que acompana al boton con el tanto por ciento.
                    Text(text = "${opcion}%")
                }
            }
        }

        // Muestra el precio final formateado con el simbolo del euro.
        Text(
            text = stringResource(R.string.precio, iva),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

// Campo de texto reutilizable para meter cantidades monetarias con teclado numerico.
@Composable
fun CampoEdicionPrecio(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        // No deja meter saltos de linea para que quede en una sola fila.
        singleLine = true,
        label = { Text(text = stringResource(R.string.precio_del_producto))},
        // Muestra directamente el teclado con numeros al tocar el campo.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

// Realiza el calculo matematico del porcentaje y lo formatea a la moneda del movil.
private fun calcularIva(precio: Double, ivaRadio: Double): String {
    val iva = precio + (ivaRadio / 100 * precio)
    // Devuelve el precio final formateado automaticamente con la moneda local.
    return NumberFormat.getCurrencyInstance().format(iva)
}
