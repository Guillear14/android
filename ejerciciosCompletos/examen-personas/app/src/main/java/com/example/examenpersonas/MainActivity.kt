package com.example.examenpersonas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examenpersonas.ui.PantallaAñadirPersona
import com.example.examenpersonas.ui.PantallaListado
import com.example.examenpersonas.ui.PersonasViewModel
import com.example.examenpersonas.ui.theme.ExamenPersonasTheme

// Abre la aplicacion y muestra su pantalla principal.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamenPersonasTheme {
                PersonasApp()
            }
        }
    }
}

// Muestra la pantalla principal de personas.
@Composable
fun PersonasApp(personasVm: PersonasViewModel = viewModel()) {
    val navController = rememberNavController()
    val personas by personasVm.personas.collectAsState()

    NavHost(navController = navController, startDestination = "listado") {
        composable("listado") {
            PantallaListado(
                personas = personas,
                onPersonaClick = { persona ->
                    // Futuro: mostrar la información de la persona pulsada
                    Log.d("Personas", "Pulsado: ${persona.nombre}")
                },
                onAñadirClick = { navController.navigate("anadir") }
            )
        }
        composable("anadir") {
            PantallaAñadirPersona(
                viewModel = personasVm,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
