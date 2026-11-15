package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.Pedido
import com.example.ud4_ejercicio.modelo.RestauranteUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Cerebro del restaurante que guarda la lista de pedidos en memoria sin perderla al rotar.
class RestauranteViewModel: ViewModel() {
    // Caja privada donde el ViewModel acumula los pedidos.
    private val _restauranteUIState = MutableStateFlow(RestauranteUIState())
    // Ventana publica de solo lectura que la pantalla observa.
    val restauranteUIState: StateFlow<RestauranteUIState> = _restauranteUIState.asStateFlow()

    // Anade un pedido nuevo a la lista creando una copia inmutable.
    fun insertarProducto(pedido: Pedido) {
        _restauranteUIState.update { estadoActual ->
            estadoActual.copy(
                pedidos = estadoActual.pedidos + pedido
            )
        }
    }
}
