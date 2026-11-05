package com.example.ud3_ejemplo2.datos

import com.example.ud3_ejemplo2.R
import com.example.ud3_ejemplo2.modelo.Bandera

class Datos {
    fun cargarBanderas(): List<Bandera> = listOf(
        Bandera(R.string.bandera1, R.drawable.bandera1),
        Bandera(R.string.bandera2, R.drawable.bandera2),
        Bandera(R.string.bandera3, R.drawable.bandera3),
        Bandera(R.string.bandera4, R.drawable.bandera4),
        Bandera(R.string.bandera5, R.drawable.bandera5)
    )
}