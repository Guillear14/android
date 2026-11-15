package com.example.ud4_ejemplo1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ud4_ejemplo1.ui.theme.Ud4_Ejemplo1Theme

// Nombre que sale en la consola de Logcat para filtrar los mensajes de esta pantalla.
private const val TAG = "MainActivity"

// Pantalla principal de la aplicacion que vigila su ciclo de vida.
class MainActivity : ComponentActivity() {
    // Se ejecuta al arrancar la pantalla por primera vez en la memoria del movil.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Deja que el diseno ocupe toda la pantalla hasta los bordes.
        enableEdgeToEdge()
        // Arranca la interfaz hecha con Jetpack Compose.
        setContent {
            Ud4_Ejemplo1Theme {
                // Estructura base que deja hueco para no tapar la barra del sistema.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Se ejecuta cuando la pantalla se vuelve visible para el usuario.
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    // Se ejecuta cuando la pantalla ya esta lista para recibir toques del usuario.
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    // Se ejecuta si la pantalla vuelve a primer plano despues de haber estado parada.
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    // Se ejecuta si la pantalla pierde el foco por un dialogo o una llamada.
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    // Se ejecuta cuando la pantalla se oculta del todo y pasa a segundo plano.
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    // Se ejecuta cuando la pantalla se destruye y se borra de la memoria del movil.
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}

// Dibuja un texto sencillo en pantalla saludando con el nombre que le pasemos.
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
