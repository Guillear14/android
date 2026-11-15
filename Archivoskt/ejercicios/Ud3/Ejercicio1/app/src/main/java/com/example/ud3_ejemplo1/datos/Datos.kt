package com.example.ud3_ejemplo1.datos

import com.example.ud3_ejemplo1.R
import com.example.ud3_ejemplo1.modelo.Bandera

// Almacen con la lista de banderas precargadas en la app.
class Datos {
    // Devuelve la lista completa de banderas con sus nombres y fotos.
    fun cargarBanderas(): List<Bandera> {
        return listOf<Bandera>(
            Bandera(R.string.bandera1, R.drawable.bandera1),
            Bandera(R.string.bandera5, R.drawable.bandera5),
            Bandera(R.string.bandera3, R.drawable.bandera3)
        )
    }
}
