package com.example.ud3_ejemplo3.datos

import com.example.ud3_ejemplo3.R
import com.example.ud3_ejemplo3.modelo.Bandera

class Datos {
    fun cargarBanderas(): List<Bandera> = listOf(
        Bandera(R.string.bandera1, R.drawable.bandera1, R.string.capital1),
        Bandera(R.string.bandera2, R.drawable.bandera2, R.string.capital2),
        Bandera(R.string.bandera3, R.drawable.bandera3, R.string.capital3),
        Bandera(R.string.bandera4, R.drawable.bandera4, R.string.capital4),
        Bandera(R.string.bandera5, R.drawable.bandera5, R.string.capital5)
    )
}