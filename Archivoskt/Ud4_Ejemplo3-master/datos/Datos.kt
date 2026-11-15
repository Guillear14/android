package com.example.ud4_ejemplo3.datos

// Puntos que ganas cada vez que aciertas una palabra secreta.
const val PUNTOS_POR_ACIERTO = 20

// Cantidad de rondas que dura la partida antes de terminar.
const val RONDAS = 5

// Lista de palabras secretas disponibles para el juego.
val listaPalabras: Set<String> =
    setOf(
        "animal",
        "auto",
        "anécdota",
        "alfombra",
        "colina",
        "casa",
        "caramelo",
        "deshielo",
        "dormitorio",
        "desfile",
        "embarcación",
        "fideos",
        "planta",
        "globo",
        "guitarra",
        "herramienta",
        "camino",
        "humano",
        "escuela",
        "instituto",
        "tienda",
        "vaso"
    )
