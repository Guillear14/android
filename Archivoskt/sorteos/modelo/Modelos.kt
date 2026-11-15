package com.example.sorteos.modelo

// Para añadir sorteos en el futuro basta con añadir una entrada aquí
enum class TipoSorteo(val nombre: String, val premio: Int, val precio: Double) {
    DECIMO("Décimo de Navidad", 100000, 20.0),
    TU_DIA("Tu Día", 500, 2.5)
}

// Campos de la fecha de "Tu Día"
enum class CampoFecha { DIA, MES, ANIO }

// Sorteo ya jugado. "detalle" guarda los décimos o la fecha elegida
data class SorteoJugado(
    val tipo: TipoSorteo,
    val fechaJuego: String,
    val detalle: String,
    val coste: Double
)
