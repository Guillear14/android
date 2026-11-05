package com.example.ud4_ejercicio.ui

import androidx.lifecycle.ViewModel
import com.example.ud4_ejercicio.modelo.Pedido
import com.example.ud4_ejercicio.modelo.RestauranteUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RestauranteViewModel: ViewModel() {
    private val _restauranteUIState = MutableStateFlow(RestauranteUIState())
    val restauranteUIState: StateFlow<RestauranteUIState> = _restauranteUIState.asStateFlow()

    fun insertarProducto(pedido: Pedido) {
        _restauranteUIState.update { estadoActual ->
            estadoActual.copy(
                pedidos = estadoActual.pedidos + pedido
            )
        }
    }


}