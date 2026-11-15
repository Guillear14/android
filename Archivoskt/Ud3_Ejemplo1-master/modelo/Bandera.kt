package com.example.ud3_ejemplo1.modelo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

// Ficha de datos que asocia el nombre de un pais con la foto de su bandera.
data class Bandera(
    // Identificador numerico del texto en strings.xml.
    @StringRes val stringResourceId: Int,
    // Identificador numerico del dibujo en la carpeta drawable.
    @DrawableRes val imagenResourceId: Int
)
