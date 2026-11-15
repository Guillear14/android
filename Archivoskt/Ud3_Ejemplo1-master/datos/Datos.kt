package com.example.ud3_ejemplo1.datos

import com.example.ud3_ejemplo1.R
import com.example.ud3_ejemplo1.modelo.Bandera

// Almacen con la lista de banderas precargadas en la app.
class Datos {
    // Devuelve la lista completa de banderas con sus nombres y fotos.
    fun cargarBanderas(): List<Bandera> {
        return listOf<Bandera>(
            Bandera(R.string.espana, R.drawable.espana),
            Bandera(R.string.francia, R.drawable.francia),
            Bandera(R.string.italia, R.drawable.italia)
        )
    }
}
