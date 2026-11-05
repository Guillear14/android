package com.example.ud3_ejemplo1.modelo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Bandera(
    // Necesitamos dos ids, uno para el texto de la bandera y otro para la imagen
    @StringRes val stringResourceId: Int,
    @DrawableRes val imagenResourceId: Int
)
