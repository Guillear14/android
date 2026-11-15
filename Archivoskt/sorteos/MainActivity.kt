package com.example.sorteos

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sorteos.modelo.TipoSorteo
import com.example.sorteos.ui.PantallaInicial
import com.example.sorteos.ui.PantallaSorteo
import com.example.sorteos.ui.PantallaSorteosJugados
import com.example.sorteos.ui.SorteosViewModel
import com.example.sorteos.ui.theme.SorteosTheme

// Abre la aplicacion y muestra su pantalla principal.
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SorteosTheme {
                SorteosApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
// Muestra la pantalla principal de sorteos.
@Composable
fun SorteosApp(sorteosVm: SorteosViewModel = viewModel()) {
    val navController = rememberNavController()
    val jugados by sorteosVm.jugados.collectAsState()

    NavHost(navController = navController, startDestination = "inicial") {
        composable("inicial") {
            PantallaInicial(
                jugados = jugados,
                onSorteoClick = { tipo -> navController.navigate("sorteo/${tipo.name}") },
                onJugadosClick = { navController.navigate("jugados") }
            )
        }
        composable("sorteo/{tipo}") { entrada ->
            val tipo = TipoSorteo.valueOf(
                entrada.arguments?.getString("tipo") ?: TipoSorteo.DECIMO.name
            )
            PantallaSorteo(
                tipo = tipo,
                vm = sorteosVm,
                onVolver = { navController.popBackStack() }
            )
        }
        composable("jugados") {
            PantallaSorteosJugados(
                jugados = jugados,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
